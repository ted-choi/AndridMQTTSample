package com.cresprit.mqtt.ui;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.cresprit.mqtt.R;
import com.cresprit.mqtt.manager.Connection;
import com.cresprit.mqtt.manager.ConnectionMgr;
import com.cresprit.mqtt.manager.DeviceListManager;
import com.cresprit.mqtt.service.MQTTService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SubscribeActivity extends Activity {
	EditText edtFeed;
	Button btnSubscribe;
	String feed;
	Connection connection;
	MessageAdapter adapter;
	ListView tvMessages;
	int mSelectedDevice;
	Intent intent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subscribe);
		
		tvMessages = (ListView)findViewById(R.id.listMessages);
		adapter = new MessageAdapter(SubscribeActivity.this,R.id.listMessages, null );
		tvMessages.setAdapter(adapter);
		
		registerReceiver(mBroadcastReceiver, new IntentFilter(ConnectionMgr.MSG_RECEIVE));
		
		intent = new Intent(SubscribeActivity.this, MQTTService.class);
		intent.putExtra(DeviceListManager.SELECT_DEVICE, mSelectedDevice);
        startService(intent);
	}
	
	final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			adapter.notifyDataSetChanged();
			tvMessages.setSelection(ConnectionMgr.getMessages().size());
		}
	};
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		stopService(intent);
		super.onDestroy();
	}

}