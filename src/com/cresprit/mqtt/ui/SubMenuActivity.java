package com.cresprit.mqtt.ui;

import java.util.Vector;

import com.cresprit.mqtt.manager.*;
import com.cresprit.mqtt.manager.DeviceListManager.DeviceInfo;
import com.cresprit.mqtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SubMenuActivity extends Activity{
		private Vector<DeviceInfo> deviceList;
		private int mSelectedDevice  = 0;
		private TextView deviceNameTv = null;
		private TextView productNameTv = null;
		private TextView authKeyTv = null;
		private Button publishBtn = null;
		private Button subscribeBtn = null;
		private Button quitBtn = null;
		
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mSelectedDevice = intent.getIntExtra(DeviceListManager.SELECT_DEVICE,  0);
	
		setContentView(R.layout.submenu);
		
		deviceNameTv = (TextView)findViewById(R.id.device_name);
		productNameTv = (TextView)findViewById(R.id.product_name);
		authKeyTv = (TextView)findViewById(R.id.auth_key);
		publishBtn = (Button)findViewById(R.id.publish);
		subscribeBtn = (Button)findViewById(R.id.subscribe);
		quitBtn = (Button)findViewById(R.id.quit);
		quitBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		publishBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				final Intent intent = new Intent(SubMenuActivity.this, PublishActivity.class);
				intent.putExtra(DeviceListManager.SELECT_DEVICE, mSelectedDevice);
                startActivity(intent);
                finish();
			}
		}); 
		
		subscribeBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent(SubMenuActivity.this, SubscribeActivity.class);
                startActivity(intent);
                finish();
			}
		});
		
		deviceList = DeviceListManager.getInstance(SubMenuActivity.this).getDeviceList();
		
		String deviceName = deviceList.get(mSelectedDevice).getDeviceName();
		String productName = deviceList.get(mSelectedDevice).getProductname();
		
		deviceNameTv.setText(deviceName);
		productNameTv.setText(productName);
		authKeyTv.setText(UserManager.getInstance(SubMenuActivity.this).getAuthKey());
	}
}