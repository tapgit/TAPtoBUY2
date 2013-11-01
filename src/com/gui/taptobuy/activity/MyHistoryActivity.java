package com.gui.taptobuy.activity;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gui.taptobuy.Entities.Product;
import com.gui.taptobuy.Entities.ProductForAuction;
import com.gui.taptobuy.Entities.ProductForSale;

import com.gui.taptobuy.customadapter.BiddingsCustomListAdapter;
import com.gui.taptobuy.customadapter.MyHistoryBoughtListCustomAdapter;
import com.gui.taptobuy.customadapter.SearchResultsCustomListAdapter;
import com.gui.taptobuy.datatask.ImageManager;
import com.gui.taptobuy.datatask.Main;
import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyHistoryActivity extends Activity{
	public  ArrayList<Product> historyBoughtItems;
	public  ArrayList<Product> historySoldItems;
	private ListView boughtItemsList;
	private ListView soldItemsList;
	private LayoutInflater layoutInflator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_myhistory);
		this.layoutInflator = LayoutInflater.from(this);
		this.boughtItemsList = (ListView) findViewById(R.id.myHistory_boughtItems);

		historyBoughtItems = new ArrayList<Product>();
		historySoldItems = new ArrayList<Product>();

		//itemsList.invalidateViews();		
		//itemsList.setAdapter(new MyHistoryBoughtListCustomAdapter(this,this.layoutInflator, historyBoughtItems));
		this.soldItemsList = (ListView) findViewById(R.id.myHistory_SoldItems);
		//itemsList.invalidateViews();		
		//itemsList.setAdapter(new MyHistoryBoughtListCustomAdapter(this,this.layoutInflator, historySoldItems));
//		Toast.makeText(MyHistoryActivity.this, historyBoughtItems.size() + " "+ historySoldItems.size() + "", Toast.LENGTH_LONG).show();
		new myHistoryTask().execute();

	}

	public static class MyViewHistory {
		public TextView productName, sellerUserName, priceAndShiping,bidsAmount,wonOr,buyerUserN;
		public RatingBar sellerRating;		
		public ImageView itemPic;
		public Product item;		

	}
	private void getMyHistoryItems(){
		HttpClient httpClient = new DefaultHttpClient();
		String myHistoryDir = Main.hostName +"/myhistory/" + Main.userId;
		HttpGet get = new HttpGet(myHistoryDir);
		get.setHeader("content-type", "application/json");
		try
		{
			HttpResponse resp = httpClient.execute(get);
			if(resp.getStatusLine().getStatusCode() == 200){
				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONArray searchResultArray = (new JSONObject(jsonString)).getJSONArray("myHistory");

				historyBoughtItems = new ArrayList<Product>();
				historySoldItems = new ArrayList<Product>();

				JSONObject searchElement = null;
				JSONObject jsonItem = null;
				Product anItem = null;

				for(int i=0; i<searchResultArray.length();i++){
					searchElement = searchResultArray.getJSONObject(i);
					jsonItem = searchElement.getJSONObject("item");
					if(searchElement.getBoolean("forBid")){
						anItem = new ProductForAuction(jsonItem.getInt("id"), jsonItem.getString("title"), jsonItem.getString("timeRemaining"), 
								jsonItem.getDouble("shippingPrice"), jsonItem.getString("imgLink"),  jsonItem.getString("sellerUsername"), 
								jsonItem.getDouble("sellerRate"),  jsonItem.getDouble("startinBidPrice"),  jsonItem.getDouble("currentBidPrice"),  jsonItem.getInt("totalBids"));
					}
					else{
						anItem = new ProductForSale(jsonItem.getInt("id"), jsonItem.getString("title"), jsonItem.getString("timeRemaining"), 
								jsonItem.getDouble("shippingPrice"), jsonItem.getString("imgLink"),  jsonItem.getString("sellerUsername"), 
								jsonItem.getDouble("sellerRate"), jsonItem.getInt("remainingQuantity"), jsonItem.getDouble("instantPrice"));
					}
					if(searchElement.getBoolean("sold")){
						historySoldItems.add(anItem);
					}
					else{
						historyBoughtItems.add(anItem);
					}
				}

			}
			else{
				Log.e("JSON","search json could not be downloaded.");
			}
		}
		catch(Exception ex)
		{
			Log.e("Search","Error!", ex);
		}
	}

	private class myHistoryTask extends AsyncTask<Void,Void,ArrayList<Product>> {
		public  int downloadadImagesIndexBought = 0;
		public  int downloadadImagesIndexSold = 0;
		protected ArrayList<Product> doInBackground(Void... params) {
			getMyHistoryItems();//get search result
			return null;//
		}
		protected void onPostExecute ( ArrayList<Product> unused) {
			//download images
			for(Product itm: historyBoughtItems){
				new DownloadImageBoughtItemTask().execute(itm.getImgLink());
			}
			for(Product itm: historySoldItems){
				new DownloadImageSoldItemTask().execute(itm.getImgLink());
			}
			boughtItemsList.setAdapter(new MyHistoryBoughtListCustomAdapter(MyHistoryActivity.this,layoutInflator, historyBoughtItems));	
			soldItemsList.setAdapter(new MyHistoryBoughtListCustomAdapter(MyHistoryActivity.this,layoutInflator, historySoldItems));
		}
		private class DownloadImageBoughtItemTask extends AsyncTask<String, Void, Bitmap> {

			protected Bitmap doInBackground(String... urls) {
				return ImageManager.downloadImage(urls[0]);
			}
			protected void onPostExecute(Bitmap result) {
				boughtItemsList.invalidateViews();
				historyBoughtItems.get(downloadadImagesIndexBought++).setImg(result);
			}
		}
		private class DownloadImageSoldItemTask extends AsyncTask<String, Void, Bitmap> {
			protected Bitmap doInBackground(String... urls) {
				return ImageManager.downloadImage(urls[0]);
			}
			protected void onPostExecute(Bitmap result) {
				soldItemsList.invalidateViews();
				historySoldItems.get(downloadadImagesIndexSold++).setImg(result);
			}
		}
	}

}
