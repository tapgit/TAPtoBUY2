package com.gui.taptobuy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gui.taptobuy.phase1.R;

public class OrderCheckoutActivity extends Activity implements OnClickListener{
	private Button placeOrder;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.order_checkout);
		placeOrder = (Button) findViewById(R.id.checkout_PlaceOrderB);
		placeOrder.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.checkout_PlaceOrderB){
			Toast.makeText(this, "Your order has been placed!", Toast.LENGTH_LONG).show();
			//anadir producto a la lista de bougth items del usuario, Id de produto y de usuario
		}		
	}
}
