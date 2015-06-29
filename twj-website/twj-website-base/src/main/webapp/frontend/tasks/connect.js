module.exports = {
    options: {
        port: 9000,
        livereload: 35729,
        hostname: 'localhost',
        base: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>'
    },
    livereload: {
        options: {
            open: true
        }
    }
};
