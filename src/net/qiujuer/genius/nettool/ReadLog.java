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
 * on 2014/11/09. ͨ����־�ļ�·��ȥ����־������ȡ1024Kb�����ݣ�������ݳ���Ĭ�ϻ�ȡ���µ�1024Kb������
 */
public class ReadLog extends NetModel  {
	private String logInfo;
    private String logFileDir;
    /**
     * ͨ����־�ļ�·��ȥ����־������ȡ1024Kb�����ݣ�������ݳ���Ĭ�ϻ�ȡ���µ�1024Kb������
     *
     * @param ��־·��
     */
    public ReadLog(String logFileDir) {
    	this.logFileDir = logFileDir;    
    }


	private void readLog() {
		
		//to do
		
	}

	
	
    @Override
    public void start() {

    }

  

    
    

    @Override
    public String toString() {
        return this.logInfo;
    }


	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		
	}
}