// Copyright 2007-2011 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, http://www.gnu.org/licenses/lgpl.html
//  MPL, Mozilla Public License 1.1, http://www.mozilla.org/MPL
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.
package org.tawja.platform.plugins.database.jdbc;

import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

/**
 * A lightweight standalone JDBC connection pool manager.
 * <p>
 * The public methods of this class are thread-safe.
 * <p>
 * Home page:
 * <a href="http://www.source-code.biz/miniconnectionpoolmanager">www.source-code.biz/miniconnectionpoolmanager</a><br>
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
 * Multi-licensed: EPL / LGPL / MPL.
 */
public class MiniConnectionPoolManager { // implements DataSource {

    private ConnectionPoolDataSource dataSource;
    private int maxConnections;
    private long timeoutMs;
    private int maxIdleConnectionLife;
    private PrintWriter logWriter;
    private Semaphore semaphore;
    private PoolConnectionEventListener poolConnectionEventListener;
    private Timer timer; // Clean pool after unused timeout

    // The following variables must only be accessed within synchronized blocks.
    // @GuardedBy("this") could by used in the future.
    //private LinkedList<PooledConnection> recycledConnections;          // list of inactive PooledConnections
    private Queue<TimeStampedPooledConnection> recycledConnections;          // list of inactive PooledConnections
    private int activeConnections;            // number of active (open) connections of this pool
    private boolean isDisposed;                   // true if this connection pool has been disposed
    private boolean doPurgeConnection;            // flag to purge the connection currently beeing closed instead of recycling it
    private PooledConnection connectionInTransition;       // a PooledConnection which is currently within a PooledConnection.getConnection() call, or null

    /**
     * Constructs a MiniConnectionPoolManager object with a timeout of 60
     * seconds.
     *
     * @param dataSource the data source for the connections.
     * @param maxConnections the maximum number of connections.
     */
    public MiniConnectionPoolManager(ConnectionPoolDataSource dataSource, int maxConnections) {
        this(dataSource, maxConnections, 60, 60);
    }

    /**
     * Constructs a MiniConnectionPoolManager object.
     *
     * @param dataSource the data source for the connections.
     * @param maxConnections the maximum number of connections.
     * @param timeout the maximum time in seconds to wait for a free connection.
     * @param maxIdleConnectionLife the maximum time in seconds to keep an idle
     * connection to wait to be used.
     */
    public MiniConnectionPoolManager(ConnectionPoolDataSource dataSource, int maxConnections, int timeout, int maxIdleConnectionLife) {
        if (dataSource == null) {
            throw new InvalidParameterException("dataSource cant be null");
        }
        if (maxConnections < 1) {
            throw new InvalidParameterException("maxConnections must be > 1");
        }
        if (timeout < 1) {
            throw new InvalidParameterException("timeout must be > 1");
        }
        if (maxIdleConnectionLife < 1) {
            throw new InvalidParameterException("maxIdleConnectionLife must be > 1");
        }

        this.dataSource = dataSource;
        this.maxConnections = maxConnections;
        this.maxIdleConnectionLife = maxIdleConnectionLife;
        this.timeoutMs = timeout * 1000L;

        try {
            logWriter = dataSource.getLogWriter();
        } catch (SQLException e) {
        }

        semaphore = new Semaphore(maxConnections, true);
        //recycledConnections = new LinkedList<PooledConnection>();
        recycledConnections = new PriorityQueue<TimeStampedPooledConnection>();
        poolConnectionEventListener = new PoolConnectionEventListener();

        // Start the monitor
        timer = new Timer(getClass().getSimpleName(), true);
        timer.schedule(new ConnectionMonitor(this), this.maxIdleConnectionLife, this.maxIdleConnectionLife);
    }

    /**
     * Closes all unused pooled connections.
     */
    public synchronized void dispose() throws SQLException {
        if (isDisposed) {
            return;
        }
        isDisposed = true;
        SQLException e = null;
        while (!recycledConnections.isEmpty()) {

            //PooledConnection pconn = recycledConnections.remove();
            TimeStampedPooledConnection pcts = recycledConnections.poll();
            if (pcts == null) {
                throw new EmptyStackException();
            }
            PooledConnection pconn = pcts.getPConn();

            try {
                pconn.close();
            } catch (SQLException e2) {
                if (e == null) {
                    e = e2;
                }
            }
        }

        // Stop the monitor
        timer.cancel();

        if (e != null) {
            throw e;
        }
    }

    /**
     * Retrieves a connection from the connection pool.
     *
     * <p>
     * If <code>maxConnections</code> connections are already in use, the method
     * waits until a connection becomes available or <code>timeout</code>
     * seconds elapsed. When the application is finished using the connection,
     * it must close it in order to return it to the pool.
     *
     * @return a new <code>Connection</code> object.
     * @throws TimeoutException when no connection becomes available within
     * <code>timeout</code> seconds.
     */
    //@Override
    public Connection getConnection() throws SQLException {
        return getConnection2(timeoutMs);
    }

