module.exports = {
    grunt: {
        options: {
            jshintrc: './.jshintrc'
        },
        src: [
            'Gruntfile.js',
            '<%= config.tasks %>/**/*.js',
            '<%= config.helpers %>'
        ]
    },
    main: {
        options: {
            jshintrc: '<%= config.assets %>/js/.jshintrc'
        },
        src: [
            '<%= config.assets %>/js/**/*.js'
        ]
    }
};
