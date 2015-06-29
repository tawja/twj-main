module.exports = {
    maven: {
        files: [{
                expand: true,
                cwd: '<%= gruntMavenProperties.directoryToWatch %>',
                src: ['./**/*.*'],
                dest: '.'
            }]
    },
    main: {
        files: [{
                expand: true,
                cwd: '<%= config.assets %>/css',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/css'
            }, {
                expand: true,
                cwd: '<%= config.assets %>/img',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/img'
            }, {
                expand: true,
                cwd: '<%= gruntMavenProperties.targetPath %>/generated-design',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/img'
            }, {
                expand: true,
                cwd: '<%= config.assets %>/icons',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/icons'
            }, {
                expand: true,
                cwd: '<%= config.assets %>/<%= config.lib %>',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>'
            }]
    },
    specifics: {
        files: [{
                expand: true,
                cwd: '<%= config.assets %>/js/specifics',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/js/specifics'
            }]
    },
    lib: {
        files: [{
                expand: true,
                cwd: '<%= config.bower_components %>/jquery/dist',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/jquery'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/bootstrap/dist',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/bootstrap'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/bootstrap/dist',
                src: ['./fonts/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/font-awesome',
                src: ['./css/*.*', './fonts/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/font-awesome'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/cldrjs/dist',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/cldrjs'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/d3',
                src: ['./*.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/d3'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/jquery.easing',
                src: ['./js/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/jquery.easing'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/jqBootstrapValidation/dist',
                src: ['./*.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/jqBootstrapValidation'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/globalize/dist',
                src: ['./**/*.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/globalize'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/lato',
                src: ['./css/*.*', './font/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/lato'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/respond/dest',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/respond'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/html5shiv/dist',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/html5shiv'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/holderjs',
                src: ['./holder.js', './holder.min.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/holderjs'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/jquery_appear',
                src: ['./jquery.appear.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/jquery_appear'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/yamm3/yamm',
                src: ['./yamm.css'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/yamm3'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/jquery_appear',
                src: ['./jquery.appear.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/jquery_appear'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/animate.css',
                src: ['./*.css'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/animate.css'
            }, {
                expand: true,
                cwd: '<%= config.bower_components %>/snapjs',
                src: ['./*.css', './*.js'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/snapjs'
            }, {
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/less/less.min.js': ['node_modules/less/dist/less.min.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/less/less.js': ['node_modules/less/dist/less.js'],
                '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/<%= config.lib %>/classie/classie.js': ['<%= config.bower_components %>/classie/classie.js']
            }]
    },
    override: {
        files: [{
                expand: true,
                cwd: '<%= config.root %>',
                src: ['./**/*.*'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>'
            }]
	}
};
