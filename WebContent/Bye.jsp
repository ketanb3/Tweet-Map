<%@page import="marker.LocationExtract"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
   <%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps</title>
    <h1 align="center" style="font-family:Times New Roman;color:black;font-size:40px;"><b><i>Tweet Map </i></b></h1>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
    

<script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/tags/markerclustererplus/2.0.12/src/markerclusterer_packed.js" /></script>
  </head>
  <body>
    
    <div id="map" style="width:1350px;height:500px;"></div>
    <div id="messages"></div>
    <script type="text/javascript">
    //<![CDATA[
    
    // delay between geocode requests - at the time of writing, 100 miliseconds seems to work well
    var delay = 100;
      // ====== Create map objects ======
      var infowindow = new google.maps.InfoWindow();
      var latlng = new google.maps.LatLng(0, 150.644);
      var mapOptions = {
        zoom: 2,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      
      };
      var geo = new google.maps.Geocoder(); 
      var map = new google.maps.Map(document.getElementById("map"), mapOptions);
      // var markerCluster = new MarkerClusterer(map);
      var bounds = new google.maps.LatLngBounds();
      // ====== Geocoding ======
      function getAddress(search, next, arrLat, arrLong) {
        geo.geocode({address:search}, function (results,status)
          { 
            // If that was successful
            if (status == google.maps.GeocoderStatus.OK) {
              // Lets assume that the first marker is the one we want
              var p = results[0].geometry.location;
              var lat=p.lat();
              var lng=p.lng();
              // Output the data
                //var msg = 'address="' + search + '" lat=' +lat+ ' lng=' +lng+ '(delay='+delay+'ms)<br>';
                //document.getElementById("messages").innerHTML += msg;
              // Create a marker
              createMarker(search,lat,lng);
            }
            // ====== Decode the error status ======
            else {
              // === if we were sending the requests to fast, try this one again and increase the delay
              if (status == google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
                nextAddress--;
                delay++;
              } else {
                var reason="Code "+status;
                //var msg = 'address="' + search + '" error=' +reason+ '(delay='+delay+'ms)<br>';
                //document.getElementById("messages").innerHTML += msg;
              }   
            }
            next();
          }
        );
      }
     // ======= Function to create a marker
     function createMarker(add,lat,lng) {
       var contentString = add;
       var marker = new google.maps.Marker({
         position: new google.maps.LatLng(lat,lng),
         map: map,
         icon: 'http://wiki.alumni.net/wiki/images/thumb/5/55/Wikimap-blue-dot.png/50px-Wikimap-blue-dot.png',
         zIndex: Math.round(latlng.lat()*-100000)<<5
       });
       
      google.maps.event.addListener(marker, 'click', function() {
         infowindow.setContent(contentString); 
         infowindow.open(map,marker);
         
       });
       bounds.extend(marker.position);
       
     }
      // ======= Global variable to remind us what to do next
      var nextAddress = 0;
      // ======= Function to call the next Geocode operation when the reply comes back
      function theNext() {
    	  //alert(1);
    	  <% List<String> list = new ArrayList<String>();
        	//  list = (ArrayList<String>) request.getAttribute("list");
     			list = LocationExtract.LatiLng;
     			//List<String> list2 = new ArrayList<String>();
     			//list2 = LocationExtract.tweetContent;
     			%>
     			 var markers = [], longLatArr = [];
     			 
     	          <%for(int i=0;i<list.size();i++){%>
     	              longLatArr.push("<%= list.get(i)%>");
     	             // alert(longLatArr[i]);
     	          <%}%>
     	          //alert(2);
     	        
     	          
     	         
     	         //alert(3);
     	          for (var j=0;j<longLatArr.length;j++)
     	        	  {
     	        	    var str = longLatArr[j];
     	        	    var res= str.split(",");
     	        	   	var con = str;
     	        	   	//alert(4);
     	        	    //alert(res);
     	        	    //mrk.push(new google.maps.LatLng(parseFloat(res[0]),parseFloat(res[1])));
     	        	   var marker = new google.maps.Marker({
     	        	         position: new google.maps.LatLng(parseFloat(res[0]),parseFloat(res[1])),
     	        	         map: map,
     	        	         title: con,
     	        	         //icon: 'http://wiki.alumni.net/wiki/images/thumb/5/55/Wikimap-blue-dot.png/50px-Wikimap-blue-dot.png',
     	        	         zIndex: Math.round(latlng.lat()*-100000)<<5
     	        	       });
     	        	       
     	        	      
     	        	      /* var infowindow = new google.maps.InfoWindow({
     	        	    	    content: con,
     	        	    	}); */
     	        	      google.maps.event.addListener(marker, 'click', function(marker) {
     	        	         infowindow.setContent(con); 
     	        	         infowindow.open(map,marker);
     	        	         
     	        	       });
     	        	  
     	        	       bounds.extend(marker.position);
     	        	       markers.push(marker);
     	        	      
     	        	  }
     	         var markerCluster = new MarkerClusterer(map, markers, {
     	            gridSize:40,
     	            minimumClusterSize: 4,
     	            calculator: function(markers, numStyles) {
     	    	    // Custom style can be returned here
     	                return {
     	                    text: markers.length,
     	                    index: numStyles
     	                };
     	            }
     	        });

     	       /* markerCluster.addMarkers(mrk,true); */
     	        // var markerCluster = new MarkerClusterer(map, mrk);
       /* if (nextAddress < jsArray.length) {
          setTimeout('getAddress("'+jsArray[nextAddress]+'",theNext)', delay);
          nextAddress++;
        } else {
          // We're done. Show map bounds
          map.fitBounds(bounds);
          
        }*/
        //map = new google.maps.Map(document.getElementById('map'),mapOptions);
		/*var pointArray = new google.maps.MVCArray(mrk);
		heatmap = new google.maps.visualization.HeatmapLayer({
			data: pointArray
			});
		heatmap.setMap(map);*/
        // var markerCluster = new MarkerClusterer(map);
      }
      // ======= Call that function for the first time =======
      theNext();
    // This Javascript is based on code provided by the
    // Community Church Javascript Team
    // http://www.bisphamchurch.org.uk/   
    // http://econym.org.uk/gmap/
    //]]>
    
    
   </script>
   
   <a href="HeatMap.jsp">Click to see the HeatMap</a>
   
  
</html>