    //@Override
    public Connection getConnection(String user, String password) throws SQLException {
        // No way to change the user and password for the pool
        return getConnection();
    }

    private Connection getConnection2(long timeoutMs) throws SQLException {
        // This routine is unsynchronized, because semaphore.tryAcquire() may block.
        synchronized (this) {
            if (isDisposed) {
                throw new IllegalStateException("Connection pool has been disposed.");
            }
        }
        try {
            if (!semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for a database connection.", e);
        }
        boolean ok = false;
        try {
            Connection conn = getConnection3();
            ok = true;
            return conn;
        } finally {
            if (!ok) {
                semaphore.release();
            }
        }
    }

    private synchronized Connection getConnection3() throws SQLException {
        if (isDisposed) {
            // test again within synchronized lock
            throw new IllegalStateException("Connection pool has been disposed.");
        }
        PooledConnection pconn;
        if (!recycledConnections.isEmpty()) {
            //pconn = recycledConnections.remove();
            TimeStampedPooledConnection pcts = recycledConnections.poll();
            if (pcts == null) {
                throw new EmptyStackException();
            }
            pconn = pcts.getPConn();
        } else {
            pconn = dataSource.getPooledConnection();
            pconn.addConnectionEventListener(poolConnectionEventListener);
        }
        Connection conn;
        try {
            // The JDBC driver may call ConnectionEventListener.connectionErrorOccurred()
            // from within PooledConnection.getConnection(). To detect this within
            // disposeConnection(), we temporarily set connectionInTransition.
            connectionInTransition = pconn;
            conn = pconn.getConnection();
        } finally {
            connectionInTransition = null;
        }
        activeConnections++;
        assertInnerState();
        return conn;
    }

    /**
     * Retrieves a connection from the connection pool and ensures that it is
     * valid by calling {@link Connection#isValid(int)}.
     *
     * <p>
     * If a connection is not valid, the method tries to get another connection
     * until one is valid (or a timeout occurs).
     *
     * <p>
     * Pooled connections may become invalid when e.g. the database server is
     * restarted.
     *
     * <p>
     * This method is slower than {@link #getConnection()} because the JDBC
     * driver has to send an extra command to the database server to test the
     * connection.
     *
     * <p>
     * This method requires Java 1.6 or newer.
     *
     * @throws TimeoutException when no valid connection becomes available
     * within <code>timeout</code> seconds.
     */
    public Connection getValidConnection() {
        long time = System.currentTimeMillis();
        long timeoutTime = time + timeoutMs;
        int triesWithoutDelay = getInactiveConnections() + 1;
        while (true) {
            Connection conn = getValidConnection2(time, timeoutTime);
            if (conn != null) {
                return conn;
            }
            triesWithoutDelay--;
            if (triesWithoutDelay <= 0) {
                triesWithoutDelay = 0;
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted while waiting for a valid database connection.", e);
                }
            }
            time = System.currentTimeMillis();
            if (time >= timeoutTime) {
                throw new TimeoutException("Timeout while waiting for a valid database connection.");
            }
        }
    }

    private Connection getValidConnection2(long time, long timeoutTime) {
        long rtime = Math.max(1, timeoutTime - time);
        Connection conn;
        try {
            conn = getConnection2(rtime);
        } catch (SQLException e) {
            return null;
        }
        rtime = timeoutTime - System.currentTimeMillis();
        int rtimeSecs = Math.max(1, (int) ((rtime + 999) / 1000));
        try {
            if (conn.isValid(rtimeSecs)) {
                return conn;
            }
        } catch (SQLException e) {
        }
        // This Exception should never occur. If it nevertheless occurs, it's because of an error in the
        // JDBC driver which we ignore and assume that the connection is not valid.
        // When isValid() returns false, the JDBC driver should have already called connectionErrorOccurred()
        // and the PooledConnection has been removed from the pool, i.e. the PooledConnection will
        // not be added to recycledConnections when Connection.close() is called.
        // But to be sure that this works even with a faulty JDBC driver, we call purgeConnection().
        purgeConnection(conn);
        return null;
    }

// Purges the PooledConnection associated with the passed Connection from the connection pool.
    private synchronized void purgeConnection(Connection conn) {
        try {
            doPurgeConnection = true;
            // (A potential problem of this program logic is that setting the doPurgeConnection flag
            // has an effect only if the JDBC driver calls connectionClosed() synchronously within
            // Connection.close().)
            conn.close();
        } catch (SQLException e) {
        } // ignore exception from close()
        finally {
            doPurgeConnection = false;
        }
    }

