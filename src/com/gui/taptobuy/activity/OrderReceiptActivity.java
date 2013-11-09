package com.gui.taptobuy.activity;

import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OrderReceiptActivity extends Activity implements OnClickListener {
	
	TextView buyerUserName;
	TextView orderID;
	TextView sellerID;
	TextView shippAdd;
	ListView boughtItems;
	TextView totalPayment;
	TextView paymentCard;
	TextView purchasedDate;
	Button okButton;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.order_recipt);	
		
		boughtItems = (ListView)findViewById(R.id.receipt_itemList);
		okButton = (Button)findViewById(R.id.receipt_okB);
		buyerUserName = (TextView)findViewById(R.id.receipt_buyerUN);
		orderID = (TextView)findViewById(R.id.receipt_orderID);
		sellerID = (TextView)findViewById(R.id.receipt_sellerUN);
		shippAdd = (TextView)findViewById(R.id.receipt_ShippingAdress);
		totalPayment = (TextView)findViewById(R.id.receipt_totalPayment);
		paymentCard = (TextView)findViewById(R.id.receipt_paymentMethod);
		purchasedDate = (TextView)findViewById(R.id.receipt_Date);		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.receipt_okB){
			
		}		
	}
}
