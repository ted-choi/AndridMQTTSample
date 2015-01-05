package com.cresprit.mqtt.manager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cresprit.mqtt.ui.IUpdateListener;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.Log;

public class DeviceListManager{
	private String TAG = "SelectDeviceManager";
	public static String SELECT_DEVICE = "com.cresprit.mqtt.DeviceListActivity.Select_device";
	public static String SHARED_URL = "com.cresprit.mqtt.DeviceListManager.Shared_URL";
	private final Vector<DeviceInfo> deviceList = new Vector<DeviceInfo>();

	
	private static DeviceListManager __instance;
	private Context context;
	private String mSelectDevice = null;
	private String mAuthKey = null;
	private DataSetObserver mObserver = null;
	
	private IUpdateListener mListener = null;
	static int retryCnt = 0;
	

	public static DeviceListManager getInstance(Context appContext) {
		if (__instance == null) {
			__instance = new DeviceListManager(appContext);
			
		}
		return __instance;
	}
	
	public DeviceListManager(Context ctx)
	{
		this.context=ctx;
	}
	
	public void setDialogUpdateListener(IUpdateListener _listener)
	{
		mListener = _listener;
	}
	
	public void setDeviceListObserverListener(DataSetObserver _observer) {
		mObserver = _observer;
	}
	
	public Vector<DeviceInfo> getDeviceList()
	{
		return deviceList;
	}
	
	public void setSelectDevice(String _device)
	{
		mSelectDevice = _device;
	}
	
	public String getSelectDevice()
	{
		return mSelectDevice;
	}
	
	
	public int getDeviceCount()
	{
		return deviceList.size();
	}
	
	public void getDeviceList(String _authKey)
	{
		mAuthKey = _authKey;
		new DeviceList().execute(mAuthKey);
	}
	


	class DeviceList extends AsyncTask<String, Void, Boolean> {
		String auid = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//mListener.update(IUpdateListener.SHOW_DIALOG);
		}

		@Override
		protected Boolean doInBackground(String... key) {
			// TODO Auto-generated method stub
			int responseCode;
			
			HttpClient client = new DefaultHttpClient();
			final HttpParams params = client.getParams();
			
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);

			HttpPost request = new HttpPost();
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Authorization", "Bearer "+key[0]);
			try {
				request.setURI(new URI(ConnectionMgr.SERVER_API_GET_DEVICE_LIST_URL));
			} catch (URISyntaxException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}


			
			try {
				HttpResponse response = client.execute(request);

				Log.i(TAG, "get http response: STATUS_CODE: " + response.getStatusLine().getStatusCode()+response.getStatusLine().getReasonPhrase());
				responseCode = response.getStatusLine().getStatusCode();
				if(responseCode == 406 || responseCode == 401 || responseCode == 404 || responseCode == 500)
				{
					return false;
				}
				
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(entity);
				try {
					JSONObject resObj = new JSONObject(jsonStr);
					JSONObject resData = resObj.getJSONObject("data");
					JSONArray deviceArray = resData.getJSONArray("devices"); 
					
					for(int i=0; i<deviceArray.length();i++)
					{
						JSONObject device = deviceArray.getJSONObject(i);
						String name = device.getString("name");
						String feedId = device.getString("feed_id");
						String activationCode = device.getString("activation_code"); 
						String productName = device.getString("product_name");
						
						deviceList.add(new DeviceInfo(name, feedId, productName, activationCode));
					}
					Log.i("","resData : "+resData.toString());
					//key = resData.getString("key");

					Log.i("","key : "+key);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;	
		}

		@Override
		protected void onPostExecute(Boolean _auid) {
			// TODO Auto-generated method stub
			if(mObserver != null)
				mObserver.onInvalidated();
			mListener.update(IUpdateListener.REMOVE_DIALOG, null);
			super.onPostExecute(_auid);
		}

	};	
	
	
	public class DeviceInfo{
		String m_pName;
		String m_pFeedId;
		String m_pProductName;
		String m_pActivationCode;

		
		public DeviceInfo(String _name, String _feedId, String _productName, String _activationCode)
		{
			this.m_pName = _name;
			this.m_pFeedId = _feedId;
			this.m_pActivationCode = _activationCode;
			this.m_pProductName = _productName;
		}
		
		public String getDeviceName()
		{
			return this.m_pName;
		}
		
		public String getFeedId()
		{
			return this.m_pFeedId;
		}
		
		public String getActivationCode()
		{
			return this.m_pActivationCode;
		}
		
		public String getProductname()
		{
			return this.m_pProductName;
		}
	}
}




