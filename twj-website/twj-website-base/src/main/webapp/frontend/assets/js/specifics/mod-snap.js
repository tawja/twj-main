/* 
 * Copyright 2015 Tawja.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Helper
/*
 $ = function (id) {
 return document.getElementById(id);
 };
 */

// Instance
var snapper = new Snap({
    element: document.getElementById('content'),
    maxPosition: 200,
    minPosition: -200,
    tapToClose: true
});
// 
UpdateDrawers = function () {
    var state = snapper.state(),
            towards = state.info.towards,
            opening = state.info.opening;
    if (opening == 'right' && towards == 'left') {
        document.getElementById('right-drawer').classList.add('active-drawer');
        document.getElementById('left-drawer').classList.remove('active-drawer');
    } else if (opening == 'left' && towards == 'right') {
        document.getElementById('right-drawer').classList.remove('active-drawer');
        document.getElementById('left-drawer').classList.add('active-drawer');
    }
};

snapper.on('drag', UpdateDrawers);
snapper.on('animating', UpdateDrawers);
snapper.on('animated', UpdateDrawers);

document.getElementById('toggle-left').addEventListener('click', function () {
    var content = $('#content');
    if (content.css('left') !== '0px')
    {
        if (snapper.state().state === "left") {
            content.css({"left":"0px"});
            snapper.close();
        } else {
            snapper.open('left');
        }
    } else {
        content.css({"left":"50px"});
    }
});

document.getElementById('toggle-right').addEventListener('click', function () {
    var content = $('#content');
    if (content.css('right') !== '0px')
    {
        if (snapper.state().state === "right") {
            content.css({"right":"0px"});
            snapper.close();
        } else {
            snapper.open('right');
        }
    } else {
        content.css({"right":"50px"});
    }
});

/*
 var snapper = new Snap({
 element: document.getElementById('content'),
 maxPosition: 200,
 minPosition: -200
 });
 */
