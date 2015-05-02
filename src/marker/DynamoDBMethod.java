package marker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sqs.*;


import java.lang.Object;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import javax.servlet.http.HttpServlet;










import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import twitter4j.FilterQuery;
import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.handlers.RequestHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.util.StringUtils;

public class DynamoDBMethod {
	
	/**
	 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
	 * Usage: java twitter4j.examples.PrintSampleStream<br>
	 * </p>
	 *
	 * @author Yusuke Yamamoto - yusuke at mac.com
	 */
	//private static final Log logger = LogFactory.getLog(DynamoDBDynamicFaultInjection.class);
	static String tableName = "TweetStore";
	public static AmazonDynamoDBClient client;
	  public static DynamoDBMapper mapper;
	 public static String tweetID;
	 public static String time;
	 public static String user;
	 public static String tweetLocation;
	 public static String message;
	 public static String latitude;
	 public static String longitude;
	 public static Double latTemp;
	 public static Double lonTemp;
	 private static final Log logger = LogFactory.getLog(DynamoDBMethod.class);
	  
	  
	 //public static final Object lock = new Object();
	 
	    public static void main(String[] args) throws TwitterException, FileNotFoundException, IllegalArgumentException, IOException {
	 //public void doGet(HttpServletRequest request, HttpServlet Response){
	 
	    	//just fill this
	    	 ConfigurationBuilder cb = new ConfigurationBuilder();
	         cb.setDebugEnabled(true)
	           .setOAuthConsumerKey("75205spF4m0CLsJpUb3TxMure")
	           .setOAuthConsumerSecret("3ZqDd9habcEDrn0FHDrb7sKmJdandwtmitxniCxqZaHCMuUHtS")
	           .setOAuthAccessToken("338369841-mpTf9Kbpd5pJtKSsVimFpyUL26qk3q7kRPKex1nF")
	           .setOAuthAccessTokenSecret("eGDsDWtgyvw90UFrvRQmi2NjX2YLJwjT10rJs6xIYwLZ5");
	         
	         AWSCredentials credentials;
	         credentials = new PropertiesCredentials(new File("src/AwsCredentials.properties"));
	         final AmazonSQS sqs = new AmazonSQSClient(credentials);
	         Region usEast1 = Region.getRegion(Regions.US_EAST_1);
	         sqs.setRegion(usEast1);
	         
	         CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
	         final String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
	         
	        final DynamoDBMethod obj = new DynamoDBMethod();
	        final Object lock = new Object();
	        final TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	        //ArrayList<String> locations = new ArrayList<String>();
	        
	        try{
	   		obj.init(); // dynamo DB object initialization
	   		
	   		//obj.createNewTable(tableName);
	   		//obj.describeTable();
	 	
	         
	        final Map<String, Condition> scanFilter = new HashMap<String, Condition>();
	        final ArrayList<Status> statuses = new ArrayList<Status>();

	        StatusListener listener = new StatusListener() {
	           // @Override
	            public void onStatus(Status status) {
	            	//do{
	            	//while(statuses.size()<4)
	            	//{
	            	//if((status.getGeoLocation()!= null)&&((status.getUser().getLang().equalsIgnoreCase("en")
	                        //|| status.getUser().getLang().equalsIgnoreCase("en_US")))&&((status.getGeoLocation().toString().equalsIgnoreCase("San Jose"))||(status.getUser().getLocation().equalsIgnoreCase("Sao Paulo"))||(status.getUser().getLocation().equalsIgnoreCase("Singapore"))||(status.getUser().getLocation().equalsIgnoreCase("Sydney"))||(status.getUser().getLocation().equalsIgnoreCase("Tokyo"))||(status.getUser().getLocation().equalsIgnoreCase("Beijing"))||status.getUser().getLocation().equalsIgnoreCase("Mumbai") || status.getUser().getLocation().equalsIgnoreCase("New York")||(status.getUser().getLocation().equalsIgnoreCase("London"))||(status.getUser().getLocation().equalsIgnoreCase("Paris"))||(status.getUser().getLocation().equalsIgnoreCase("Mexico")))){
	            	
	            	if((status.getGeoLocation() != null)&&((status.getUser().getLang().equalsIgnoreCase("en"))||(status.getUser().getLang().equalsIgnoreCase("en_US")))){
	            	statuses.add(status);
	            	
	            	
	            	System.out.println(statuses.size());
	            	if(statuses.size()> 200)
	            	{
	            		//try {
	            		      synchronized (lock) {
	            		    	
	            				
									lock.notify();
									twitterStream.shutdown();
									/*System.out.println("a");
									ArrayList<String> locations = FetchDataToMap.scanResult("tweetID");
									System.out.println("b");
		            		    	for (String l: locations)
		            		    	{
		            		        System.out.println(l);
		            		    	}
		            		        
		            		       
		    	            		System.out.println("Sample Collected");
		    	            		lock.notify();
		    	            		twitterStream.shutdown();*/
								} 
	            		      System.out.println("unlocked");
								/*}catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
	            		    	
	    	            		
	            		      }*/
	            	
	            		   
	                      }
	                     // System.out.println("unlocked");
	            		
	            	
	            	
	                //System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
	            	try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            	User userobj = status.getUser();
	            	Long tweetIDtemp = status.getId();
	            	tweetID = Long.toString(tweetIDtemp);
	            	time = status.getCreatedAt().toString();
	            	user = status.getUser().getScreenName();
	            	tweetLocation = userobj.getLocation();
	            	
	            	
	            	GeoLocation locate=status.getGeoLocation();
	            	latTemp = locate.getLatitude();
	            	
	            	latitude = Double.toString(latTemp);
	            	
	            	lonTemp = locate.getLongitude();
	            	
	            	longitude = Double.toString(lonTemp);
	            	//System.out.println(locate);
	            	//String l = locate.toString();
	            	/*if(locate!=null)
	            	{
	            	latTemp = status.getGeoLocation().getLatitude();
	            	
	            	latitude = Double.toString(latTemp);
	            	
	            	lonTemp = status.getGeoLocation().getLongitude();
	            	
	            	longitude = Double.toString(lonTemp);
	            	}*/
	            	message = status.getText();
	            	try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	//obj.insert();
	            	/*Map<String, AttributeValue> item = newItem(tweetID,time, user, location, message);
	            	PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
	            	PutItemResult putItemResult = client.putItem(putItemRequest);*/
	            	
	            	obj.putItem(newItem(tweetID,time, user, latitude,longitude, message));
	            	
	            	sqs.sendMessage(new SendMessageRequest(myQueueUrl, message));

	            	// loadTable.setId(tweetID);
	            	//mapper.load(tweetID);

	            	// scanning the tweet location
	            	// FetchDataToMaps fm = new FetchDataToMaps();
	            	
	            	
	            	/*Condition condition = new Condition()
	            	.withComparisonOperator(ComparisonOperator.NOT_NULL)
	            	.withAttributeValueList(new AttributeValue());
	            	scanFilter.put("location", condition);
	            	ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
	            	ScanResult scanResult = client.scan(scanRequest);
	            	logger.info("Result: " + scanResult);
	            	//System.out.println(scanResult);*/
	            	
	            	//ArrayList<String> loc = FetchDataToMap.scanResult("tweetID");
	            	//System.out.println(FetchDataToMap.scanResult("tweetID"));
	            	//}
	            }
	            	
	            	
	            }    
	            

	            //@Override
	            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
	            }

	            //@Override
	            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
	            }

	            //@Override
	            public void onScrubGeo(long userId, long upToStatusId) {
	                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
	            }

	            //@Override
	            public void onStallWarning(StallWarning warning) {
	                //System.out.println("Got stall warning:" + warning);
	            }

	            //@Override
	            public void onException(Exception ex) {
	                ex.printStackTrace();
	            }
	        };
	        //FilterQuery fq = new FilterQuery();
	        //String keywords[] = {"apple"};
	        //fq.track(keywords);
	        
