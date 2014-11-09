package net.qiujuer.genius.nettool;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * Created by hobodong
 * on 2014/11/09.
 */
public class Hardware extends NetModel  {
    private static final Intent Intent = null;
	private Map<String,String> HardwareInfo;
    private Activity activity;
    private BroadcastReceiver batteryLevelRcvr;
	private IntentFilter batteryLevelFilter;
    /**
     * Domain name resolution test
     *
     * @param host Domain name address
     */
    public Hardware() {
      
    }


    public Hardware(Activity activity) {
		// TODO Auto-generated constructor stub
    	this.activity = activity;
    	this.HardwareInfo = new HashMap<String,String>();
	}


	private void monitorBatteryState() {
		
		batteryLevelFilter = new IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED);
		android.content.Intent intent = this.activity.registerReceiver(null, batteryLevelFilter);
		int rawlevel = intent.getIntExtra("level", -1);
		
		int scale = intent.getIntExtra("scale", -1);
		int status = intent.getIntExtra("status", -1);
		int health = intent.getIntExtra("health", -1);
		int level = -1; // percentage, or -1 for unknown
		if (rawlevel >= 0 && scale > 0) {
			level = (rawlevel * 100) / scale;
		}
		String batteryStatus = "";
		if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
			batteryStatus = "battery feels very hot!";
		} else {
			switch (status) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:
				batteryStatus = "no battery.";
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:
				
				if (level <= 33)
					batteryStatus = "charging, battery level is low";
				else if (level <= 84)
					batteryStatus = "charging";
				else
					batteryStatus = " will be fully charged.";
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				if (level == 0)
					batteryStatus = " needs charging right away.";
				else if (level > 0 && level <= 33)
					batteryStatus = "ready to be recharged, battery level is low";
				else
					batteryStatus = "not charging";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				batteryStatus = " is fully charged.";
				break;
			default:
				batteryStatus = "battery is indescribable!";
				break;
			}
		}
		HardwareInfo.put("Battery Level", String.valueOf(level));
		HardwareInfo.put("Battery status", batteryStatus);
		//this.activity.sendBroadcast(this.activity.getIntent());
		
		//this.activity.unregisterReceiver(batteryLevelRcvr);
		
	}

	private Map<String,String> getHardwareInfo() {
    	
    	monitorBatteryState();
        return HardwareInfo;
    }

	private void readSDCard() {
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED.equals(state)) {
		  File sdcardDir = Environment.getExternalStorageDirectory();
		  StatFs sf = new StatFs(sdcardDir.getPath());
		  long blockSize = sf.getBlockSize();
		  long blockCount = sf.getBlockCount();
		  long availCount = sf.getAvailableBlocks();
		  this.HardwareInfo.put("Total Space (SD Card)", (float)blockSize*blockCount/ 1024 /1024 /1024 + "GB");
		  this.HardwareInfo.put("Free Space (SD Card)", (float)availCount*blockSize/ 1024 /1024 /1024 + "GB");
		 }
	}
	
	private void readSystem() {
		 File root = Environment.getRootDirectory();
		 StatFs sf = new StatFs(root.getPath());
		 long blockSize = sf.getBlockSize();
		 long blockCount = sf.getBlockCount();
		 long availCount = sf.getAvailableBlocks();
		 
		 this.HardwareInfo.put("Total Space (Phone)", (float)blockSize*blockCount/ 1024 /1024 /1024 + "GB");
		 this.HardwareInfo.put("Free Space (Phone)", (float)availCount*blockSize/ 1024 /1024 /1024 + "GB");
		}
	
    @Override
    public void start() {
    	getHardwareInfo();
    	readSDCard();
    	readSystem();
        
    }

  

    
    

    @Override
    public String toString() {
        return this.HardwareInfo.toString();
    }


	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}