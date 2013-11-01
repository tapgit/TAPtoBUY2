package com.gui.taptobuy.activity;

import java.io.File;

import com.gui.taptobuy.Entities.Product;
import com.gui.taptobuy.Entities.ProductForAuctionInfo;
import com.gui.taptobuy.Entities.ProductForSaleInfo;
import com.gui.taptobuy.datatask.ImageManager;
import com.gui.taptobuy.phase1.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SellAnItemActivity extends Activity implements OnClickListener
{	
	//for image purposes
	private static final int SELECT_PICTURE = 1;
	private String picPathByFileManager;
	private String picPathByGallery;	
	private TextView picPathInput;

	private int prodID = -1;
	private double sellerRate = -1;
	private String sellerUsername = null;

	private EditText prodTitle;
	private EditText prodModel;
	private EditText prodBrand;
	private EditText prodDimen;
	private TextView prodDescrip;
	private EditText prodProduct;
	private EditText prodBuyPriceIn;
	private EditText prodStartingPrice;
	private EditText shippingPrice;
	private EditText prodTime;
	private EditText prodQty;


	private CheckBox forBidCheck;
	private Product newProd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.account_sellanitem);	 

		((Button) findViewById(R.id.sell_uploadPicB)).setOnClickListener(this);  
		((Button) findViewById(R.id.sell_sellItemB)).setOnClickListener(this); 


		prodTitle = (EditText) findViewById(R.id.sell_inputProdTitle);
		prodQty = (EditText) findViewById(R.id.sell_inputQty);
		prodModel = (EditText) findViewById(R.id.sell_inputModel);
		prodProduct = (EditText) findViewById(R.id.sell_inputProduct);
		prodBrand = (EditText) findViewById(R.id.sell_inputBrand);
		prodDimen = (EditText) findViewById(R.id.sell_inputDimensions);
		prodDescrip = (EditText) findViewById(R.id.sell_inputDescription);
		prodBuyPriceIn = (EditText) findViewById(R.id.sell_inputBuyNowPrice);
		prodStartingPrice = (EditText) findViewById(R.id.sell_inputStartingPrice);
		shippingPrice = (EditText) findViewById(R.id.sell_inputShipping);	        
		prodTime = (EditText) findViewById(R.id.sell_inputNumofDays);
		forBidCheck = (CheckBox) findViewById(R.id.sell_ForBiddingCheck);
		picPathInput = (TextView)findViewById(R.id.sell_PicturePath);

		forBidCheck.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {	

		switch(v.getId()){

		case R.id.sell_ForBiddingCheck:
			if(!forBidCheck.isChecked())
				prodBuyPriceIn.setClickable(false);			
			break;

			//   select a file
		case R.id.sell_uploadPicB:
			Intent getPic = new Intent();
			getPic.setType("image/*");
			getPic.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(getPic,"Select Picture"), SELECT_PICTURE);
			break;

		case R.id.sell_sellItemB:
			if(forBidCheck.isChecked()){
				newProd = new ProductForAuctionInfo(prodID, prodTitle.getText().toString(),prodTime.getText().toString(), Double.parseDouble(prodStartingPrice.getText().toString()), 
						picPathInput.getText().toString(), sellerUsername, sellerRate,  Double.parseDouble(shippingPrice.getText().toString()), 
						-1, -1, prodProduct.getText().toString(), prodModel.getText().toString(), prodBrand.getText().toString(), prodDimen.getText().toString(), prodDescrip.getText().toString());    				
				//add to server
			}
			else{    	    				

				newProd = new ProductForSaleInfo(prodID, prodTitle.getText().toString(),prodTime.getText().toString(), Double.parseDouble(shippingPrice.getText().toString()), 
						picPathInput.getText().toString(), sellerUsername, sellerRate, Integer.parseInt(prodQty.getText().toString()), Double.parseDouble(prodBuyPriceIn.getText().toString()),
						prodProduct.getText().toString(), prodModel.getText().toString(), prodBrand.getText().toString(), prodDimen.getText().toString(), prodDescrip.getText().toString());

				//add to server
			}
			Toast.makeText(this, "Your product has been place on sale", Toast.LENGTH_SHORT).show();
			break;
		}	

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();

				//OI FILE Manager
				//picPathByFileManager = selectedImageUri.getPath();

				//MEDIA GALLERY
				picPathByGallery = getRealPathFromURI(selectedImageUri);

				//setting the path to display
				if(picPathByGallery!=null){
					picPathInput.setText(picPathByGallery);	
					//Toast.makeText(SellAnItemActivity.this, picPathByGallery.toString(), Toast.LENGTH_LONG).show();
					new UploadImageTask().execute(picPathByGallery.toString());
				}
				else{
					Toast.makeText(SellAnItemActivity.this,"No image selected..", Toast.LENGTH_LONG).show();
					//	System.out.println("selectedImagePath is null");
				}
				//				if(picPathByFileManager!=null)
				//					picPathInput.setText(picPathByFileManager);	
				//				else System.out.println("filemanagerstring is null");

				Toast.makeText(SellAnItemActivity.this, picPathByGallery.toString(), Toast.LENGTH_LONG).show();

			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		Cursor cursor = null;
		try { 
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
//	public String getPath(Uri uri) {
//		String selectedImagePath;
//		//1:MEDIA GALLERY --- query from MediaStore.Images.Media.DATA
//		String[] projection = { MediaStore.Images.Media.DATA };
//		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//		if(cursor != null){
//			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//			cursor.moveToFirst();
//			selectedImagePath = cursor.getString(column_index);
//		}else{
//			selectedImagePath = null;
//		}
//
//		if(selectedImagePath == null){
//			//2:OI FILE Manager --- call method: uri.getPath()
//			selectedImagePath = uri.getPath();
//		}
//		return selectedImagePath;
//	}

	private class UploadImageTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog = null;
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(SellAnItemActivity.this, "Please wait...", "Uploading image");
			dialog.show();
		}
		protected Boolean doInBackground(String... imageLocalDir) {
			return ImageManager.uploadImage(imageLocalDir[0]);
		}
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				Toast.makeText(SellAnItemActivity.this, "Image has been uploaded successfully.", Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(SellAnItemActivity.this, "Image could not be uploaded.", Toast.LENGTH_LONG).show();
			}
		}
	}
}