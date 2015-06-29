/*
 * SCROLL for Top-Menu
 */
var misfOnScroll = (function () {

    var docElem = document.documentElement,
            scrollToBottomElt = document.querySelector('.scroll-bottom'),
            scrollToTopElt = document.querySelector('.scroll-top'),
            didScroll = false,
            changeToBottomOn = 450,
            changeToTopOn = 450;

    function init() {
        window.addEventListener('scroll', function (event) {
            if (!didScroll) {
                didScroll = true;
                setTimeout(scrollPage, 250);
            }
        }, false);
    }

    function scrollPage() {
        var syTop = scrollYTop();
        var syBottom = scrollYBottom();
        if (syTop > changeToBottomOn) {
            classie.add(scrollToBottomElt, 'hidden');
        }
        else {
            classie.remove(scrollToBottomElt, 'hidden');
        }
        if (syBottom > changeToTopOn) {
            classie.add(scrollToTopElt, 'hidden');
        }
        else {
            classie.remove(scrollToTopElt, 'hidden');
        }
        didScroll = false;
    }

    function scrollYTop() {
        return (window.pageYOffset || docElem.scrollTop);
    }

    function scrollYBottom() {
        return -((window.pageYOffset || docElem.scrollTop) + (window.innerHeight || 0 ) - docElem.offsetHeight);
    }
    
    init();

})();