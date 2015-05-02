
package marker;

import marker.DynamoDBInsert;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONValue;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
//import com.cloud.tweetExtract.TweetFeedExtract;
import com.google.gson.Gson;

/**
 * Servlet implementation class LocationExtract
 */
public class LocationExtract extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static String latitude ;
	public static String longitude ;
	public static String message ;
	public static DynamoDBMapper mapper;
	 public static String tweetID;
	 public static String time;
	 public static String user;
	 public static String tweetLocation;
	 List<Double[]> locations = new ArrayList<Double[]>(); 
	 
	 public static Double latTemp;
	 public static Double lonTemp;
	 private static final Log logger = LogFactory.getLog(LocationExtract.class);
	 public static List<String> LatiLng = new ArrayList<String>();
	 public static List<String> tweetContent = new ArrayList<String>();
	 public static List<String> tweets = new ArrayList<String>();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationExtract() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException{
	      // Do required initialization
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		 AWSCredentials credentials;
		 credentials = new PropertiesCredentials(new File("src/AwsCredentials.properties"));
		 
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		 String tableName = "TweetStore";
		 String srch = request.getParameter("keyword");
		 
		 AmazonSQS sqs = new AmazonSQSClient(credentials);
	        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
	        sqs.setRegion(usEast1);
		 
		 //Gson jsob = new Gson();
			//HashMap<String,Object> Lat = new HashMap<String,Object>();
			//HashMap<String,Object> Lng = new HashMap<String,Object>();
			//HashMap<String,Object> keyStore = new HashMap<String,Object>();
		 
		 JSONArray jArr = new JSONArray();
		 ArrayList<String> Lats = new ArrayList<String>();
		 ArrayList<String> Longs = new ArrayList<String>();
		 ArrayList<String> Mesgs = new ArrayList<String>();
		 JSONObject json = new JSONObject();
     	JSONObject jsob = new JSONObject();
		//ServletOutputStream out=response.getOutputStream();
		//out.print(locations);
		 
		ScanRequest scanRequest = new ScanRequest()
        .withTableName(tableName)
        .withProjectionExpression("latitude, longitude, message");
        
		
        ScanResult result = client.scan(scanRequest);
        
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest("https://sqs.us-east-1.amazonaws.com/251151665387/MyQueue");
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        
        
        
        //Map<String, Object> list = new HashMap<String, Object>();
        String  tempMsg, msg, lt, ln;
        String dlt, dln;
        for (Map<String, AttributeValue> item : result.getItems()){
        	
        	
        	lt = item.get("latitude").toString();
        	dlt = lt.substring(lt.indexOf(":")+2,lt.indexOf(","));
        	ln = item.get("longitude").toString();
        	dln= ln.substring(ln.indexOf(":")+2,ln.indexOf(","));
        	tempMsg=item.get("message").toString();
        	msg= tempMsg.substring(tempMsg.indexOf(":")+2,tempMsg.indexOf(",")).toLowerCase().replace("'", "&#039").replace("\"", "&#034").replaceAll("\\p{Cntrl}", "").trim();;
        	if(msg.contains(srch))
        	{
        		Lats.add(dlt);
        		Longs.add(dln);
        		Mesgs.add(msg);
        	
        	}
        	
    			
    			
        	
    }
        try {
			json.put("Lat", Lats);
			json.put("Lng", Longs);
			json.put("Msg", Mesgs);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       request.setAttribute("sendObj", json);
       request.getRequestDispatcher("test.jsp").forward(request, response);
        
       
	}	
	
	public void destroy(){
		// do nothing.
		}


	
	
	

}
