<!DOCTYPE html>
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
    
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=visualization"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.0.min.js"></script>
    
    <script>
    
    var taxiData=[];
    var map, pointarray, heatmap,kw;
    
    function getKeywordTweet(srch){
    	var mapOptions = {
    		    zoom: 3,
    		    center: new google.maps.LatLng(37.774546, -122.433523),
    		    mapTypeId: google.maps.MapTypeId.SATELLITE
    		  };
    		  $.getJSON( '<%= request.getContextPath() %>' + "LocationExtract?srch=".concat(srch.substring(1)), function(json){
    			  var Lat = json.Lat;
    			  var Lng = json.Lng;
    				taxiData =[];
    			  for (var i = 0; i < Lat.length; i++) {
    				    var strLat = Lat[i];
    				    var strLng = Lng[i];
    				    //var res = str.split(" "); 
    				    taxiData.push(new google.maps.LatLng(strLat,strLng));
    				}
    				map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
    				var pointArray = new google.maps.MVCArray(taxiData);
    				heatmap = new google.maps.visualization.HeatmapLayer({
    					data: pointArray
    					});
    				heatmap.setMap(map);
    			});
    }
    
    $( document ).ready(function() {
    	$( "#keyword" ).change(function() {
    		
    		var srch= $( "#keyword" ).val();
    	
    		getKeywordTweet(srch);
    		});
    	});
    

function initialize() {
	geocoder = new google.maps.Geocoder();
	alert(1);
	var mapOptions = {
    zoom: 3,
    center: new google.maps.LatLng(37.774546, -122.433523),
    mapTypeId: google.maps.MapTypeId.SATELLITE
  };
	
	map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
	$.getJSON('<%= request.getContextPath() %>' + "LocationExtract?srch=", function(json){
		  var Lat = json.Lat;
		  var Lng = json.Lng;
			kw = json.keyword;
			//var dropdown = document.getElementById("keyword");
			
		   /* for (var i=0; i < kw.length;i++){    
		    	var optn = document.createElement("OPTION");
			    optn.text = kw[i];
			    optn.value = kw[i];
			    dropdown.options.add(optn);
		    }*/
			
			
	    	
		  for (var i = 0; i < Lat.length; i++) {
			    var strLat = Lat[i];
			    alert(strLat);
			    var strLng = Lng[i];
			    //var res = str.split(" "); 
			    taxiData.push(new google.maps.LatLng(strLat, strLng));
			}
			map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
			var pointArray = new google.maps.MVCArray(taxiData);
			heatmap = new google.maps.visualization.HeatmapLayer({
				data: pointArray
				});
			heatmap.setMap(map);
		});
	
  
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
  ];
  heatmap.set('gradient', heatmap.get('gradient') ? null : gradient);
}

function changeRadius() {
  heatmap.set('radius', heatmap.get('radius') ? null : 50);
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
      Keyword:<br>
	<input type="text" name="keyword">
  	<input type="submit" value="Collect Tweets" id="tweets"/>
    	

    </div>
    <div id="map-canvas"></div>
  </body>
</html>