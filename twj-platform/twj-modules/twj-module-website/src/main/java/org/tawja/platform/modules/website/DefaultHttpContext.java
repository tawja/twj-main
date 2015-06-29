package org.tawja.platform.modules.website;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.service.http.HttpContext;

public class DefaultHttpContext implements HttpContext {

    public DefaultHttpContext() {
    }

    @Override
    public boolean handleSecurity(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        return true; // TODO support pluggable security
    }

    @Override
    public URL getResource(String name) {
        return getResource(name.replace("tmp/", ""));
    }

    @Override
    public String getMimeType(String name) {
        if (name.endsWith(".css")) {
            return "text/css";
        }
        return "*"; // TODO map with real types
    }
}
