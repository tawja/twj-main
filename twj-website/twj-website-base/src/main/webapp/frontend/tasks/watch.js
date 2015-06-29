module.exports = {
    maven: {
        files: ['<%= gruntMavenProperties.filesToWatch %>'],
        tasks: ['sync:maven']
    },     
    mavenGeneratedDesign: {
        files: ['<%= gruntMavenProperties.targetPath %>/generated-design'],
        tasks: ['sync:maven']
    },
    assemble: {
        files: ['<%= config.templates %>/**/*.{md,hbs,yml}', '<%= config.pages %>/*.{md,hbs}', '<%= config.articles %>/*.{md,hbs}'],
        tasks: ['assemble']
    },
    less: {
        files: ['<%= config.assets %>/less/**/*.less'],
        tasks: ['less']
    },
    script: {
        files: ['<%= config.assets %>/js/**/*.js'],
        tasks: ['concat', 'uglify']
    },
    grunt: {
        files: ['Gruntfile.js', '<%= config.tasks %>/*.js', '*.{json,yml}'],
        tasks: ['default']
    },
    readme: {
        files: ['README.md'],
        tasks: ['sync:maven', 'assemble']
    },
    livereload: {
        options: {
            livereload: '<%= connect.options.livereload %>'
        },
        files: [
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/{,*/}*.html',
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/{,*/}*.less',
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/{,*/}*.css',
            '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>/{,*/}*.js'
        ]
    }
};
