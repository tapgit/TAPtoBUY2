package com.gui.taptobuy.activity;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gui.taptobuy.Entities.Address;
import com.gui.taptobuy.Entities.CreditCard;
import com.gui.taptobuy.Entities.Product;
import com.gui.taptobuy.Entities.ProductForAuction;
import com.gui.taptobuy.Entities.ProductForSale;
import com.gui.taptobuy.Entities.User;

import com.gui.taptobuy.customadapter.OrderCustomListAdapter;

import com.gui.taptobuy.datatask.ImageManager;
import com.gui.taptobuy.datatask.Main;
import com.gui.taptobuy.phase1.R;

public class OrderCheckoutActivity extends Activity implements OnClickListener{
	
	private Button placeOrder;
	private Spinner cardsSpinner;
	private Spinner shipAddrSpinner;
	private TextView shippingAdd;
	private ListView itemsList;
	private User receivedUserdata;
	public static ArrayList<Product>  items;
	private ArrayList<Integer> productsIDList;
	private LayoutInflater layoutInflator;
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.order_checkout);		
		new searchProductsTask().execute("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		
		this.layoutInflator = LayoutInflater.from(this);
		
		Intent intent = getIntent();
		if(intent.getStringExtra("previousActivity").equals("Cart")){
			productsIDList = intent.getIntegerArrayListExtra("productsID");
		}
		else if(intent.getStringExtra("previousActivity").equals("BuyItProductInfo")){
			productsIDList = new ArrayList<Integer>();
			productsIDList.add(intent.getIntExtra("productID", 0)); /// cudiado con el 0 - default value
		}
		
		Toast.makeText(this, productsIDList.get(0)+"", Toast.LENGTH_SHORT);
		
		itemsList = (ListView)findViewById(R.id.checkout_ItemsList);
		shippingAdd = (TextView)findViewById(R.id.checkout_ShippingAdress);
		
		placeOrder = (Button) findViewById(R.id.checkout_PlaceOrderB);
		placeOrder.setOnClickListener(this);
		
		cardsSpinner = (Spinner) findViewById (R.id.checkout_CardsSpinner);
		shipAddrSpinner = (Spinner) findViewById(R.id.checkout_SpinnerAdd);			

		shipAddrSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){	
			@Override
			public void onItemSelected(AdapterView<?> arg0,View arg1,int arg2, long arg3) 
			{
				int index = arg0.getSelectedItemPosition();
				Address selectedAddress = receivedUserdata.getShipping_addresses()[index];
				shippingAdd.setText(selectedAddress.getContact_name() + "\n" + selectedAddress.getStreet() +  "\n" + selectedAddress.getCity() + 
						" " + selectedAddress.getState() + " " + selectedAddress.getZip_code() + "\n" + selectedAddress.getCountry() + "\n" + 
						selectedAddress.getTelephone());
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}	        	
		});
		
		new getMyAccountSettingsTask().execute();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.checkout_PlaceOrderB){
			Toast.makeText(this, "Your order has been placed!", Toast.LENGTH_LONG).show();
			//anadir producto a la lista de bougth items del usuario, Id de produto y de usuario
			// enviar la orden al DB
		}		
	}
	
	
	private User getMyAccountSettings(){
		HttpClient httpClient = new DefaultHttpClient();
		String userAccountDir = Main.hostName +"/user/" + Main.userId;
		HttpGet get = new HttpGet(userAccountDir);
		get.setHeader("content-type", "application/json");
		try
		{
			HttpResponse resp = httpClient.execute(get);
			if(resp.getStatusLine().getStatusCode() == 200){
				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONObject userJson = new JSONObject(jsonString);
				JSONArray shippingAddressesArray = userJson.getJSONArray("shipping_addresses");
				JSONArray creditCardsArray = userJson.getJSONArray("credit_cards");

				JSONObject jsonAddress = null;
				JSONObject jsonCreditCard = null;

				//Get shipping addresses
				Address[] shippingAddresses = new Address[shippingAddressesArray.length()];
				for(int i=0;i<shippingAddressesArray.length();i++){
					jsonAddress = shippingAddressesArray.getJSONObject(i);
					shippingAddresses[i] = new Address(jsonAddress.getInt("id"),jsonAddress.getString("country"), jsonAddress.getString("contact_name"), 
							jsonAddress.getString("street"), jsonAddress.getString("city"), jsonAddress.getString("state"), 
							jsonAddress.getString("zip_code"), jsonAddress.getString("telephone"));
				}
				//Get credit cards
				CreditCard[] creditCards = new CreditCard[creditCardsArray.length()];
				for(int i=0;i<creditCardsArray.length();i++){
					jsonCreditCard = creditCardsArray.getJSONObject(i);
					//get the billing address from this credit card		
					jsonAddress = jsonCreditCard.getJSONObject("billing_address");
					Address aBillingAddress = new Address(jsonAddress.getInt("id"),jsonAddress.getString("country"), jsonAddress.getString("contact_name"), 
							jsonAddress.getString("street"), jsonAddress.getString("city"), jsonAddress.getString("state"), 
							jsonAddress.getString("zip_code"), jsonAddress.getString("telephone"));

					creditCards[i] = new CreditCard(jsonCreditCard.getString("number"), jsonCreditCard.getString("holders_name"), 
							jsonCreditCard.getString("exp_date"), aBillingAddress);
				}

				receivedUserdata = new User(userJson.getInt("id"), userJson.getString("firstname"), userJson.getString("lastname"), 
						userJson.getString("username"), userJson.getString("password"), userJson.getString("email"), 
						shippingAddresses, creditCards);


			}
			else{
				Log.e("JSON","User account data could not be downloaded.");
			}
		}
		catch(Exception ex)
		{
			Log.e("Account Settings","Error!", ex);
		}
		return receivedUserdata;
	}

	private class getMyAccountSettingsTask extends AsyncTask<Void,Void,User> {
		protected User doInBackground(Void... params) {
			return getMyAccountSettings();
		}
		protected void onPostExecute(User receivedUserdata) {

		
			Address firstAddress = receivedUserdata.getShipping_addresses()[0];
			shippingAdd.setText(firstAddress.getContact_name() + "\n" + firstAddress.getStreet() +  "\n" + firstAddress.getCity() + 
					" " + firstAddress.getState() + " " + firstAddress.getZip_code() + "\n" + firstAddress.getCountry() + "\n" + 
					firstAddress.getTelephone());

			//Extraer los identificadores de cada shipping address para llenar el spinner:
			String[] shippingAddressesIdentifiers = new String[receivedUserdata.getShipping_addresses().length];
			for(int i=0;i<shippingAddressesIdentifiers.length;i++){
				shippingAddressesIdentifiers[i] = receivedUserdata.getShipping_addresses()[i].getStreet();
			}

			//Extraer los identificadores de cada credit card para llenar el spinner:
			String[] creditCardsIdentifiers = new String[receivedUserdata.getCredit_cards().length];
			for(int i=0;i<creditCardsIdentifiers.length;i++){
				CreditCard tmpCrdCard = receivedUserdata.getCredit_cards()[i];
				creditCardsIdentifiers[i] = "xxxx-xxxx-xxxx-" + tmpCrdCard.getNumber().substring(12);
			}
			
			ArrayAdapter<String> shippingAddressesAdapter = new ArrayAdapter<String>(OrderCheckoutActivity.this,android.R.layout.simple_list_item_single_choice, shippingAddressesIdentifiers);
			shipAddrSpinner.setAdapter(shippingAddressesAdapter);

			ArrayAdapter<String> creditCardsAdapter = new ArrayAdapter<String>(OrderCheckoutActivity.this,android.R.layout.simple_list_item_single_choice, creditCardsIdentifiers);
			cardsSpinner.setAdapter(creditCardsAdapter);

		}			

	}
		
	private ArrayList<Product> getSearchItems(String searchString){
		HttpClient httpClient = new DefaultHttpClient();
		String searchDir = Main.hostName +"/search/" + "aaaaaaaaaaaaaaaaaaaaa";
		HttpGet get = new HttpGet(searchDir);
		get.setHeader("content-type", "application/json");
		try
		{
			HttpResponse resp = httpClient.execute(get);
			if(resp.getStatusLine().getStatusCode() == 200){
				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONArray searchResultArray = (new JSONObject(jsonString)).getJSONArray("results");
				items = new ArrayList<Product>();

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
					items.add(anItem);
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
		return items;
	}

	private class searchProductsTask extends AsyncTask<String,Void,ArrayList<Product>> {
		public  int downloadadImagesIndex = 0;
		protected ArrayList<Product> doInBackground(String... params) {
			return getSearchItems(params[0]);//get search result
		}
		protected void onPostExecute(ArrayList<Product> searchResultItems ) {
			//download images
			for(Product itm: searchResultItems){
				new DownloadImageTask().execute(itm.getImgLink());
			}
			itemsList.setAdapter(new OrderCustomListAdapter(OrderCheckoutActivity.this, OrderCheckoutActivity.this.layoutInflator, searchResultItems));
		}			
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

			protected Bitmap doInBackground(String... urls) {
				return ImageManager.downloadImage(urls[0]);
			}
			protected void onPostExecute(Bitmap result) {
				itemsList.invalidateViews();
				items.get(downloadadImagesIndex++).setImg(result);
			}
		}
	}
}
