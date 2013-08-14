function drawGraph(xhr, status, args) {
    var data = args.graphData;
    if (data != null) {
        var g = new Graph();
        var arr = JSON.parse(data);
        for (i = 0; i < arr.length; i++) {
            g.addNode(arr[i].id, {label: arr[i].label});
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