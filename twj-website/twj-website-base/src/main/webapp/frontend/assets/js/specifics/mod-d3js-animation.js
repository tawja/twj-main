var vv = window,
        w = 640,
        h = 256;

var svg = d3.select("#svgContainer")
        .append("svg")
        .attr("width", w)
        .attr("height", h)
        .attr("style", "margin: 0 auto; display:block;");

var width = w,
        height = h,
        padding = 1, // separation between same-color nodes
        clusterPadding = 6, // separation between different-color nodes
        maxRadius = 8;
nodeId = 0;

var incScene = 0, maxIncScene = 360;

var n = 140, // total number of nodes
        m = 8;// number of distinct clusters

//d3.scale.category10(1) = "#ff0000";	
var color = d3.scale.category10().domain(d3.range(m));

color.range(["#cc0000", "#999999", "#66ff99", "#ccff66", "#ffcc66", "#ff66cc", "#cc66ff", "#3399ff"]);

// The largest node for each cluster.
var clusters = new Array(m);

var nodes = d3.range(n).map(function () {

    var maxRFactor = 2;
    var newCluster = Math.floor(nodeId % m);

    if (nodeId >= m && newCluster === 0) {
        nodeId++;
        newCluster = Math.floor(nodeId % m);
    }

    var i = Math.floor(Math.random() * m),
            r = Math.sqrt((i + 1) / m * Math.max(maxRFactor * 0.001, -Math.log(Math.random()))) * maxRadius,
            newName = null;

    if (nodeId < m)
    {

        if (newCluster === 1) {
            newName = "Vous";
        }
        if (newCluster === 2) {
            newName = "Ressources";
        }
        if (newCluster === 3) {
            newName = "Partenaires";
        }
        if (newCluster === 4) {
            newName = "Clients";
        }
        if (newCluster === 5) {
            newName = "Fournisseurs";
        }
        if (newCluster === 6) {
            newName = "Sponsors";
        }
        if (newCluster === 7) {
            newName = "Participants";
        }

        r = maxRFactor * maxRadius;

        if (newCluster === 0) {
            newName = "Environnement";
            r = 10;
        }
    }

    d = {
        cluster: newCluster,
        radius: r,
        x: Math.cos(newCluster / m * 2 * Math.PI) * 200 + width / 2 + Math.random(),
        y: Math.sin(newCluster / m * 2 * Math.PI) * 200 + height / 2 + Math.random(),
        name: newName,
        id: nodeId
    };
    if (!clusters[newCluster] || (r > clusters[newCluster].radius))
    {
        clusters[newCluster] = d;
    }

    nodeId++;


    return d;
});


/*
 var svg = d3.select("body").append("svg")
 .attr("width", width)
 .attr("height", height);
 */
// Create the groups under svg
var gnodes = svg.selectAll('g.gnode')
        .data(nodes)
        .enter()
        .append('g')
        .classed('gnode', true);


var node = gnodes.append("circle")
        .attr("class", "node")
        //.attr("r", 5)
        .style("fill", function (d) {
            return color(d.cluster);
        })
        .style("stroke", "#333333")
        .style("stroke-width", function (d) {
            return d.radius / 3.5;
        });



// filters go in defs element
var defs = svg.append("defs");

// create filter with id #drop-shadow
// height=130% so that the shadow is not clipped
var filter = defs.append("filter")
        .attr("id", "drop-shadow")
        .attr("height", "130%");

// SourceAlpha refers to opacity of graphic that this filter will be applied to
// convolve that with a Gaussian with standard deviation 3 and store result
// in blur
filter.append("feGaussianBlur")
        .attr("in", "SourceAlpha")
        .attr("stdDeviation", 3)
        .attr("result", "blur");

// translate output of Gaussian blur to the right and downwards with 2px
// store result in offsetBlur
filter.append("feOffset")
        .attr("in", "blur")
        .attr("dx", 0)
        .attr("dy", 0)
        .attr("result", "offsetBlur");

// overlay original SourceGraphic over translated blurred opacity by using
// feMerge filter. Order of specifying inputs is important!
var feMerge = filter.append("feMerge");

feMerge.append("feMergeNode")
        .attr("in", "offsetBlur");
feMerge.append("feMergeNode")
        .attr("in", "SourceGraphic");



// Append the labels to each group
var labels = gnodes.append("text")
        .text(function (d) {
            return d.name;
        })
        .attr("dominant-baseline", "top")
        .attr("text-anchor", "middle")
        .attr("font-family", "sans-serif")
        .attr("font-size", "16")
        .style("fill", "#ffffff")
        .style("font-weight", "bold")
        .style("filter", "url(#drop-shadow)");

labels.filter(function (d) {
    return d.name === null;
}).remove();

gnodes.sort(function (a, b) { // select the parent and sort the path's
    if (a.name === null || a.name > d.name)
        return -1;  // a is not the hovered element, send "a" to the back
    else
        return 1;   // a is the hovered element, bring "a" to the front
});

var root = node[0][0];
root.radius = 10;
root.fixed = true;

/*
 svg.on("mousemove", function() {
 var p1 = d3.mouse(this);
 root.px = p1[0];
 root.py = p1[1];
 force.resume();
 });
 */


node.transition()
        .duration(750)
        .delay(function (d, i) {
            return i * 5;
        })
        .attrTween("r", function (d) {
            var i = d3.interpolate(0, d.radius);
            return function (t) {
                return d.radius = i(t);
            };
        })
        ;


