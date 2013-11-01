package com.gui.taptobuy.activity;

import java.util.ArrayList;

import com.gui.taptobuy.Entities.Bid;
import com.gui.taptobuy.Entities.Category;
import com.gui.taptobuy.Entities.Product;

import com.gui.taptobuy.customadapter.MySellingBidListCustomAdapter;
import com.gui.taptobuy.customadapter.MySellingListCustomAdapter;
import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView.OnCloseListener;

public class BidsActivity extends Activity implements OnItemClickListener {
	private LayoutInflater layoutInflator;
	private ListView bidsList;
	private ArrayList<Bid> bids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emptylist);			
		bidsList = (ListView)findViewById(R.id.ViewList);
		this.layoutInflator = LayoutInflater.from(this);
//		bidsList.setAdapter(new MySellingBidListCustomAdapter(this,this.layoutInflator, this.bids));
//		bidsList.setOnItemClickListener(this);		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}	
	
	public static class MyBidHolder {
		public Bid bidToshow;
		public TextView bidPrice;
		public TextView placerUsername;
		public int productId;
		public int placerUserId;
	}
}


