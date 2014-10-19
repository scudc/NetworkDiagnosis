package net.qiujuer.genius.nettool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by QiuJU
 * on 2014/9/21.
 */
public class SimplePing implements Runnable {
    private final Object mEndLock = new Object();
    private boolean IsEnd = false;

    private int arrivedCount = 0;

    private int Count;
    private int TimeOut;
    private String Name;

    private int mEndCount;
    private String mIp = null;
    private float mLossRate = 1f;
    private float mDelay = 0;


    public SimplePing(String name, int count, int timeOut) {
        Count = mEndCount = count;
        TimeOut = timeOut;
        Name = name;
        for (int i = 0; i < mEndCount; i++) {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
        if (!IsEnd) {
            try {
                synchronized (mEndLock) {
                    mEndLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setEnd(boolean isArrived, long delay, String ip) {
        synchronized (mEndLock) {
            Count--;
            if (isArrived) {
                arrivedCount++;
                mDelay = (mDelay + delay) / 2f;
                if (ip != null)
                    mIp = ip;
            }
        }
        if (Count == 0)
            setEnd();
    }

    private void setEnd() {
        mLossRate = (mEndCount - arrivedCount) / mEndCount;

        IsEnd = true;
        synchronized (mEndLock) {
            mEndLock.notifyAll();
        }
    }

    @Override
    public void run() {
        long delay = 0;
        boolean isArrived = false;
        String ip = null;
        try {
            long startTime = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(Name);
            isArrived = address.isReachable(TimeOut);
            delay = System.currentTimeMillis() - startTime;
            ip = address.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setEnd(isArrived, delay, ip);
        }
    }

    public String getIp() {
        return mIp;
    }

    public float getLossRate() {
        return mLossRate;
    }

    public float getDelay() {
        return mDelay;
    }

    public boolean getIsSucceed() {
        return arrivedCount > 0;
    }
}
