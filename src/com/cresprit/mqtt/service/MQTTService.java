package com.cresprit.mqtt.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import com.cresprit.mqtt.ui.SubscribeCallback;
import com.cresprit.mqtt.manager.Connection;
import com.cresprit.mqtt.manager.ConnectionMgr;
import com.cresprit.mqtt.manager.DeviceListManager;
import com.cresprit.mqtt.manager.UserManager;


public class MQTTService extends Service {


    /* In a real application, you should get an Unique Client ID of the device and use this, see
    http://android-developers.blogspot.de/2011/03/identifying-app-installations.html */
    public static final String clientId = "android-client";
    
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private int mSelectedDevice=0;
    private DeviceListManager.DeviceInfo deviceInfo=null;
    Connection connect=null;
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
    		mSelectedDevice = intent.getByteExtra(DeviceListManager.SELECT_DEVICE, (byte) 0);
    		
        try {
            connect = ConnectionMgr.getConnectionInstance(ConnectionMgr.MQTT_SERVER_URL);
            connect.setUsername(UserManager.getInstance(this).getAuthKey());
            
            deviceInfo = DeviceListManager.getInstance(this).getDeviceList().get(mSelectedDevice);
            connect.subscribe(this, deviceInfo.getFeedId());

        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        connect.disconnect();
    }
}
