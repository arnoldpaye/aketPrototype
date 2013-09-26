function drawGraph(xhr, status, args) {
    var data = args.graphData;
    if (data != null) {
        var render = function(r, n) {
            var set = r.set().push(
                    r.rect(n.point[0] - 30, n.point[1] - 13, 60, 44).attr({"fill": "#feb", r: "12px", "stroke-width": n.distance == 0 ? "3px" : "1px"})).push(
                    r.text(n.point[0], n.point[1] + 10, (n.label || n.id) + "\n(" + (n.distance == undefined ? "Infinity" : n.distance) + ")"));
        };
        var g = new Graph();
        var arr = JSON.parse(data);
        for (i = 0; i < arr.length; i++) {
            //g.addNode(arr[i].id, {label: arr[i].label});
            g.addNode(arr[i].label);
        }
        for (i = 0; i < arr.length; i++) {
            for (j = 0; j < arr[i].nodeList.length; j++) {
                g.addEdge(arr[i].label, arr[i].nodeList[j].label);
            }
        }

//        g.addEdge("C", "A");

        var layouter = new Graph.Layout.Spring(g);
        layouter.layout();

        var renderer = new Graph.Renderer.Raphael('canvas', g, 500, 1000);
        renderer.draw();
    }
}