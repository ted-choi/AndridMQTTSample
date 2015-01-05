package com.cresprit.mqtt.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cresprit.mqtt.R;
import com.cresprit.mqtt.manager.*;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;



class DeviceListAdapter extends BaseAdapter{

	private Context context;
	private DeviceListManager deviceManager;
	
	public DeviceListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		deviceManager = DeviceListManager.getInstance(context);
		deviceManager.setDeviceListObserverListener(mDeviceListDataSetObserver);
		
	}
	public static DeviceListAdapter createSelectDeviceAdapter(Context context) {
		return new DeviceListAdapter(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count=0;
		count = deviceManager.getDeviceList().size();
		Log.i("", "count : "+count);
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		
		if(deviceManager.getDeviceList().size() == 0)
			return null;
		else
			return deviceManager.getDeviceList().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = null;
		TextView deviceNameV;
		TextView deviceIdV;
		LinearLayout backgroundV;
		
		String deviceName;
		String deviceId;
		
		DeviceHolder holder;

		if( convertView == null ) {
			v = LayoutInflater.from(context).inflate(R.layout.devicelistview, null);
			holder = new DeviceHolder(v);
			v.setTag(holder);
		}
		else {
			v = convertView;
			holder = (DeviceHolder) v.getTag();
		}

		backgroundV = holder.getBackgroundView();
		
		deviceName = deviceManager.getDeviceList().get(position).getDeviceName();
		deviceId = deviceManager.getDeviceList().get(position).getProductname();
		
		deviceNameV = holder.getDeviceNameView();
		deviceNameV.setText(deviceName);		
		deviceIdV  = holder.getDeviceIdView();
		deviceIdV.setText(deviceId);	
		
		if(position == 0 || (position % 2) ==0 )
		{
			backgroundV.setBackgroundColor(Color.LTGRAY);
			deviceNameV.setTextColor(Color.DKGRAY);
			deviceIdV.setTextColor(Color.DKGRAY);
		}
		else
		{
			backgroundV.setBackgroundColor(Color.WHITE);
			deviceNameV.setTextColor(Color.GRAY);
			deviceIdV.setTextColor(Color.GRAY);
		}
		return v;
	}
	
	class DeviceHolder {
		View v;
		
		TextView name;
		TextView id;
		LinearLayout background;
		
		DeviceHolder(View v){
			this.v = v;
		}

		TextView getDeviceNameView(){
			if (name == null) {
				name = (TextView)v.findViewById(R.id.device_name);
			}
			return name;
		}
		
		TextView getDeviceIdView(){
			if(id == null) {
				id = (TextView)v.findViewById(R.id.device_id);
			}
			return id;
		}
		
		LinearLayout getBackgroundView()
		{
			if(background == null){
				background = (LinearLayout)v.findViewById(R.id.background);
			}
			return background;
		}
	}
	
	private DataSetObserver mDeviceListDataSetObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			notifyDataSetChanged();
			super.onChanged();
			
		}

		@Override
		public void onInvalidated() {
			notifyDataSetInvalidated();
			super.onInvalidated();
		}

	};
}