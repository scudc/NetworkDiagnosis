package net.qiujuer.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.qiujuer.genius.Genius;
import net.qiujuer.genius.app.UiModel;
import net.qiujuer.genius.app.UiTool;
import net.qiujuer.genius.command.Command;
import net.qiujuer.genius.nettool.DnsResolve;
import net.qiujuer.genius.nettool.Hardware;
import net.qiujuer.genius.nettool.Ping;
import net.qiujuer.genius.nettool.SpeedRoad;
import net.qiujuer.genius.nettool.Telnet;
import net.qiujuer.genius.nettool.TraceRoute;
import net.qiujuer.genius.util.FixedList;
import net.qiujuer.genius.util.HashUtils;
import net.qiujuer.genius.util.Log;
import net.qiujuer.genius.util.ToolUtils;

import java.util.ArrayList;
import java.util.List;


public class TestCaseActivity extends Activity {
    private static final String TAG = TestCaseActivity.class.getSimpleName();
    TextView mText = null;
    private String domain = null;
    private String port = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_case);

        mText = (TextView) findViewById(R.id.text);

        Bundle bundle=getIntent().getExtras();
        domain=bundle.getString("domain");
        port = bundle.getString("port");
        //添加回调
        Log.addCallbackListener(new Log.LogCallbackListener() {
            @Override
            public void onLogArrived(final Log data) {
                //异步显示到界面
                UiTool.asyncRunOnUiThread(TestCaseActivity.this, new UiModel() {
                    @Override
                    public void doUi() {
                        if (mText != null)
                            mText.append("\n" + data.getMsg());
                    }
                });
            }
        });

        //开始测试
        //testLog();
        //testHashUtils();
        //testToolUtils();
        //testFixedList();
        testNetTool();
        //testCommand();
    }

    @Override
    protected void onDestroy() {
        mText = null;
        super.onDestroy();
    }

    /**
     * 日志测试
     */
    public void testLog() {
        //是否调用系统Android Log，发布时可设置为false
        Log.setCallLog(true);

        //清理存储的文件
        Log.clearLogFile();

        //是否开启写入文件，存储最大文件数量，单个文件大小（Mb），重定向地址（默认包名目录）
        Log.setSaveLog(true, 10, 1, null);

        //设置是否监听外部存储插入操作
        //开启时插入外部设备（SD）时将拷贝存储的日志文件到外部存储设备
        //此操作依赖于是否开启写入文件功能，未开启则此方法无效
        //是否开启，SD卡目录
        Log.setCopyExternalStorage(true, "Test/Logs");

        //设置日志等级
        //VERBOSE为5到ERROR为1依次递减
        Log.setLevel(Log.ALL);

        Log.v(TAG, "测试日志 VERBOSE 级别。");
        Log.d(TAG, "测试日志 DEBUG 级别。");
        Log.i(TAG, "测试日志 INFO 级别。");
        Log.w(TAG, "测试日志 WARN 级别。");
        Log.e(TAG, "测试日志 ERROR 级别。");

        Log.setLevel(Log.INFO);
        Log.v(TAG, "二次测试日志 VERBOSE 级别。");
        Log.d(TAG, "二次测试日志 DEBUG 级别。");
        Log.i(TAG, "二次测试日志 INFO 级别。");
        Log.w(TAG, "二次测试日志 WARN 级别。");
        Log.e(TAG, "二次测试日志 ERROR 级别。");

        Log.setLevel(Log.ALL);
    }

    /**
     * 测试MD5
     */
    public void testHashUtils() {
        Log.i(TAG, "HashUtils：QIUJUER的MD5值为：" + HashUtils.getStringMd5("QIUJUER"));
        //文件MD5不做演示，传入file类即可
    }

    /**
     * 测试工具类
     */
    public void testToolUtils() {
        Log.i(TAG, "ToolUtils：getAndroidId：" + ToolUtils.getAndroidId(Genius.getApplication()));
        Log.i(TAG, "ToolUtils：getDeviceId：" + ToolUtils.getDeviceId(Genius.getApplication()));
        Log.i(TAG, "ToolUtils：getSerialNumber：" + ToolUtils.getSerialNumber());
        Log.i(TAG, "ToolUtils：isAvailablePackage(net.qiujuer.sample)：" + ToolUtils.isAvailablePackage(Genius.getApplication(), "net.qiujuer.sample"));
    }

    /**
     * 测试固定长度队列
     */
    public void testFixedList() {
        //初始化最大长度为5
        FixedList<Integer> list = new FixedList<Integer>(5);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //添加4个元素
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //继续追加2个
        list.add(5);
        list.add(6);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //调整最大长度
        list.setMaxSize(6);
        list.add(7);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        list.add(8);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //缩小长度，自动删除前面多余部分
        list.setMaxSize(3);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        list.add(9);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //添加一个列表进去，自动删除多余部分
        List<Integer> addList = new ArrayList<Integer>();
        addList.add(10);
        addList.add(11);
        addList.add(12);
        addList.add(13);
        list.addAll(addList);
        Log.i(TAG, "FixedSizeList:AddList:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //采用poll方式弹出元素
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        //末尾插入元素与add一样
        list.addLast(14);
        list.addLast(15);
        list.addLast(16);
        list.addLast(17);
        list.addLast(18);
        Log.i(TAG, "FixedSizeList:AddLast:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //从头部插入，默认删除尾部超出部分
        list.addFirst(19);
        list.addFirst(20);
        Log.i(TAG, "FixedSizeList:AddFirst:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //Remove与poll类似不过不返回删除元素，只会删除一个
        list.remove();
        Log.i(TAG, "FixedSizeList:Remove:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //清空操作
        list.clear();
        Log.i(TAG, "FixedSizeList:Clear:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());

        //使用List操作,最大长度2
        List<Integer> list1 = new FixedList<Integer>(2);
        list1.add(1);
        list1.add(2);
        Log.i(TAG, "FixedSizeList:List:" + " " + list1.size() + " ," + list1.toString());
        list1.add(3);
        Log.i(TAG, "FixedSizeList:List:" + " " + list1.size() + " ," + list1.toString());
        list1.add(4);
        Log.i(TAG, "FixedSizeList:List:" + " " + list1.size() + " ," + list1.toString());
        list1.clear();
        Log.i(TAG, "FixedSizeList:List:" + " " + list1.size() + " ," + list1.toString());
    }

    /**
     * 测试命令行执行
     */
    public void testCommand() {
        //同步
        Thread thread = new Thread() {
            @Override
			public void run() {
                //调用方式与ProcessBuilder传参方式一样
                Command command = new Command("/system/bin/ping",
                        "-c", "4", "-s", "100",
                        "www.baidu.com");
                //同步方式执行
                String res = Command.command(command);
                Log.i(TAG, "\n\nCommand 同步：" + res);
            }
        };
        thread.setDaemon(true);
        thread.start();

        //异步
        Command command = new Command("/system/bin/ping",
                "-c", "4", "-s", "100",
                "www.baidu.com");

        //异步方式执行
        //采用回调方式，无需自己建立线程
        //传入回调后自动采用此种方式
        Command.command(command, new Command.CommandListener() {
            @Override
            public void onCompleted(String str) {
                Log.i(TAG, "\n\nCommand 异步 onCompleted：\n" + str);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "\n\nCommand 异步 onCancel");
            }

            @Override
            public void onError() {
                Log.i(TAG, "\n\nCommand 异步 onError");
            }
        });
    }


    /**
     * 基本网络功能测试
     */
    public void testNetTool() {
 
    	
    	
        //所有目标都可为IP地址
        Thread thread = new Thread() {
            @Override
			public void run() {
               	
            	Hardware hardware = new Hardware(TestCaseActivity.this);
            	hardware.start();
            	System.out.println(hardware.toString());
            	
            	/*
            	
                //包数，包大小，目标，是否解析IP
                Ping ping = new Ping(4, 32, domain, true);
                ping.start();
                Log.i(TAG, "Ping 测试：\n" + ping.toString());
                //目标，可指定解析服务器
                DnsResolve dns = new DnsResolve(domain);
                dns.start();
                Log.i(TAG, "Dns解析：\n" + dns.toString());
                //目标，端口
                Telnet telnet = new Telnet(domain, Integer.parseInt(port));
                telnet.start();
                Log.i(TAG, "端口探测：\n" + telnet.toString());
                //目标
                TraceRoute traceRoute = new TraceRoute(domain);
                traceRoute.start();
                Log.i(TAG, "\n\nTraceRoute 信息：\n" + traceRoute.toString());
                //测速
                //下载目标，下载大小
                SpeedRoad speedRoad = new SpeedRoad("http://down.360safe.com/se/360se_setup.exe", 1024 * 32);
                speedRoad.start();
                Log.i(TAG, "下载速度：\n" + speedRoad.getSpeed());
                */
                
            	
            }
        };
        thread.setDaemon(true);
        thread.start();
        
    }
}