var force =
        d3.layout.force()
        .nodes(nodes)
        .size([width, height])
        .gravity(0.05)
        .charge(function (d, i) {
            return i ? 0 : -200;
        })
        //.charge(0)
        //.friction(0.5)
        .on("tick", tick);

node.call(force.drag);

force.start();

function tick(e) {
    gnodes
            .each(cluster(10 * e.alpha * e.alpha))
            .each(collide(0.5))
            .attr("cx", function (d) {
                return d.x;
            })
            .attr("cy", function (d) {
                return d.y;
            });

    // Update the links
    /*
     link.attr("x1", function(d) { return d.source.x; })
     .attr("y1", function(d) { return d.source.y; })
     .attr("x2", function(d) { return d.target.x; })
     .attr("y2", function(d) { return d.target.y; });
     */
    // Translate the groups

    gnodes.attr("transform", function (d) {
        return 'translate(' + [d.x, d.y] + ')';
    });
    //root.x =  Math.cos(e.alpha * 2 * Math.PI) * 200 + width / 2 + Math.random();
    //root.y =  Math.sin(e.alpha * 2 * Math.PI) * 200 + height / 2 + Math.random();
    //root.x = xPos + Math.cos(10 * e.alpha * e.alpha * Math.PI) * 256 + width / 2 + Math.random();
    //root.y =  yPos + Math.sin(10 * e.alpha * e.alpha * Math.PI) * 256 + height / 2 + Math.random()
}


// Move root randomly.
function move(alpha) {
    return function (d) {
        var cluster = clusters[d.cluster];
        if (cluster === d)
            return;
        var x = d.x - cluster.x,
                y = d.y - cluster.y,
                l = Math.sqrt(x * x + y * y),
                r = d.radius + cluster.radius;
        if (l !== r) {
            l = (l - r) / l * alpha;
            d.x -= x *= l;
            d.y -= y *= l;
            cluster.x += x;
            cluster.y += y;
        }
    };
}

// Move d to be adjacent to the cluster node.
function cluster(alpha) {
    return function (d) {

        var cluster = clusters[d.cluster];

        var x = 0,
                y = 0,
                l = 0,
                r = 0;

        if (d.cluster === 0)
        {
            incScene++;
            //incScene = Math.floor(incScene % maxIncScene);


            if (incScene > maxIncScene)
            {
                force.stop();
                return;
            }

            x = width / 2 * Math.cos(incScene);
            y = height / 2 * Math.sin(incScene);
            l = Math.sqrt(x * x + y * y);
            r = d.radius + 0;

            if (l !== r) {
                l = (l - r) / l * 0.1;
                d.x -= x *= l;
                d.y -= y *= l;
            }
            var diam = 200, speed = 0.047;
            d.x = width / 2 + diam * Math.cos(incScene * speed);
            d.y = height / 2 + diam * Math.cos(incScene * speed) * Math.sin(incScene * speed);
            //x: Math.cos(newCluster / m * 2 * Math.PI) * 200 + width / 2 + Math.random(),
            //y: Math.sin(newCluster / m * 2 * Math.PI) * 200 + height / 2 + Math.random(),

            //var x = d.x - width / 2,
            //	y = d.y - height / 2,
            //	l = Math.sqrt(x * x + y * y),
            //	r = d.radius + 0;
            //if (l != r) {
            //  l = (l - r) / l * alpha;
            //  d.x -= x *= l;
            //  d.y -= y *= l;
            //}	

        }

        //if (cluster === d && (d.cluster == 0 || d.cluster == 1))
        //if (d.cluster == 0 || d.cluster == 1)
        if (d.cluster === 1)
        {
            x = d.x - width / 2;
            y = d.y - height / 2;
            l = Math.sqrt(x * x + y * y);
            r = d.radius + 0;

            if (l !== r) {
                l = (l - r) / l * alpha;
                d.x -= x *= l;
                d.y -= y *= l;
            }
        }

        if (cluster === d)
            return;

        x = d.x - cluster.x;
        y = d.y - cluster.y;
        l = Math.sqrt(x * x + y * y);
        r = d.radius + cluster.radius;

        if (l !== r) {
            l = (l - r) / l * alpha;
            d.x -= x *= l;
            d.y -= y *= l;
            cluster.x += x;
            cluster.y += y;
        }

        // Center

        //if (root == d) return;
        /*
         if (root.d == d)
         {
         var x = d.x - width / 2,
         y = d.y - height / 2,
         l = Math.sqrt(x * x + y * y),
         r = d.radius + 0;
         if (l != r) {
         l = (l - r) / l * alpha;
         d.x -= x *= l;
         d.y -= y *= l;
         }	
         }
         */
    };
}

// Resolves collisions between d and all other circles.
function collide(alpha) {
    var quadtree = d3.geom.quadtree(nodes);
    return function (d) {
        var r = d.radius + maxRadius + Math.max(padding, clusterPadding),
                nx1 = d.x - r,
                nx2 = d.x + r,
                ny1 = d.y - r,
                ny2 = d.y + r;
        quadtree.visit(function (quad, x1, y1, x2, y2) {
            if (quad.point && (quad.point !== d)) {
                var x = d.x - quad.point.x,
                        y = d.y - quad.point.y,
                        l = Math.sqrt(x * x + y * y),
                        r = d.radius + quad.point.radius + (d.cluster === quad.point.cluster ? padding : clusterPadding);
                if (l < r) {
                    l = (l - r) / l * alpha;
                    d.x -= x *= l;
                    d.y -= y *= l;
                    quad.point.x += x;
                    quad.point.y += y;
                }
            }
            return x1 > nx2 || x2 < nx1 || y1 > ny2 || y2 < ny1;
        });
    };
}
