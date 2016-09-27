package Parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.resto.cuisinier.MainActivity;

import Model.Orderline;


import android.os.Handler;
import android.util.Log;

public class OrderLineParser {

	static InputStream stream = null;
	 private String urlString = null;
	 private Handler hand;
	 public volatile boolean parsingComplete = true;
	 public String output;
	 public String name;
	 public ArrayList<Orderline> orders=new ArrayList<Orderline>();
	 public int status;
	 private int tableId;
	 public String flash;
	 public Orderline orderline;
	 public OrderLineParser(int id ,Handler hand){
	      this.urlString = MainActivity.url;
	      this.tableId = id;
	      this.hand = hand;
	      Log.i("url", HttpClientSingleton.url+urlString+"/"+tableId);
	      
	   }
	 public void readAndParseJSON(String in) {
		 Log.i("in", in);
	      try {
	    	  if(status==200)
	    	  {
	    		JSONObject billObject=null;
	    		JSONObject reader = new JSONObject(in);
	    	  	JSONArray billArray =new JSONArray();
	    	  	billArray=reader.getJSONArray("products");
	    	  	Log.i("billtablesize", ""+billArray.length());
	    	 
	    	  	for(int i=0;i<billArray.length();i++)
	    	  	{ 	
	    	  		orderline=new Orderline();
	    	  		billObject=billArray.getJSONObject(i);
	    	  		orderline.setCategoryId(billObject.getInt("categoryId"));
	    	  		orderline.setCategoryName(billObject.getString("categoryName"));
	    	  		orderline.setOrderLine(billObject.getInt("orderLine"));
	    	  		orderline.setProductId(billObject.getInt("productId"));
	    	  		orderline.setProductName(billObject.getString("productName"));
	    	  		orderline.setProductPrice(billObject.getLong("productPrice"));
	    	  		orderline.setTva(billObject.getLong("tva"));
	    	  		orderline.setServed(billObject.getInt("served"));
	    	  		Log.i("serve", ""+orderline.getServed());
	    	  		if(orderline.getServed()<3)
	    	  			orders.add(orderline);
	    	  		
  	  		}
	    	  }
	    	  else
	    	  {
	    		  flash=output;
	    	  }	    	  
	         parsingComplete = false;
	         


	        } catch (Exception e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	        }
	      Log.i("ffffffffff","eeeeeeeeeeeeee");
	      
	   }
	 
	   public void fetchJSON(final String token){
		   Log.i("tokentable", token);
	      Thread thread = new Thread(new Runnable(){
	         @Override
	         public void run() {
	         try {
	        	HttpClient httpClient =HttpClientSingleton.getInstance();
	        	HttpGet httpGet= new HttpGet(HttpClientSingleton.url+urlString+"/"+tableId);
	        	
	        	httpGet.setHeader("Accept", token);
	        	HttpResponse httpResponse = httpClient.execute(httpGet);
	        	
	            StatusLine statusLine = httpResponse.getStatusLine();
	            status = statusLine.getStatusCode();
	            Log.i("statut", ""+status);
				HttpEntity httpEntity = httpResponse.getEntity();
				stream = httpEntity.getContent();
				
	      String data = convertStreamToString(stream);
	      output=data;
	      Log.i("data", data);
	      readAndParseJSON(data);
	         stream.close();
	         hand.sendEmptyMessage(0);

	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         }
	      });

	       thread.start(); 		
	   }
	   public void setServed(final String token,final int i)
	   {
		   Thread thread = new Thread(new Runnable(){
		         @Override
		         public void run() {
		         try {
		        	 List<NameValuePair> state = new ArrayList<NameValuePair>() ;
		        	HttpClient httpClient =HttpClientSingleton.getInstance();
		        	HttpPut httpPut= new HttpPut(HttpClientSingleton.url+"orderline/"+tableId);
		        	state.add(new BasicNameValuePair("state", ""+i));
		        	httpPut.setHeader("Accept", token);
		        	httpPut.setEntity(new UrlEncodedFormEntity(state));
		        	HttpResponse httpResponse = httpClient.execute(httpPut);
		        	
		            StatusLine statusLine = httpResponse.getStatusLine();
		            Log.i("stats", ""+statusLine);
		            hand.sendEmptyMessage(0);
		         } catch (Exception e) {
		            e.printStackTrace();
		         }
		         }
		      });
		   
		       thread.start(); 
		       
	   }
	   static String convertStreamToString(java.io.InputStream is) {
	      java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	      return s.hasNext() ? s.next() : "";
	   }
}
