module.exports = {
  dist: { // Target
    options: { // Target options
      removeComments: true,
      collapseWhitespace: true,
      keepClosingSlash: true,
      minifyJS: true,
      minifyCSS: true
    },
    files: [{
      expand: true,
      cwd: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>',
      src: '**/*.html',
      dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/'
    }]
  }
};
