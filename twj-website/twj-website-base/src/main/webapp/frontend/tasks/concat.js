module.exports = {
    main: {
        options: {
            banner: '<%= banner %>',
            stripBanners: false
        },
        files: [{
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/main.js': ['<%= config.assets %>/js/*.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/core.js': ['<%= config.assets %>/js/core/*.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/app.js': ['<%= config.assets %>/js/app/*.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/website.js': ['<%= config.assets %>/js/website/*.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/webapp.js': ['<%= config.assets %>/js/webapp/*.js']
            }]
    }
};
