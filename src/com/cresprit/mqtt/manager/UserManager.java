package com.cresprit.mqtt.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.cresprit.mqtt.ui.IUpdateListener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class UserManager{
	private static final String TAG = "UserManager";
	private static final String PROPERTY_USER_ID = "userid";
	private static final String PROPERTY_PASSWORD = "password";
	private static final String JSON_ID_EMAIL = "email";
	private static final String JSON_ID_PASSWORD = "password";
	private static final String JSON_ID_DATA = "data";
	private static final String JSON_ID_KEY = "key";
	private static final String HEADER_TYPE = "Content-Type";
	private static final String HEADER_APPLICATION_JSON = "application/json";
	private static String m_pId;
	private String m_pAuthKey;
	private static UserManager __instance;
	private static SharedPreferences mSharedPreferences;
	private IUpdateListener mListener = null;
	String m_pPasswd;
	Context context;
	
	public static UserManager getInstance(Context appContext) {
		if (__instance == null) {
			__instance = new UserManager(appContext);
			
		}

		if(mSharedPreferences == null)
			mSharedPreferences = appContext.getSharedPreferences(appContext.getPackageName(), Context.MODE_PRIVATE);
		return __instance;
	}
	
	public UserManager(Context ctx)
	{
		this.context=ctx;
	}	
	
	public UserManager()
	{
		
	}
	
	public UserManager(String _id, String _passwd){
		m_pId = _id;
		this.m_pPasswd = _passwd;
	}
	
	public void setAuthKey(String _key)
	{
		m_pAuthKey = _key;
	}
	
	public String getAuthKey()
	{
		return m_pAuthKey;
	}
	
	public void setDialogUpdateListener(IUpdateListener _listener)
	{
		mListener = _listener;
	}
	
	public String getUserId()
	{
		m_pId = getString( PROPERTY_USER_ID, "" );
		return m_pId;
	}
	
	public void setUserId(String _id)
	{
		m_pId = _id;
		setString(PROPERTY_USER_ID, _id);
	}	
	
	public String getPassword()
	{
		m_pPasswd = getString( PROPERTY_PASSWORD, "" ); 
		return m_pPasswd;
	}
	
	public void setPassword(String _password)
	{
		m_pPasswd = _password;
		setString(PROPERTY_PASSWORD, _password);
	}
	
	public String getString(String key, String defaultValue )
	{
		if( mSharedPreferences == null )
			return defaultValue;

		return mSharedPreferences.getString(key, defaultValue);
		
	}
	
	public void setString(String key, String value)
	{
		if( mSharedPreferences == null )
			return;
		
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);

		editor.commit();
	}
	
	
	public void doLogin()
	{
		LoginTask loginTask = new LoginTask();
		loginTask.execute("");
	}
	
	class LoginTask extends AsyncTask<String, Void, String> {
		
		String key=null;
		int responseCode = 0;
		
		@Override
		protected String doInBackground(String... params1) {
			JSONObject data = new JSONObject();
			JSONObject json = new JSONObject();
			
			try {
				data.put(JSON_ID_EMAIL	, m_pId);
				data.put(JSON_ID_PASSWORD, m_pPasswd);
				json.put(JSON_ID_DATA, data);				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpClient client = new DefaultHttpClient();
			final HttpParams params = client.getParams();
			
			HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
			HttpConnectionParams.setSoTimeout(params, 30 * 1000);
	
			HttpPost request = new HttpPost();
			request.setHeader(HEADER_TYPE, HEADER_APPLICATION_JSON);
	
			try {
				request.setURI(new URI(ConnectionMgr.SERVER_API_LOGIN_URL));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			try {
				request.setEntity(new StringEntity(json.toString(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				HttpResponse response = client.execute(request);
	
				Log.i(TAG, "get http response: STATUS_CODE: " + response.getStatusLine().getStatusCode()+response.getStatusLine().getReasonPhrase());
				responseCode = response.getStatusLine().getStatusCode();
				if(responseCode == 406 || responseCode == 401 || responseCode == 404 || responseCode == 500)
				{
					key = Integer.toString(responseCode);			
					return key;
				}
				
				HttpEntity entity = response.getEntity();
				String jsonStr = EntityUtils.toString(entity);
				try {
					JSONObject resObj = new JSONObject(jsonStr);
					JSONObject resData = resObj.getJSONObject(JSON_ID_DATA);
					Log.i(TAG,"resData : "+resData.toString());
					key = resData.getString(JSON_ID_KEY);
	
					Log.i(TAG,"key : "+key);
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
			return key;	
		}

		@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				if(mListener != null)
					mListener.update(IUpdateListener.SHOW_DIALOG, null);
				super.onPreExecute();
		}
	
		@Override
		protected void onPostExecute(String _key) {
			if(mListener != null)
				mListener.update(IUpdateListener.REMOVE_DIALOG, _key);
			super.onPostExecute(key);
		}
	}
}