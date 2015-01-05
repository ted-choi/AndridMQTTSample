package com.cresprit.mqtt.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cresprit.mqtt.R;
import com.cresprit.mqtt.manager.ConnectionMgr;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class MessageAdapter extends BaseAdapter{
	Context context;
	JSONObject  json;
	
	class MessageHolder {
		View v;
		
		TextView message;
		TextView time;
		MessageHolder(View v){
			this.v = v;
		}

		TextView getTextView(){
			if (message == null) {
				message = (TextView)v.findViewById(R.id.message);
			}
			return message;
		}
		TextView getTimeView(){
			if (time == null) {
				time = (TextView)v.findViewById(R.id.time);
			}
			return time;
		}
	}
	
	
	public MessageAdapter(Context _context, int textViewResourceId,
			String[] objects) {
	
		// TODO Auto-generated constructor stub
		context = _context;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		TextView message;
		TextView time;
		JSONObject data_streams;
		JSONArray data_points;
		JSONObject data;
		JSONArray jvalue;
		JSONObject jvalue2;
		
		String text="";
		String datetime ="";
		
		MessageHolder holder;

		if( convertView == null ) {
			v = LayoutInflater.from(context).inflate(R.layout.listview, null);
			holder = new MessageHolder(v);
			v.setTag(holder);
		}
		else {
			v = convertView;
			holder = (MessageHolder) v.getTag();
		}

		message = holder.getTextView();
		time = holder.getTimeView();
		
		if(position == 0 || (position % 2) ==0 )
		{
			message.setBackgroundColor(Color.LTGRAY);
			message.setTextColor(Color.DKGRAY);
		}
		else
		{
			message.setBackgroundColor(Color.WHITE);
			message.setTextColor(Color.GRAY);
		}
		text = ConnectionMgr.getMessages().get(position);
		try {
			
			if(text != null)
			{
			json = new JSONObject(ConnectionMgr.getMessages().get(position));
			data_streams = json.getJSONObject("data");
			data_points = data_streams.getJSONArray("data_streams");
			data = data_points.getJSONObject(0);
			jvalue = data.getJSONArray("data_points");
			jvalue2 = jvalue.getJSONObject(0);
			text = jvalue2.get("v").toString();
			datetime = jvalue2.get("t").toString();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		message.setText(text);		
		time.setText(datetime);
		return v;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		ArrayList<String> msgList = ConnectionMgr.getMessages();
		if(msgList == null)
			return 0;
		
		int size =msgList.size();
		Log.i("", "size : "+size);
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ConnectionMgr.getMessages().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private DataSetObserver mMessageDataSetObserver = new DataSetObserver() {

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