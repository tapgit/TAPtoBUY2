package com.gui.taptobuy.activity;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.gui.taptobuy.Entities.Address;
import com.gui.taptobuy.Entities.CreditCard;
import com.gui.taptobuy.Entities.User;
import com.gui.taptobuy.datatask.Main;
import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AccountSettingsActivity extends Activity implements OnClickListener {

	private Spinner cardsSpinner;
	private Spinner shipAddrSpinner;
	private Dialog Dialog;
	private Button addCard;
	private Button removeCard;
	private Button save;	

	private Button addShippingAdd;
	private Button removeShippingAdd;

	private User receivedUserdata;

	private EditText userName;
	private EditText firstname;
	private EditText password;
	private EditText lastname;
	private EditText email;
	private TextView shippingAdd;


	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		//if(!user.isAdmin)
		setContentView(R.layout.account_settings);
		//else
		//setContentView(R.layout.account_admin);
		addCard = (Button)findViewById(R.id.accSet_AddB);
		removeCard = (Button)findViewById(R.id.accSet_RemoveB);
		save = (Button)findViewById(R.id.accSet_SaveB);

		addShippingAdd = (Button)findViewById(R.id.accSet_AddSAButton);
		removeShippingAdd = (Button)findViewById(R.id.accSet_removeSAButton);

		firstname = (EditText) findViewById(R.id.accSet_Fname);
		lastname = (EditText)findViewById(R.id.accSet_Lname);
		password = (EditText)findViewById(R.id.accSet_Passsword);
		email = (EditText)findViewById(R.id.AccSet_email);
		userName = (EditText)findViewById(R.id.accSet_inputUserName);
		shippingAdd = (TextView)findViewById(R.id.accSet_ShippingAddressBox);

		addCard.setOnClickListener(this);
		removeCard.setOnClickListener(this);
		save.setOnClickListener(this);

		addShippingAdd.setOnClickListener(this);
		removeShippingAdd.setOnClickListener(this);

		cardsSpinner = (Spinner) findViewById (R.id.accSet_CardsSpinner);
		shipAddrSpinner = (Spinner) findViewById(R.id.accSet_AddressSpinner);


		// setting action for when an sorting instance is selected

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

		switch (v.getId()){

		//////////////SHIPPING ADDRESS SETTINGS		

		case R.id.accSet_AddSAButton:
			Dialog = new Dialog(AccountSettingsActivity.this);

			Dialog.setContentView(R.layout.addshipping_address_dialog);
			Dialog.setTitle("Add New Shipping Address");

			final EditText country = (EditText) Dialog.findViewById(R.id.shippingADD_Country);
			final EditText streetAdd = (EditText) Dialog.findViewById(R.id.shippingADD_StreetAdd);  
			final EditText city = (EditText) Dialog.findViewById(R.id.shippingADD_City);  
			final EditText state = (EditText) Dialog.findViewById(R.id.shippingADD_State); 
			final EditText zipcode = (EditText) Dialog.findViewById(R.id.shippingADD_ZipCode); 
			final EditText phone = (EditText) Dialog.findViewById(R.id.shippingADD_TelNum); 
			final EditText contactName = (EditText) Dialog.findViewById(R.id.shippingADD_ContactName);

			Button btnAddSA = (Button) Dialog.findViewById(R.id.shippingADD_addB);			
			btnAddSA.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) 
				{			                
					String Country = country.getText().toString();
					String StreetAddress = streetAdd.getText().toString();
					String City = city.getText().toString();
					String State = state.getText().toString();
					String ZipCode = zipcode.getText().toString();
					String Telephone = phone.getText().toString();
					String ContactName = contactName.getText().toString();

					if(!Country.equals("")&&!StreetAddress.equals("")&&!City.equals("")&&!State.equals("")&&!ZipCode.equals("")&&!Telephone.equals(""))
					{
						Address newShippingAddress = new Address(-1,Country, ContactName, StreetAddress, City, State, ZipCode, Telephone);
						new addShippingAddressTask().execute(newShippingAddress);
					}	
					else{
						Toast.makeText(AccountSettingsActivity.this, "Error: You must fill every field", Toast.LENGTH_SHORT).show();
					}					      				                
				}
			});    
			Dialog.show();

			break;

		case R.id.accSet_removeSAButton:
			//new removeShippingAddressTask(sacardID);
			Toast.makeText(this, "Address successfully Removed", Toast.LENGTH_SHORT).show();	
			break;			



			////////////////////CREDIT CARD SETTINGS

		case R.id.accSet_AddB:

			Dialog = new Dialog(AccountSettingsActivity.this);

			Dialog.setContentView(R.layout.addcard_dialog);
			Dialog.setTitle("Add Credit Card");

			final EditText CardNumET = (EditText) Dialog.findViewById(R.id.addCard_CardNum);
			final EditText CardHolderET = (EditText) Dialog.findViewById(R.id.addCard_HolderName);  
			final EditText CardExpDateET = (EditText) Dialog.findViewById(R.id.addCard_ExpDate);  
			
			final EditText billingContactNameEt = (EditText) Dialog.findViewById(R.id.etBillUserContactName);
			final EditText billingCountryEt = (EditText) Dialog.findViewById(R.id.etBillUserCountry);
			final EditText billingStreetEt = (EditText) Dialog.findViewById(R.id.etBillUserStreetAdd);
			final EditText billingCityEt = (EditText) Dialog.findViewById(R.id.etBillUserCity);
			final EditText billingStateEt = (EditText) Dialog.findViewById(R.id.etBillUserState);
			final EditText billingZipCodeEt = (EditText) Dialog.findViewById(R.id.etBillUserZipCode);
			final EditText billingTelephoneEt = (EditText) Dialog.findViewById(R.id.etBillUserTelNum);
			
			Button btnAddCard = (Button) Dialog.findViewById(R.id.addCardB);			
			btnAddCard.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) 
				{			                
					String CardNum = CardNumET.getText().toString();
					String CardHolder = CardHolderET.getText().toString();
					String CardDate = CardExpDateET.getText().toString();
					
					String contact_name = billingContactNameEt.getText().toString();
					String country = billingCountryEt.getText().toString();
					String street = billingStreetEt.getText().toString();
					String city = billingCityEt.getText().toString();
					String state = billingStateEt.getText().toString();
					String zip_code = billingZipCodeEt.getText().toString();
					String telephone = billingTelephoneEt.getText().toString();
					

					if(!CardNum.equals("")&&!CardHolder.equals("")&&!CardDate.equals("")&&!contact_name.equals("")&&!country.equals("")&&
							!street.equals("")&&!city.equals("")&&!state.equals("")&&!zip_code.equals("")&&!telephone.equals(""))
					{
						Address billing_address = new Address(-1,country, contact_name, street, city, state, zip_code, telephone);
						CreditCard newCreditCard = new CreditCard(CardNum, CardHolder, CardDate, billing_address);
						new addCreditCardTask().execute(newCreditCard);
					}	
					else{
						Toast.makeText(AccountSettingsActivity.this, "Error: You must fill every field", Toast.LENGTH_SHORT).show();
					}					      				                
				}
			});    
			Dialog.show();

			break;

		case R.id.accSet_RemoveB:
			Toast.makeText(this, "Card successfully Removed", Toast.LENGTH_SHORT).show();	
			break;

		case R.id.accSet_SaveB:
			Toast.makeText(this, "Your settings had been updated", Toast.LENGTH_SHORT).show();
			break;		
		}
	}

	private boolean addNewCreditCard(CreditCard newCreditCard){
		HttpClient httpClient = new DefaultHttpClient();
		String newCreditCardDir = Main.hostName+ "/user/" + Main.userId + "/addCreditCard";
		HttpPost post = new HttpPost(newCreditCardDir);
		post.setHeader("content-type", "application/json");
		try
		{
			JSONObject billJson = new JSONObject();
			billJson.put("country",newCreditCard.getBilling_address().getCountry());
			billJson.put("contact_name",newCreditCard.getBilling_address().getContact_name());
			billJson.put("street",newCreditCard.getBilling_address().getStreet());
			billJson.put("city",newCreditCard.getBilling_address().getCity());
			billJson.put("state",newCreditCard.getBilling_address().getState());
			billJson.put("zip_code",newCreditCard.getBilling_address().getZip_code());
			billJson.put("telephone",newCreditCard.getBilling_address().getTelephone());
			JSONObject json = new JSONObject();
			json.put("number", newCreditCard.getNumber());
			json.put("holders_name", newCreditCard.getHolders_name());
			json.put("exp_date", newCreditCard.getExp_date());
			json.put("billing_address", billJson);

			StringEntity entity = new StringEntity(json.toString());
			post.setEntity(entity);
			HttpResponse resp = httpClient.execute(post);
			
			if(resp.getStatusLine().getStatusCode() == 200){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception ex)
		{
			Log.e("Could not add credit card","Error!", ex);
			return false;
		}
	}
	private class addCreditCardTask extends AsyncTask<CreditCard,Void,Boolean> {
		protected Boolean doInBackground(CreditCard... newCreditCard) {
			return addNewCreditCard(newCreditCard[0]);
		}
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(AccountSettingsActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
				Dialog.dismiss();
			}
			else{
				Toast.makeText(AccountSettingsActivity.this, "The credit card could not be added", Toast.LENGTH_SHORT).show();
			}
		}
	}
	private boolean addNewShippingAddress(Address newShippingAddress){
		HttpClient httpClient = new DefaultHttpClient();
		String newShippAddrDir = Main.hostName+ "/user/" + Main.userId + "/addshippAddr";
		HttpPost post = new HttpPost(newShippAddrDir);
		post.setHeader("content-type", "application/json");
		try
		{
			JSONObject json = new JSONObject();
			json.put("country",newShippingAddress.getCountry());
			json.put("contact_name",newShippingAddress.getContact_name());
			json.put("street",newShippingAddress.getStreet());
			json.put("city",newShippingAddress.getCity());
			json.put("state",newShippingAddress.getState());
			json.put("zip_code",newShippingAddress.getZip_code());
			json.put("telephone",newShippingAddress.getTelephone());

			StringEntity entity = new StringEntity(json.toString());
			post.setEntity(entity);
			HttpResponse resp = httpClient.execute(post);
			
			if(resp.getStatusLine().getStatusCode() == 200){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception ex)
		{
			Log.e("Could not add shipping address","Error!", ex);
			return false;
		}
	}
	
	

	private class addShippingAddressTask extends AsyncTask<Address,Void,Boolean> {
		protected Boolean doInBackground(Address... newShipAddr) {
			return addNewShippingAddress(newShipAddr[0]);
		}
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(AccountSettingsActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
				Dialog.dismiss();
			}
			else{
				Toast.makeText(AccountSettingsActivity.this, "The shipping address could not be added", Toast.LENGTH_SHORT).show();
			}
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

			Toast.makeText(AccountSettingsActivity.this, receivedUserdata.getId() + "", Toast.LENGTH_LONG).show();

			firstname.setText(receivedUserdata.getFirstname());
			lastname.setText(receivedUserdata.getLastname());
			password.setText(receivedUserdata.getPassword());
			email.setText(receivedUserdata.getEmail());
			userName.setText(receivedUserdata.getUsername());
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


			ArrayAdapter<String> shippingAddressesAdapter = new ArrayAdapter<String>(AccountSettingsActivity.this,android.R.layout.simple_list_item_single_choice, shippingAddressesIdentifiers);
			shipAddrSpinner.setAdapter(shippingAddressesAdapter);

			ArrayAdapter<String> creditCardsAdapter = new ArrayAdapter<String>(AccountSettingsActivity.this,android.R.layout.simple_list_item_single_choice, creditCardsIdentifiers);
			cardsSpinner.setAdapter(creditCardsAdapter);

		}			

	}
}