module.exports = {
    main: {
        options: {
            banner: '<%= banner %>',
            sourceMap: true,
            sourceMapName: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/main.js.map'
        },
        files: {
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/assets/js/main.min.js': ['<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/main.js']
        }
    },
    core: {
        options: {
            banner: '<%= banner %>',
            sourceMap: true,
            sourceMapName: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/core.js.map'
        },
        files: {
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/core.min.js': ['<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/core.js']
        }
    },
    app: {
        options: {
            banner: '<%= banner %>',
            sourceMap: true,
            sourceMapName: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/app.js.map'
        },
        files: {
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/app.min.js': ['<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/app.js']
        }
    },
    website: {
        options: {
            banner: '<%= banner %>',
            sourceMap: true,
            sourceMapName: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/website.js.map'
        },
        files: {
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/website.min.js': ['<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/website.js']
        }
    },
    webapp: {
        options: {
            banner: '<%= banner %>',
            sourceMap: true,
            sourceMapName: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/webapp.js.map'
        },
        files: {
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/webapp.min.js': ['<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/webapp.js']
        }
    },
    specifics: {
        options: {
            banner: '<%= banner %>'
        },
        files: [{
                expand: true,
                cwd: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/specifics',
                src: '**/*.js',
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/specifics',
                ext: '.min.js'
            }]
    }
};
