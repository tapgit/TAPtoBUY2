package com.gui.taptobuy.datatask;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gui.taptobuy.Entities.Rating;

public class RatingsManager {
	
//	ArrayList<Rating> ratingsList;
//
//	private ArrayList<Rating> getRatingsList(String productId){
//		HttpClient httpClient = new DefaultHttpClient();
//		String bidListDir = Main.hostName +"/ratingslist/" + productId;////
//		HttpGet get = new HttpGet(bidListDir);
//		get.setHeader("content-type", "application/json");
//		try
//		{
//			HttpResponse resp = httpClient.execute(get);
//			if(resp.getStatusLine().getStatusCode() == 200){
//				String jsonString = EntityUtils.toString(resp.getEntity());
//				JSONArray bidListArray = (new JSONObject(jsonString)).getJSONArray("ratingslist");
//				ratingsList = new ArrayList<Rating>();
//
//				JSONObject bidListElement = null;
//
//				for(int i=0; i<bidListArray.length();i++){
//					bidListElement = bidListArray.getJSONObject(i);
//					bidList.add(new Bid(-1, bidListElement.getDouble("amount"), -1, bidListElement.getString("username")));
//				}
//
//			}
//			else{
//				Log.e("JSON","bidlist json could not be downloaded.");
//			}
//		}
//		catch(Exception ex)
//		{
//			Log.e("BidList","Error!", ex);
//		}
//		return bidList;
//	}
//
//	private class getBidListTask extends AsyncTask<String,Void,ArrayList<Bid>> {
//		protected ArrayList<Bid> doInBackground(String... productId) {
//			return getBidList(productId[0]);//get bidlist de bids puestos a este product
//		}
//		protected void onPostExecute(ArrayList<Bid> bidList ) {
//			//llenar con array de bid
//			bidListView.setAdapter(new MySellingBidListCustomAdapter(activity,layoutInflater, bidList));
//		}			
//	}
}
