<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta charset="utf-8">
    <title>Heatmaps</title>
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
      #panel {
        position: absolute;
        top: 5px;
        left: 50%;
        margin-left: -180px;
        z-index: 5;
        background-color: #fff;
        padding: 5px;
        border: 1px solid #999;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=visualization"></script>
    <script>
// Adding 500 Data Points
var map, pointarray, heatmap;

var taxiData = [
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(40.712784,-74.005941),
  new google.maps.LatLng(51.507351,-0.127758),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(37.786956, -122.440279),
  new google.maps.LatLng(19.075984,72.877656),
  new google.maps.LatLng(19.075984,72.877656),
  new google.maps.LatLng(19.075984,72.877656),
  new google.maps.LatLng(19.075984,72.877656),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(23.634501,-102.552784),
  new google.maps.LatLng(-33.867487,151.206990),
  new google.maps.LatLng(-33.867487,151.206990),
  new google.maps.LatLng(35.689487,139.691706),
  new google.maps.LatLng(35.689487,139.691706),
  new google.maps.LatLng(35.689487,139.691706),
  new google.maps.LatLng(1.352083,103.819836),
  new google.maps.LatLng(1.352083,103.819836),
  new google.maps.LatLng(1.352083,103.819836),
  new google.maps.LatLng(-23.550520,-46.633309)
  
 ];

function initialize() {
  var mapOptions = {
    zoom: 13,
    center: new google.maps.LatLng(37.774546, -122.433523),
    mapTypeId: google.maps.MapTypeId.SATELLITE
  };

  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

  var pointArray = new google.maps.MVCArray(taxiData);

  heatmap = new google.maps.visualization.HeatmapLayer({
    data: pointArray
  });

  heatmap.setMap(map);
}

function toggleHeatmap() {
  heatmap.setMap(heatmap.getMap() ? null : map);
}

function changeGradient() {
  var gradient = [
    'rgba(0, 255, 255, 0)',
    'rgba(0, 255, 255, 1)',
    'rgba(0, 191, 255, 1)',
    'rgba(0, 127, 255, 1)',
    'rgba(0, 63, 255, 1)',
    'rgba(0, 0, 255, 1)',
    'rgba(0, 0, 223, 1)',
    'rgba(0, 0, 191, 1)',
    'rgba(0, 0, 159, 1)',
    'rgba(0, 0, 127, 1)',
    'rgba(63, 0, 91, 1)',
    'rgba(127, 0, 63, 1)',
    'rgba(191, 0, 31, 1)',
    'rgba(255, 0, 0, 1)'
  ]
  heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
}

function changeRadius() {
  heatmap.set('radius', heatmap.get('radius') ? null : 20);
}

function changeOpacity() {
  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>

  <body>
    <div id="panel">
      <button onclick="toggleHeatmap()">Toggle Heatmap</button>
      <button onclick="changeGradient()">Change gradient</button>
      <button onclick="changeRadius()">Change radius</button>
      <button onclick="changeOpacity()">Change opacity</button>
    </div>
    <div id="map-canvas"></div>
  </body>
</html>