	        twitterStream.addListener(listener);
	        //twitterStream.filter(fq);
	        twitterStream.sample();
	        try {
	            synchronized (lock) {
	              lock.wait();
	            } }catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	              }
	        
            
            
            
	        System.out.println("Returning statuses");
	        
	        
	        ScanRequest scanRequest = new ScanRequest()
	        .withTableName(tableName)
	        .withProjectionExpression("latitude, longitude, message");

	        ScanResult result = client.scan(scanRequest);
	        
	        String lt, tempMsg, msg;
	        Double lt1, ln1;
			String ln;
	        for (Map<String, AttributeValue> item : result.getItems()){
	        	lt = item.get("latitude").toString();
	        	lt1 = Double.parseDouble(lt.substring(lt.indexOf(":")+2,lt.indexOf(",")));
	        	ln = item.get("longitude").toString();
	        	ln1= Double.parseDouble(ln.substring(ln.indexOf(":")+2,ln.indexOf(",")));
	        	
	        	//lt = Double.parseDouble(item.get("latitude").toString());
	        	//ln = Double.parseDouble(item.get("longitude").toString());
	        	
	        	tempMsg=item.get("message").toString();
	        	msg= tempMsg.substring(tempMsg.indexOf(":")+2,tempMsg.indexOf(",")).toLowerCase();
	        	
	        	//System.out.println(lt1+" "+ln1+" "+msg);
	        	
	        	
	        	/*locations.add(str.substring(19, str.length()-3));
	        	System.out.println(str.substring(19, str.length()-3));*/
	        	
	    }
	        /*System.out.println("Location value size:" + locations.size());
	        for(String l: locations)
	        {
	        	System.out.println(l.substring(19, l.length()-3));
	        }*/
	        //obj.retrieveItem();
	        
	       // twitterStream.shutdown();
	        
	       /* Condition condition = new Condition()
        	.withComparisonOperator(ComparisonOperator.NOT_NULL)
        	.withAttributeValueList(new AttributeValue());
        	scanFilter.put("location", condition);
        	ScanRequest scanRequest = new ScanRequest(tableName).withScanFilter(scanFilter);
        	ScanResult scanResult = client.scan(scanRequest);
        	logger.info("Result: " + scanResult);
        	
        	System.out.println("Result: " + scanResult);*/
	       /* locations = FetchDataToMap.scanResult("tweetID");
			System.out.println("b");
	    	for (String l: locations)
	    	{
	        System.out.println(l);
	    	}*/
	    	
	        
	        
	           
	        }catch (AmazonServiceException ase) {
	        	logger.error("Service Exception: " + ase);
	        	} catch (AmazonClientException ace) {
	        	logger.error("Client Exception: " + ace);
	        	}
	        
	        
	        //System.out.println(FetchDataToMap.scanResult("tweetID"));
	        	
	        //return locations;
  
	    }

	    
	    public void init() {
			  
			  
	    	PropertyConfigurator.configure("log4j.properties");
	    	 AWSCredentials credentials;
			try {
				credentials = new PropertiesCredentials(new File("src/AwsCredentials.properties"));
				client = new AmazonDynamoDBClient(new STSSessionCredentialsProvider(credentials));
		    	
		    	RequestHandler requestHandler = new FaultInjectionRequestHandler(client);
		    	client.addRequestHandler(requestHandler);
				 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*private void retrieveItem() {
	        try {
	            
	            HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
				key.put("tweetID", new AttributeValue());
				GetItemRequest getItemRequest = new GetItemRequest()
	                .withTableName(tableName)
	                .withKey(key)
	                .withProjectionExpression("tweetID, location, time, user,  message");
	            
	            GetItemResult result = client.getItem(getItemRequest);

	            // Check the response.
	            System.out.println("Printing item after retrieving it....");
	            printItem(result.getItem());            
	                        
	        }  catch (AmazonServiceException ase) {
	                    System.err.println("Failed to retrieve item in " + tableName);
	        }   

	    }*/
	    
	   /* private void printItem(Map<String, AttributeValue> attributeList) {
	        for (Map.Entry<String, AttributeValue> item : attributeList.entrySet()) {
	            String attributeName = item.getKey();
	            AttributeValue value = item.getValue();
	            System.out.println(attributeName +" "+(value.getS() == null ? "" : "S=[" + value.getS() + "]"));
	        }
	    }*/
	 

	    	
		//to create a new dynamodb table
		private void createNewTable(String tableName) {
			 try {
				 List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
				 attributeDefinitions.add(new AttributeDefinition().withAttributeName("tweetID").withAttributeType("S"));
				 List<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
				 ks.add(new KeySchemaElement().withAttributeName("tweetID").withKeyType(KeyType.HASH));
				 ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
				 .withReadCapacityUnits(10L)
				 .withWriteCapacityUnits(10L);
				 CreateTableRequest request = new CreateTableRequest()
				 .withTableName(tableName)
				 .withAttributeDefinitions(attributeDefinitions)
				 .withKeySchema(ks)
				 .withProvisionedThroughput(provisionedThroughput);
				 try {
				 CreateTableResult createdTableDescription = client.createTable(request);
				logger.info("Created Table: " + createdTableDescription);
				 // Wait for it to become active
				 waitForTableToBecomeAvailable(tableName);
				 } catch (ResourceInUseException e) {
				 logger.warn(e);
				 }
				  } catch (AmazonServiceException e) {
				   e.printStackTrace();
				  } catch (AmazonClientException e) {
				   e.printStackTrace();
				  }
				 }
		//method which waits till the DynamoDB Table gets created
		private void waitForTableToBecomeAvailable(String tableName) {
				  System.out.println("Waiting for " + tableName + " to become ACTIVE...");
			    long startTime = System.currentTimeMillis();
				  long endTime = startTime + (10 * 60 * 1000);
				  while (System.currentTimeMillis() < endTime) {
				   try {
				    Thread.sleep(1000 * 20);
				   } catch (Exception e) {
				   }
				   try {
				    DescribeTableRequest request = new DescribeTableRequest()
				      .withTableName(tableName);
				    TableDescription tableDescription = client.describeTable(
				      request).getTable();
				    String tableStatus = tableDescription.getTableStatus();
				    System.out.println("  - current state: " + tableStatus);
				    if (tableStatus.equals(TableStatus.ACTIVE.toString()))
				     return;
				   } catch (AmazonServiceException ase) {
				    if (ase.getErrorCode().equalsIgnoreCase(
				      "ResourceNotFoundException") == false)
				     throw ase;
				   }
				  }
				  throw new RuntimeException("Table " + tableName + " never went active");
				  
				 }
		
		private void putItem(Map<String, AttributeValue> item) {
			try {
			PutItemRequest putItemRequest = new PutItemRequest(tableName, item);
			PutItemResult putItemResult = client.putItem(putItemRequest);
			logger.info("Result: " + putItemResult);
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
			//if(location!="")
			//{
		    /*if(!StringUtils.isNullOrEmpty(location))
		    {
			item.put("tweetLocation", new AttributeValue(location));
		    }
		    else
		    {
		    item.put("tweetLocation", new AttributeValue("Columbus"));
		    }*/
			//}
		    
			item.put("latitude", new AttributeValue(lat));
		    
		    
			item.put("longitude", new AttributeValue(lng));
		    
			//}
			//}
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
		 
		 /*private static void getItem(String keyVal) {
			 Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			 key.put("tweetID", new AttributeValue(keyVal));
			 GetItemRequest getItemRequest = new GetItemRequest()
			 .withTableName(tableName)
			 .withKey(key);
			 GetItemResult item = client.getItem(getItemRequest);
			 logger.info("Get Result: " + item);
			 }*/
		/*public void insert()
		{
			tableCreate item = new tableCreate();
			  	  item.setId(tweetID);
				  item.setUser(user);
				  item.setTime(time);
				  item.setLocation(location);
				  item.setMessage(message);
				  mapper.save(item);
		}*/
		 private void describeTable() {
			 DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(tableName);
			 TableDescription tableDescription = client.describeTable(describeTableRequest).getTable();
			 logger.info("Table Description: " + tableDescription);
			 //System.out.println(tableDescription);
			 }
	    


}
