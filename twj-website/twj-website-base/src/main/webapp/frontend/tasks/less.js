module.exports = {
    options: {
        banner: '<%= banner %>',
        paths: ['<%= config.bower_components %>/bootstrap/less']
    },
    development: {
        cwd: "<%= config.assets %>/less/",
        src: [
            '**/*.less'
        ],
        expand: true,
        rename: function (dest, src) {
            return dest + src.replace('.less', '.css');
        },
        dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/css/'
    },
    production: {
        options: {
            cleancss: true,
			compress: true,
            report: 'min'
        },
        cwd: "<%= config.assets %>/less/",
        src: [
            '**/*.less'
        ],
        expand: true,
        rename: function (dest, src) {
            return dest + src.replace('.less', '.min.css');
        },
        dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/css/'
    }
};
