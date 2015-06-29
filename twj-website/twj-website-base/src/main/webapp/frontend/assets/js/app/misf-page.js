// jQuery for page scrolling feature - Requires jQuery Easing plugin
/*
 $(function () {
 $('.page-scroll a').bind('click', function (event) {
 var $anchor = $(this);
 $('html, body').stop().animate({
 scrollTop: $($anchor.attr('href')).offset().top
 }, 1500, 'easeInOutExpo');
 event.preventDefault();
 });
 });
 */

// Support for smooth scrolling
// (simplified version, taken from http://stackoverflow.com/a/14805098/1173184)
$(window).load(function () {
    $('a[href^="#"]:not([href^="#tab_"]):not([href^="#carousel"]):not([data-toggle="dropdown"])').on('click', function (e) {

        // prevent default anchor click behavior
        e.preventDefault();

        // store hash
        var hash = this.hash;

        // animate
        $("html,body,#content").animate({
            scrollTop: $(this.hash).offset().top
        }, 300, function () {

            // when done, add hash to url
            // (default click behaviour)
            window.location.hash = hash;
        });
    });
});

// Highlight the top nav as scrolling occurs
$("#content").scrollspy({
    target: '.navbar-top-panel'
});

// Highlight the top nav as scrolling occurs
/*
$("#content").affix({
    target: '#toc-bar'
});
*/

// Closes the Responsive Menu on Menu Item Click
$('.top-navbar-collapse.navbar-collapse ul li a').click(function () {
    $('.top-navbar-toggle.navbar-toggle:visible').click();
});

$('.toc-navbar-collapse.navbar-collapse ul li a').click(function () {
    $('.toc-navbar-toggle.navbar-toggle:visible').click();
});

// Activate fixed menu
/*
$(function () {

    $('#toc-bar').affix({
        offset: {
            //top: $('#toc-bar').offset().top
            target: $('header').offset().top + $('header').height() + 100
        }
    });
});
*/

$(document).ready(function(){

    $('[data-toggle="tooltip"]').tooltip();   

});

