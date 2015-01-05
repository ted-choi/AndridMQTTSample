package com.cresprit.mqtt.ui;


public interface IUpdateListener
{
	public int SHOW_DIALOG  = 0;
	public int REMOVE_DIALOG = 1;
	
	void update(int status, String result);
}