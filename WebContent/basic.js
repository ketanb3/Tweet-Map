/**
 * 
 */

$(document).ready(function(){

    var mapCanvas = document.getElementById("map_canvas");

    $.ajax('info', {
    	url: "http://localhost:8085/MapMarker/marker/LocationExtract.java",
        type:"GET",
        dataType: "json",
        success: function(list){
        	var markers =[];
            if(list.length)
            {
            	
                for (var i = 0; i < list.length; i++) 
                {
                    var latLng = new google.maps.LatLng(list[i].latitude,list[i].longitude);
                    var marker = new google.maps.Marker({
                        position: latLng,
                        map: map
                    });
                    markers.push(marker);
                }    
            }

            var mapOptions = {
                    zoom: 2,
                    center: new google.maps.LatLng(list[0].latitude,list[0].longitude),
                    mapTypeId: google.maps.MapTypeId.TERRAIN
                    };

            var map = new google.maps.Map(mapCanvas, mapOptions); 
            var markerCluster = new MarkerClusterer(map, markers);
        }

    });

 });