package com.gui.taptobuy.customadapter;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.gui.taptobuy.Entities.Product;
import com.gui.taptobuy.Entities.ProductForAuction;
import com.gui.taptobuy.Entities.ProductForAuctionInfo;
import com.gui.taptobuy.Entities.ProductForSale;
import com.gui.taptobuy.Entities.ProductForSaleInfo;
import com.gui.taptobuy.activity.BidProductInfoActivity;
import com.gui.taptobuy.activity.BuyItProductInfoActivity;
import com.gui.taptobuy.activity.MyHistoryActivity;
import com.gui.taptobuy.activity.MyHistoryActivity.MyViewHistory;
import com.gui.taptobuy.activity.SearchActivity.MyViewItem;

import com.gui.taptobuy.datatask.ImageManager;
import com.gui.taptobuy.datatask.Main;
import com.gui.taptobuy.phase1.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MyHistorySoldListCustomAdapter extends BaseAdapter implements OnClickListener {

	private MyHistoryActivity activity;
	private LayoutInflater layoutInflater;
	private ArrayList<Product> items;	

	public MyHistorySoldListCustomAdapter (MyHistoryActivity a, LayoutInflater l, ArrayList<Product> items)
	{
		this.activity = a;		
		this.layoutInflater = l;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View itemRow, ViewGroup parent) {
		MyViewHistory itemHolder;
		Product item = items.get(position);
		        	
			itemRow = layoutInflater.inflate(R.layout.myhistory_boughtprodrow, parent, false); 
			itemHolder = new MyViewHistory();
			itemHolder.itemPic =  (ImageView) itemRow.findViewById(R.id.myHist_soldProductPic);
			itemHolder.productName = (TextView) itemRow.findViewById(R.id.myHist_soldProdName);
			itemHolder.sellerUserName = (TextView) itemRow.findViewById(R.id.myHist_soldBuyersName);
			itemHolder.priceAndShiping = (TextView) itemRow.findViewById(R.id.myHist_soldPrice);	
			itemHolder.wonOr = (TextView)itemRow.findViewById(R.id.myHist_soldOrW);
			
			itemHolder.itemPic.setTag(itemHolder);
			itemRow.setTag(itemHolder);

			double shippingPrice = item.getShippingPrice();
			
			if(item instanceof ProductForAuction)
			{
				itemHolder.wonOr.setText("Won!");
				if(shippingPrice == 0){
					itemHolder.priceAndShiping.setText("$" + ((ProductForAuction) item).getCurrentBidPrice()+" (Free Shipping)");
				}
				else{
					itemHolder.priceAndShiping.setText("$" + ((ProductForAuction) item).getCurrentBidPrice()+" (Shipping: $" + shippingPrice + ")"); 
				}	
				
			}
			else{

				itemHolder.wonOr.setText("Purchased");
				if(shippingPrice == 0){
					itemHolder.priceAndShiping.setText("$" + ((ProductForSale) item).getInstantPrice() +" (Free Shipping)");
				}
				else{
					itemHolder.priceAndShiping.setText("$" + ((ProductForSale) item).getInstantPrice() +" (Shipping: $" + shippingPrice + ")"); 
				} 
		}
		
		itemRow.setOnClickListener(this);  

		itemHolder.item = item;
		itemHolder.productName.setText(item.getTitle());   		
		itemHolder.sellerUserName.setText(item.getSellerUsername());		
		itemHolder.sellerRating.setRating((float)item.getSellerRate());		
		itemHolder.itemPic.setImageBitmap(item.getImg());

		return itemRow;
	}

	@Override
	public void onClick(View v) {
		MyViewItem itemHolder = (MyViewItem) v.getTag();    
		new productInfoTask().execute(itemHolder.item.getId() + "");		
	}
	
	public void startBidProductInfoActivity(){
		this.activity.startActivity(new Intent(this.activity, BidProductInfoActivity.class));
	}


	private Product getProductInfo(String productId){
		HttpClient httpClient = new DefaultHttpClient();
		String productInfoDir = Main.hostName +"/product/" + productId;
		HttpGet get = new HttpGet(productInfoDir);
		get.setHeader("content-type", "application/json");
		Product theItem = null;
		try
		{
			HttpResponse resp = httpClient.execute(get);
			if(resp.getStatusLine().getStatusCode() == 200){
				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONObject json = new JSONObject(jsonString);
				JSONObject itemInfoJson = json.getJSONObject("productInfo");
				if(json.getBoolean("forBid")){
					theItem = new ProductForAuctionInfo(itemInfoJson.getInt("id"), itemInfoJson.getString("title"), itemInfoJson.getString("timeRemaining"), 
							itemInfoJson.getDouble("shippingPrice"), itemInfoJson.getString("imgLink"),  itemInfoJson.getString("sellerUsername"), 
							itemInfoJson.getDouble("sellerRate"),  itemInfoJson.getDouble("startinBidPrice"),  itemInfoJson.getDouble("currentBidPrice"),  itemInfoJson.getInt("totalBids"),
							itemInfoJson.getString("product"),itemInfoJson.getString("model"),itemInfoJson.getString("brand"),itemInfoJson.getString("dimensions"),itemInfoJson.getString("description"));
				}
				else{
					theItem = new ProductForSaleInfo(itemInfoJson.getInt("id"), itemInfoJson.getString("title"), itemInfoJson.getString("timeRemaining"), 
							itemInfoJson.getDouble("shippingPrice"), itemInfoJson.getString("imgLink"),  itemInfoJson.getString("sellerUsername"), 
							itemInfoJson.getDouble("sellerRate"), itemInfoJson.getInt("remainingQuantity"), itemInfoJson.getDouble("instantPrice"),
							itemInfoJson.getString("product"),itemInfoJson.getString("model"),itemInfoJson.getString("brand"),itemInfoJson.getString("dimensions"),itemInfoJson.getString("description"));
				}
			}
			else{
				Log.e("JSON","ProductInfo json could not be downloaded.");
			}
		}
		catch(Exception ex)
		{
			Log.e("Search","Error!", ex);
		}
		return theItem;
	}

	private class productInfoTask extends AsyncTask<String,Void,Product> {
		Product downloadedProductInfo;
		protected Product doInBackground(String... params) {
			return getProductInfo(params[0]);//get product info
		}
		protected void onPostExecute(Product productInfo ) {
			downloadedProductInfo = productInfo;
			//download image
			new DownloadImageTask().execute(productInfo.getImgLink());
		}			
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
			
			protected Bitmap doInBackground(String... params) {
				return ImageManager.downloadImage(params[0]);
			}
			protected void onPostExecute(Bitmap result) {
				downloadedProductInfo.setImg(result);
				if(downloadedProductInfo instanceof ProductForAuctionInfo){//for auction
					BidProductInfoActivity.showingProductInfo = (ProductForAuctionInfo) downloadedProductInfo;
					
					startBidProductInfoActivity();
				}
				else{//for sale
					BuyItProductInfoActivity.showingProductInfo = (ProductForSaleInfo) downloadedProductInfo;
					activity.startActivity(new Intent(activity, BuyItProductInfoActivity.class));
				}
			}
		}
	}
}
