package com.gui.taptobuy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.gui.taptobuy.phase1.R;

public class AdministratorActivity extends Activity implements OnClickListener {
	
	RadioButton RegUser1;
	RadioButton Admin1;
	EditText userId;
	Button viewUser;
	Button modifyUser;
	Button createAcc;
	EditText fromDate;
	EditText toDate;
	Button loadSales;
	EditText salesProd;
	Button salesViewB;
	EditText totalRev;
	Button totalRevViewB;
	
	
	RadioButton RegUser2;
	RadioButton Admin2;
	
	
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.account_admin);
		
		RegUser1 = (RadioButton) findViewById(R.id.checkRegUser);
		RegUser2 = (RadioButton) findViewById(R.id.checkRegUser2);
		Admin1 = (RadioButton) findViewById(R.id.checkAdmiUser);
		Admin2 = (RadioButton) findViewById(R.id.checkAdminUser2);
		userId = (EditText) findViewById(R.id.adminUserIDET);
		viewUser = (Button) findViewById(R.id.adminViewB);
		createAcc = (Button)findViewById(R.id.adminCreateAccB);
		fromDate = (EditText) findViewById(R.id.admin_dateFrom);
		toDate = (EditText) findViewById(R.id.admin_dateTo);
		loadSales = (Button) findViewById(R.id.adminTotalSalesB);
		salesProd = (EditText) findViewById(R.id.adminReportProductId);
		salesViewB = (Button) findViewById(R.id.adminViewBReport1);
		totalRev = (EditText) findViewById(R.id.adminReportTotalRevProdId);
		totalRevViewB = (Button) findViewById(R.id.adminViewBReport2);
		
		
		RegUser1.setOnClickListener(this);
		RegUser2.setOnClickListener(this);
		Admin2.setOnClickListener(this);
		Admin1.setOnClickListener(this);
		viewUser.setOnClickListener(this);
		createAcc.setOnClickListener(this);
		loadSales.setOnClickListener(this);
		salesViewB.setOnClickListener(this);
		totalRevViewB.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
