<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="marker.LocationExtract" %>
<%@page import="java.util.*" %>
<html>
  <head>
    <title>Tweet Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
       height:500px;
       width:1350px;
      }
      #panel {
        position: absolute;
        top: 5px;
        left: 50%;'[-a]'
        margin-left: -180px;
        z-index: 5;
        background-color: #fff;
        padding: 5px;
        border: 1px solid #999;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
    <!--   <script src="basic.js"></script> -->
   
    <script>

var map;
var geocoder;

//ArrayList<String> list=(ArrayList<String>)request.getAttribute("TweetFeedExtract");

/*function codeAddress(){
	
	
		
	var address = l;
	geocoder.geocode({'address':address}, function(results, status){
		if (status ==google.maps.GeocoderStatus.OK){
			map.setCenter(result[0].geometry.location);
			var marker= new google.maps.Marker({
				map: map,
				position:results[0].geometry.location
			});
		}else{
			alert("Geocode was not successful for the following reason: " + status);
		}
			
	});
		
		
	
}*/



function initialize() {

geocoder = new google.maps.Geocoder();
  var mapOptions = {
    zoom: 8,
    center: new google.maps.LatLng(37.774546, -122.433523)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
}


/*$(document).ready(function() {
	$.get('LocationExtract', function(jsob){
		$.each(jsob,function(key, value){
			var Lat = value.Lat;
			var Lng = value.Lng;
			var myLatLng = new google.maps.LatLng(Lat, Lng);
			var map = new google.maps.Map(document.getElementById('map-canvas'),
                    mapOptions);
			var beachMarker = new google.maps.Marker({
			      position: myLatLng,
			      map: map,
			  });

		});
		
	});
});*/
  /*window.setInterval(function (){
	  heatmap = null;
	  var keywords = 'apple';
	  $.get('marker/LocationExtract', {
	  keyword : keywords
	  }, function(responseText) {
	  $.each(responseText, function(key, val) {
	  var data = JSON.stringify(val);
	  var data_s = data.substr(1, data.length-2);
	  var data_spl = data_s.split(',');
	  var lat = data_spl[0];
	  var lon = data_spl[1];
	  var taxiData = [
	  new google.maps.LatLng(lat, lon)];
	  var pointArray = new google.maps.MVCArray(taxiData);
	  heatmap = new google.maps.visualization.HeatmapLayer({
	  data: pointArray
	  });
	  heatmap.setMap(map);
	  });
	  });
	  }, 3000);
	  }*/
  //var loc[];
  /*var loc = request.getAttribute("locations");
  
  var i;
  var markers= [];
    for (i=0; i< loc.length; i++){
    	var location = loc[i];
    	var latlng= new google.maps.LatLng(location.latitude, location.longitude);
    	var marker= new google.maps.Marker({
    		position: latlng
    	});
    	markers.push(marker);
    }
    var markerCluster = new MarkerCluster(map, markers);*/
    	
    
    

//setMarkers();

//setMarkers(srch);
  






/*function toggleHeatmap() {
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
	  heatmap.set('radius', heatmap.get('radius') ? null : 50);
	}

	function changeOpacity() {
	  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.2);
	}


function setMarkers() {
	/*int k;
    for(k=0; k=list.length(); k++) 
    {
   geocoder.geocode( { 'address': s}, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
       // map.setCenter(results[0].geometry.location);
      var marker = new google.maps.Marker({
        position: results[0].geometry.location,
        map: map,
        title:'Tweet'
    });
	}
	else{alert('Geocode was not successful for the following reason: ' + status);
	}
	
	});
   }*/
  
   
   
   

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body onload="initialize()">
  
   
  <form id="TweetFeedExtract" method="post" action="LocationExtract">
  <div id="heading">
  	<h1 align=left><font style="Lucida Console" size=14>Tweet Map</font></h1> <br>
  	<hr>
  	</div><br><br>
  	<div id="panel">
  	Keyword:
	<input align="left" type="text" name="keyword">
  	<input type="submit" value="Collect Tweets" id="tweets"/>
    </div>
    <div id="map-canvas"></div>
    </form>
  </body>
</html>