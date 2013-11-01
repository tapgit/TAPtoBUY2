package com.gui.taptobuy.customadapter;

import java.util.ArrayList;

import com.gui.taptobuy.Entities.Product;
import com.gui.taptobuy.Entities.ProductForAuction;
import com.gui.taptobuy.Entities.ProductForSale;

import com.gui.taptobuy.activity.AccountSettingsActivity;
import com.gui.taptobuy.activity.BidsActivity;
import com.gui.taptobuy.activity.CartActivity;
import com.gui.taptobuy.activity.MySellingActivity;
import com.gui.taptobuy.activity.SearchActivity.MyViewItem;
import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MySellingListCustomAdapter extends BaseAdapter implements OnClickListener {
	private MySellingActivity activity;
	private LayoutInflater layoutInflater;
	private ArrayList<Product> items;	
	
	public MySellingListCustomAdapter (MySellingActivity a, LayoutInflater l, ArrayList<Product> products)
    {
    	this.activity = a;    	
    	this.layoutInflater = l;
    	this.items = products;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View itemRow, ViewGroup parent) {
		
		Product item = items.get(position);
		MyViewItem itemHolder;
		
		if(activity instanceof MySellingActivity){
			if(item instanceof ProductForAuction)
			{        	
				itemRow = layoutInflater.inflate(R.layout.selling_bidproductrow, parent, false); 
				itemHolder = new MyViewItem();
				itemHolder.itemPic =  (ImageView) itemRow.findViewById(R.id.mySell_BidProductPic);
				itemHolder.productName = (TextView) itemRow.findViewById(R.id.mySell_BidProdName);					
				itemHolder.priceAndShiping = (TextView) itemRow.findViewById(R.id.mySell_BidPrice);					
				itemHolder.timeRemaining = (TextView) itemRow.findViewById(R.id.mySell_BidRemaningTime);
				itemHolder.bidsAmount = (TextView) itemRow.findViewById(R.id.mySell_bids);
				itemHolder.bidListB = (Button) itemRow.findViewById(R.id.mySell_BidList);
				itemHolder.AcceptBid = (Button) itemRow.findViewById(R.id.mySell_AcceptBidB);
				itemHolder.Quit = (Button) itemRow.findViewById(R.id.mySell_QuitB);					
				
				itemHolder.itemPic.setTag(itemHolder);
				itemRow.setTag(itemHolder);
				
				itemHolder.bidListB.setOnClickListener(this);
				itemHolder.AcceptBid.setOnClickListener(this);
				itemHolder.Quit.setOnClickListener(this);
				itemRow.setOnClickListener(this);
				itemHolder.bidsAmount.setText(((ProductForAuction) item).getTotalBids()+" bids");
				
				double shippingPrice = item.getShippingPrice();
				if(shippingPrice == 0){
					itemHolder.priceAndShiping.setText("$" + ((ProductForAuction) item).getCurrentBidPrice()+" (Free Shipping)");
				}
				else{
					itemHolder.priceAndShiping.setText("$" + ((ProductForAuction) item).getCurrentBidPrice()+" (Shipping: $" + shippingPrice + ")"); 
				}
			}
			else //for sale
			{	        
				itemRow = layoutInflater.inflate(R.layout.selling_buyitproductrow, parent, false); 
				itemHolder = new MyViewItem();
				itemHolder.itemPic =  (ImageView) itemRow.findViewById(R.id.mySell_BuyNProductPic);
				itemHolder.productName = (TextView) itemRow.findViewById(R.id.mySell_BuyNProdName);					
				itemHolder.priceAndShiping = (TextView) itemRow.findViewById(R.id.mySell_BuyNPrice);					
				itemHolder.timeRemaining = (TextView) itemRow.findViewById(R.id.mySell_BuyNRemaningTime);
				itemHolder.Quit = (Button) itemRow.findViewById(R.id.mySell_QuitfromSellingB);					
				
				itemHolder.itemPic.setTag(itemHolder);
				itemRow.setTag(itemHolder);
				itemHolder.Quit.setOnClickListener(this);
				double shippingPrice = item.getShippingPrice();
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
			itemHolder.timeRemaining.setText(item.getTimeRemaining());
			itemHolder.itemPic.setImageBitmap(item.getImg());
							
		}           
        return itemRow;
	}

	@Override
	public void onClick(View v) {
		ListView bidList ;
		switch(v.getId()){		
		case R.id.mySell_BidList:
			
			final Dialog dialog = new Dialog(activity);

			dialog.setContentView(R.layout.bidlist_dialog);
			dialog.setTitle("Item's Bids");
		//	bidList.setAdapter(MySellingBidListCustomAdapter(this.activity,this.layoutInflater), array de bids)
			Button okBTN = (Button) dialog.findViewById(R.id.bidOkButton);			
			okBTN.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) 
				{	
					if(v.getId() == R.id.bidOkButton)
					dialog.dismiss();
				}
			});    
			dialog.show();			
			
			break;
		case R.id.mySell_AcceptBidB:
			Toast.makeText(this.activity, "Your Auction has not ended yet", Toast.LENGTH_SHORT).show();
			break;
		case R.id.mySell_QuitB:
			Toast.makeText(this.activity, "Your Item will be removed from sale", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

}
