package marker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.util.StringUtils;
import marker.FaultInjectionRequestHandler;

public class DynamoDBInsert {
	
	static String tableName = "TweetStore";
	public static AmazonDynamoDBClient client;
	
	
	public void putItem(Map<String, AttributeValue> item) {
		try {
		PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
		PutItemResult putItemResult = client.putItem(putItemRequest);
		//logger.info("Result: " + putItemResult);
		} catch (Exception e) {
				e.printStackTrace();		// TODO: handle exception
		}
		}
	
	//insert tweet fields into the Dynamo DB table
	 static Map<String, AttributeValue> newItem(String tweetID, String time, String user, String lat, String lng,  String message) {
		
		 Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		 try{
	    if(!StringUtils.isNullOrEmpty(tweetID)) 
	    {
		item.put("tweetID", new AttributeValue(tweetID));
	    }
	    else
	    {
	    item.put("tweetID", new AttributeValue(" "));
	    }
	    if(!StringUtils.isNullOrEmpty(time)) 
	    {
		item.put("time", new AttributeValue(time));
	    }
	    else
	    {
	    item.put("time", new AttributeValue(" "));
	    }
	    if(!StringUtils.isNullOrEmpty(user))
	    {
		item.put("user", new AttributeValue(user));
	    }
	    else
	    {
	    item.put("user", new AttributeValue(" "));
	    }
		
		item.put("latitude", new AttributeValue(lat));
		item.put("longitude", new AttributeValue(lng));
	    
	    if(!StringUtils.isNullOrEmpty(message))
	    {
		item.put("message", new AttributeValue(message));
	    }
	    else
	    {
	    item.put("message", new AttributeValue(" "));
	    }
		
		} catch(Exception e){
			e.printStackTrace();
		}
		return item;
		}
	 

}
