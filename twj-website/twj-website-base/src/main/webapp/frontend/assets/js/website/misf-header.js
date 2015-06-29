/**
 * cbpAnimatedHeader.js v1.0.0
 * http://www.codrops.com
 *
 * Licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * Copyright 2013, Codrops
 * http://www.codrops.com
 */
/**
 * Updated for Microsfair needs
 * TODO : Suggest an enhancement to add 2 images for the logo (avoid resizing in the browser!)
 */
var cbpAnimatedHeader = (function () {

    var docElem = document.documentElement,
            body = document.querySelector('body'),
            header = document.querySelector('.navbar-top-panel'),
            logoImg = document.querySelector('.navbar-logo-img'),
            didScroll = false,
            changeHeaderOn = 450;

    function init() {
        // First calculation
        scrollPage();
        
        window.addEventListener('scroll', function (event) {
            if (!didScroll) {
                didScroll = true;
               setTimeout(scrollPage, 250);
            }
        }, false);
        
        window.addEventListener('resize', function (event) {
            if (!didScroll) {
                didScroll = true;
                setTimeout(scrollPage, 250);
            }
        }, false);
    }

    function scrollPage() {
        var sy = scrollY();
        if (sy >= changeHeaderOn || header.clientWidth < 768) {
            logoImg.src = 'assets/img/logo.png';
            classie.add(header, 'navbar-shrink');
            if(header.clientWidth < 768)
            {
                $('body').css("padding-top", "50px");
            }
        } else {
            logoImg.src = 'assets/img/logo-big.png';
            classie.remove(header, 'navbar-shrink');
            if(header.clientWidth >= 768)
            {
                $('body').css("padding-top", "100px");
            }
        }
        didScroll = false;
    }

    function scrollY() {
        return window.pageYOffset || docElem.scrollTop;
    }

    init();

})();
