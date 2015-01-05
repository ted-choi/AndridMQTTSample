package com.cresprit.mqtt.manager;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.String;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.cresprit.mqtt.ui.MessageAdapter;

public class ConnectionMgr{
	public static final String MQTT_SERVER_URL = "tcp://api.alooh.io:1883";
	public static final String SERVER_API_LOGIN_URL = "http://api.alooh.io:50001/api/v1/session";
	public static final String SERVER_API_CREATE_USER_URL = "http://api.alooh.io:50001/api/v1/user/new";
	public static final String SERVER_API_RESET_USER_URL = "http://api.alooh.io:50001/api/v1/user/reset";
	public static final String SERVER_API_REGIST_DEVICE_NAME_CHECK_URL = "http://api.alooh.io:50001/api/v1/devices/check";
	public static final String SERVER_API_REGIST_DEVICE_AUID_URL = "http://api.alooh.io:50001/api/v1/products/542271621846451c43bae192/device/new";	
	public static final String SERVER_API_GET_DEVICE_LIST_URL = "http://api.alooh.io:50001/api/v1/devices/name/list";	
    
	static Connection connection;
	
	private static ArrayList<String> list;
	private static ArrayList<String> date;
	public static String MSG_RECEIVE = "com.cresprit.mqtt.ConnectionMgr.receive_message";
	private static MessageAdapter adapter;
	private Context mContext= null;
	
	public static Connection getConnectionInstance(String serverUri)
	{
		connection = new Connection(serverUri);
	
		if(list == null)
			list = new ArrayList();
		if(date == null)
			date = new ArrayList();
		return connection;
	}


	public static ArrayList<String> getMessages()
	{
		return list;
	}
	
	public static ArrayList<String> getDates()
	{
		return date;
	}
	
	public static MessageAdapter getAdapter()
	{
		return adapter;
	}
	
	public static void setAdapter(MessageAdapter _adapter)
	{
		adapter = _adapter;
	}
}