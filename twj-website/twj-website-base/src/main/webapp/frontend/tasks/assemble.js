module.exports = {
    // Task-level options
    options: {
        middleware: ['assemble-middleware-sitemap', 'other/middleware/*'],
        sitemap: {
            homepage: "<%=configSite.homepage %><%= (mavenProperties.webdir != '' ? ('/' + mavenProperties.webdir) : '') %>",
            relativedest: true,
            exclude: ['**/test-*.html', '**/sample-*.html', '**/demo-*.html']
        },
        flatten: true,
        postprocess: require('pretty'),
        assets: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/<%= config.assets %>',
        data: '<%= config.data %>',
        //partials: ['<%= config.partials %>', '<%= config.layoutdir %>/**/*hbs'],
        partials: ['<%= config.partials %>', '<%= config.articles %>/articles-toc.hbs'],
        helpers: ['<%= config.helpers %>', 'handlebars-helper-analytics'],
        layoutdir: '<%= config.layoutdir %>',
        layout: 'layout-default',
        layoutext: '.hbs',
        analytics: '<%= config.analytics %>',
        ads: '<%= config.ads %>',
        marked: {
            breaks: false,
            gfm: true,
            langPrefix: 'language-',
            pedantic: false,
            sanitize: false,
            silent: false,
            smartLists: false,
            smartypants: false,
            tables: true
        },
        config: '<%= config %>',
        configSite: '<%= configSite %>',
        mavenProperties: '<%= mavenProperties %>'
    },
    mainInc: {
        options: {
            layout: 'layout-default-for-inclusion'
        },
        files: [{
                expand: true,
                cwd: '<%= config.pages %>',
                src: ['*.hbs'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>',
                ext: '.inc.html'
            }]
    },
    main: {
        options: {
            layout: 'layout-default',
            plugins: ['assemble-middleware-sitemap']
        },
        files: [{
                expand: true,
                cwd: '<%= config.pages %>',
                src: ['*.hbs'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>'
            }]
    },
    articlesInc: {
        options: {
            layout: 'layout-markdown-for-inclusion'
        },
        files: [{
                expand: true,
                cwd: '<%= config.articles %>',
                engine: 'handlebars',
                src: ['*.md'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>',
                ext: '.inc.html'
            }]
    },
    articles: {
        options: {
            layout: 'layout-markdown',
            //plugins: ['assemble-middleware-sitemap'],
            sitemap: {
                homepage: "<%=configSite.homepage %><%= (mavenProperties.webdir != '' ? ('/' + mavenProperties.webdir) : '') %>",
                relativedest: true,
                exclude: ['**/test-*.html', '**/sample-*.html', '**/demo-*.html'],
                robot: false
                // TODO : Need the plugin to be updated to set the sitemap.xml alternatif name to avoid overidding main one...
                //dest: "<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>/sitemap-articles"
            },
        },
        files: [{
                expand: true,
                cwd: '<%= config.articles %>',
                engine: 'handlebars',
                src: ['*.md'],
                dest: '<%= gruntMavenProperties.targetPath %>/<%= config.webapp %>'
            }]
    }
};
