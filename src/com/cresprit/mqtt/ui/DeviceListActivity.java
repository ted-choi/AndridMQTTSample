package com.cresprit.mqtt.ui;


import com.cresprit.mqtt.R;
import com.cresprit.mqtt.manager.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;

public class DeviceListActivity extends Activity implements		OnItemClickListener {
	private static String TAG = "SelectDeviceActivity";
	
	ListView listV;
	ImageView seperate_top;
	ImageView seperate_bottom;
	DeviceListAdapter adapter;
	DeviceListManager device;
	int selectDevice = 0;
	Handler handler = null;
	static int retryCnt = 0;
	String mSharedUrl = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectdevice_layout);
		
		Intent intent = getIntent();
		mSharedUrl = intent.getStringExtra(DeviceListManager.SHARED_URL);
		showDialog(1);
		device = DeviceListManager.getInstance(this);
		device.setDialogUpdateListener(listener);
		
				
		adapter = DeviceListAdapter.createSelectDeviceAdapter(DeviceListActivity.this);

		seperate_top = (ImageView) findViewById(R.id.seperate_top);
		seperate_bottom = (ImageView) findViewById(R.id.seperate_bottom);

		if (device.getDeviceCount() > 0) {
			seperate_top.setVisibility(View.VISIBLE);
			seperate_bottom.setVisibility(View.VISIBLE);
		}
		listV = (ListView) findViewById(R.id.deviceListv);
		listV.setAdapter(adapter);
		
		listV.setOnItemClickListener(this);
		handler = new Handler();
		device.getDeviceList(UserManager.getInstance(DeviceListActivity.this).getAuthKey());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Intent intent;
		if(mSharedUrl != null)
		{
			intent = new Intent(DeviceListActivity.this, PublishActivity.class);
			intent.putExtra(DeviceListManager.SHARED_URL, mSharedUrl);
			intent.putExtra(DeviceListManager.SELECT_DEVICE, position);
		}
		else
		{
			selectDevice = position;
			intent = new Intent(DeviceListActivity.this, SubMenuActivity.class);
			intent.putExtra(DeviceListManager.SELECT_DEVICE, position);
		}
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case 0:
			LayoutInflater factory = LayoutInflater.from(this);
            final View ButtonView = factory.inflate(R.layout.submenu, null);		
            
			return new AlertDialog.Builder(DeviceListActivity.this)
					.setView(ButtonView)
					.create();

		case 1:

			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.connecting);
			dialog.setMessage(getResources().getString(R.string.noti_wait_moment));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;

		}
		return null;
	}


	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		device.getDeviceList().clear();
	}




	private IUpdateListener listener = new IUpdateListener()
	{
		@Override
		public void update(int status, String result) {
			// TODO Auto-generated method stub
			if(status == IUpdateListener.SHOW_DIALOG)
			{
				//showDialog();
			}
			else//status == IUpdateListener.REMOVE_DIALOG
			{
				removeDialog(1);

			}
		}
	};
	
}