    private synchronized void recycleConnection(PooledConnection pconn) {
        if (isDisposed || doPurgeConnection) {
            disposeConnection(pconn);
            return;
        }
        if (activeConnections <= 0) {
            throw new AssertionError();
        }
        activeConnections--;
        semaphore.release();
        //recycledConnections.add(pconn);
        recycledConnections.add(new TimeStampedPooledConnection(pconn));
        assertInnerState();
    }

    private synchronized void disposeConnection(PooledConnection pconn) {
        pconn.removeConnectionEventListener(poolConnectionEventListener);
        if (!recycledConnections.remove(pconn)
                && pconn != connectionInTransition) {
            // If the PooledConnection is not in the recycledConnections list
            // and is not currently within a PooledConnection.getConnection() call,
            // we assume that the connection was active.
            if (activeConnections <= 0) {
                throw new AssertionError();
            }
            activeConnections--;
            semaphore.release();
        }
        closeConnectionAndIgnoreException(pconn);
        assertInnerState();
    }

    private void closeConnectionAndIgnoreException(PooledConnection pconn) {
        try {
            pconn.close();
        } catch (SQLException e) {
            log("Error while closing database connection: " + e.toString());
        }
    }

    private void log(String msg) {
        String s = "MiniConnectionPoolManager: " + msg;
        try {
            if (logWriter == null) {
                System.err.println(s);
            } else {
                logWriter.println(s);
            }
        } catch (Exception e) {
        }
    }

    private synchronized void assertInnerState() {
        if (activeConnections < 0) {
            throw new AssertionError();
        }
        if (activeConnections + recycledConnections.size() > maxConnections) {
            throw new AssertionError();
        }
        if (activeConnections + semaphore.availablePermits() > maxConnections) {
            throw new AssertionError();
        }
    }

    /**
     * Returns the number of active (open) connections of this pool.
     * <p>
     * This is the number of <code>Connection</code> objects that have been
     * issued by {@link #getConnection()}, for which
     * <code>Connection.close()</code> has not yet been called.
     *
     * @return the number of active connections.
     *
     */
    public synchronized int getActiveConnections() {
        return activeConnections;
    }

    /**
     * Returns the number of inactive (unused) connections in this pool.
     * <p>
     * This is the number of internally kept recycled connections, for which
     * <code>Connection.close()</code> has been called and which have not yet
     * been reused.
     *
     * @return the number of inactive connections.
     *
     */
    public synchronized int getInactiveConnections() {
        return recycledConnections.size();
    }

    /**
     * Thrown in {@link #getConnection()} or {@link #getValidConnection()} when
     * no free connection becomes available within <code>timeout</code> seconds.
     */
    public static class TimeoutException extends RuntimeException {

        private static final long serialVersionUID = 1;

        public TimeoutException() {
            super("Timeout while waiting for a free database connection.");
        }

        public TimeoutException(String msg) {
            super(msg);
        }
    }

    /**
     * Object to hold the Connection under the queue, with a TimeStamp.
     *
     * @author Daniel Jurado
     */
    private class TimeStampedPooledConnection implements Comparable<TimeStampedPooledConnection> {

        private PooledConnection pconn;
        private Calendar timeStamp;

        private PooledConnection getPConn() {
            return this.pconn;
        }

        private Calendar getTimeStamp() {
            return this.timeStamp;
        }

        private TimeStampedPooledConnection(PooledConnection pconn) {
            this.timeStamp = Calendar.getInstance();
            this.pconn = pconn;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(TimeStampedPooledConnection other) {
            return (int) (other.getTimeStamp().getTimeInMillis() - this
                    .getTimeStamp().getTimeInMillis());
        }
    }

    private class PoolConnectionEventListener implements ConnectionEventListener {

        @Override
        public void connectionClosed(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection) event.getSource();
            recycleConnection(pconn);
        }

        @Override
        public void connectionErrorOccurred(ConnectionEvent event) {
            PooledConnection pconn = (PooledConnection) event.getSource();
            disposeConnection(pconn);
        }
    }

    /**
     * Looks for recycled connections older than the specified seconds.
     *
     * @author Daniel Jurado
     *
     */
    private class ConnectionMonitor extends TimerTask {

        private MiniConnectionPoolManager owner;

        private ConnectionMonitor(MiniConnectionPoolManager owner) {
            this.owner = owner;
        }

        @Override
        public void run() {
            Calendar now = Calendar.getInstance();
            synchronized (owner) {
                Iterator<TimeStampedPooledConnection> iterator = recycledConnections.iterator();
                while (iterator.hasNext()) {
                    TimeStampedPooledConnection pcts = iterator.next();
                    int delta = (int) ((now.getTimeInMillis() - pcts.getTimeStamp().getTimeInMillis()) / 1000);
                    if (delta >= maxIdleConnectionLife) {
                        closeConnectionAndIgnoreException(pcts.getPConn());
                        iterator.remove();
                        System.out.println("ConnectionMonitor : 1 Connection removed");
                    }
                }
            }
        }
    }

} // end class MiniConnectionPoolManager
