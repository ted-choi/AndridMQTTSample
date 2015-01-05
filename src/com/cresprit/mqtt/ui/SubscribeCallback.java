package com.cresprit.mqtt.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import com.cresprit.mqtt.manager.ConnectionMgr;


public class SubscribeCallback implements MqttCallback {

    private Context context;

    public SubscribeCallback(Context context) {

        this.context = context;
    }

    @Override
    public void connectionLost(Throwable cause) {
        //We should reconnect here
    }

    @Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
    	String date;

        ConnectionMgr.getMessages().add(new String(message.getPayload()));

		Intent intent = new Intent(ConnectionMgr.MSG_RECEIVE);
		context.sendBroadcast(intent);
    }

    @Override
    public void deliveryComplete(MqttDeliveryToken token) {
        //We do not need this because we do not publish
    }
}
