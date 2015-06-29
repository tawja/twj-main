/* global module:true */
/* global require:true */
module.exports = function (grunt) {

    grunt.initConfig({

      clean: {
        assets: {
          src: "output/"
        },
        tmp: {
          src: "tmp"
        }

      },

      bower: {
        install: {
          options: {
            copy: false
          }
        }
      },

      webfont_svg_extractor: {
        test_glyphicon: {
          options: {
            fontPath: "bower_components/bootstrap/dist/fonts/glyphicons-halflings-regular.svg",
            cssPath: "bower_components/bootstrap/dist/css/bootstrap.css",
            outputDir: "../target/svg-temp-icons/",
            preset: "glyphicon",
			outFilePrefix: "gi-",
          }
        },
        test_fontawesome: {
          options: {
            fontPath: "bower_components/font-awesome/fonts/fontawesome-webfont.svg",
            cssPath: "bower_components/font-awesome/css/font-awesome.css",
            outputDir: "../target/svg-temp-icons/",
            preset: "fontawesome",
			outFilePrefix: "fa-",
          }
        },
        test_elusive: {
          options: {
            fontPath: "bower_components/elusive-iconfont/fonts/Elusive-Icons.svg",
            cssPath: "bower_components/elusive-iconfont/css/elusive-webfont.css",
            outputDir: "../target/svg-temp-icons/",
            translate: "width",
            regexp: /.el-icon-(.*):before/,
			outFilePrefix: "el-",
          }
        },
        test_ionicons: {
          options: {
            fontPath: "bower_components/ionicons/fonts/ionicons.svg",
            cssPath: "bower_components/ionicons/css/ionicons.css",
            outputDir: "../target/svg-temp-icons/",
            translate: "width",
            regexp: /ion-(.*):before/,
			outFilePrefix: "io-",
          }
        }
	},

      webfont: {
        icons: {
          src: 'icons/**/*.svg',
          dest: '../target/generated-design/icons/fonts',
          destCss: '../target/generated-design/icons/css',
          options: {
			types: 'eot,woff,ttf',
            template: 'templates/webfonts/tawja.css',
			font: 'tawja-icons',
			hashes: false,
            templateOptions: {
              baseClass: 'twj',
              classPrefix: 'twj-',
              mixinPrefix: 'twj-'
            },
            relativeFontPath: '',
            htmlDemo: true,
			htmlDemoTemplate: 'templates/webfonts/demo.html',
			destHtml: '../target/generated-design/icons',
            engine: 'node',
			fontHeight: 1152
          }
        }
      }, 

    svg2png: {
        all: {
            files: [
                { cwd: '../target/svg-temp-icons/', src: ['**/*.svg'], dest: '../target/png-temp-icons/' },
                { cwd: 'icons/', src: ['**/*.svg'], dest: '../target/png-icons/' }
            ]
        }
    },

cssmin: {
  target: {
    files: [{
      expand: true,
      cwd: '../target/generated-design/icons/css',
      src: ['*.css', '!*.min.css'],
      dest: '../target/generated-design/icons/css',
      ext: '.min.css'
    }]
  }
},

    });

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-webfont-svg-extractor');
    grunt.loadNpmTasks('grunt-webfont');
	grunt.loadNpmTasks('grunt-svg2png');
	grunt.loadNpmTasks('grunt-contrib-cssmin');

    grunt.registerTask('twj-tsk-webfont', [
        'webfont',
		'cssmin'
    ]);

    // TO call to get svg from existing fonts, and then check/update.integrate them
    grunt.registerTask('twj-tsk-webfont-extract', [
        'webfont_svg_extractor',
        'twj-tsk-webfont'
    ]);

    grunt.registerTask('default', [
        'twj-tsk-webfont'
    ]);
};
