package net.qiujuer.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.qiujuer.genius.Genius;
import net.qiujuer.genius.app.UiModel;
import net.qiujuer.genius.app.UiTool;
import net.qiujuer.genius.command.Command;
import net.qiujuer.genius.nettool.DnsResolve;
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
        //��ӻص�
        Log.addCallbackListener(new Log.LogCallbackListener() {
            @Override
            public void onLogArrived(final Log data) {
                //�첽��ʾ������
                UiTool.asyncRunOnUiThread(TestCaseActivity.this, new UiModel() {
                    @Override
                    public void doUi() {
                        if (mText != null)
                            mText.append("\n" + data.getMsg());
                    }
                });
            }
        });

        //��ʼ����
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
     * ��־����
     */
    public void testLog() {
        //�Ƿ����ϵͳAndroid Log������ʱ������Ϊfalse
        Log.setCallLog(true);

        //����洢���ļ�
        Log.clearLogFile();

        //�Ƿ���д���ļ����洢����ļ������������ļ���С��Mb�����ض����ַ��Ĭ�ϰ���Ŀ¼��
        Log.setSaveLog(true, 10, 1, null);

        //�����Ƿ�����ⲿ�洢�������
        //����ʱ�����ⲿ�豸��SD��ʱ�������洢����־�ļ����ⲿ�洢�豸
        //�˲����������Ƿ���д���ļ����ܣ�δ������˷�����Ч
        //�Ƿ�����SD��Ŀ¼
        Log.setCopyExternalStorage(true, "Test/Logs");

        //������־�ȼ�
        //VERBOSEΪ5��ERRORΪ1���εݼ�
        Log.setLevel(Log.ALL);

        Log.v(TAG, "������־ VERBOSE ����");
        Log.d(TAG, "������־ DEBUG ����");
        Log.i(TAG, "������־ INFO ����");
        Log.w(TAG, "������־ WARN ����");
        Log.e(TAG, "������־ ERROR ����");

        Log.setLevel(Log.INFO);
        Log.v(TAG, "���β�����־ VERBOSE ����");
        Log.d(TAG, "���β�����־ DEBUG ����");
        Log.i(TAG, "���β�����־ INFO ����");
        Log.w(TAG, "���β�����־ WARN ����");
        Log.e(TAG, "���β�����־ ERROR ����");

        Log.setLevel(Log.ALL);
    }

    /**
     * ����MD5
     */
    public void testHashUtils() {
        Log.i(TAG, "HashUtils��QIUJUER��MD5ֵΪ��" + HashUtils.getStringMd5("QIUJUER"));
        //�ļ�MD5������ʾ������file�༴��
    }

    /**
     * ���Թ�����
     */
    public void testToolUtils() {
        Log.i(TAG, "ToolUtils��getAndroidId��" + ToolUtils.getAndroidId(Genius.getApplication()));
        Log.i(TAG, "ToolUtils��getDeviceId��" + ToolUtils.getDeviceId(Genius.getApplication()));
        Log.i(TAG, "ToolUtils��getSerialNumber��" + ToolUtils.getSerialNumber());
        Log.i(TAG, "ToolUtils��isAvailablePackage(net.qiujuer.sample)��" + ToolUtils.isAvailablePackage(Genius.getApplication(), "net.qiujuer.sample"));
    }

    /**
     * ���Թ̶����ȶ���
     */
    public void testFixedList() {
        //��ʼ����󳤶�Ϊ5
        FixedList<Integer> list = new FixedList<Integer>(5);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //���4��Ԫ��
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //����׷��2��
        list.add(5);
        list.add(6);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //������󳤶�
        list.setMaxSize(6);
        list.add(7);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        list.add(8);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //��С���ȣ��Զ�ɾ��ǰ����ಿ��
        list.setMaxSize(3);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        list.add(9);
        Log.i(TAG, "FixedSizeList:" + list.size() + " ," + list.getMaxSize());
        //���һ���б��ȥ���Զ�ɾ�����ಿ��
        List<Integer> addList = new ArrayList<Integer>();
        addList.add(10);
        addList.add(11);
        addList.add(12);
        addList.add(13);
        list.addAll(addList);
        Log.i(TAG, "FixedSizeList:AddList:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //����poll��ʽ����Ԫ��
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        Log.i(TAG, "FixedSizeList:Poll:[" + list.poll() + "] " + list.size() + " ," + list.getMaxSize());
        //ĩβ����Ԫ����addһ��
        list.addLast(14);
        list.addLast(15);
        list.addLast(16);
        list.addLast(17);
        list.addLast(18);
        Log.i(TAG, "FixedSizeList:AddLast:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //��ͷ�����룬Ĭ��ɾ��β����������
        list.addFirst(19);
        list.addFirst(20);
        Log.i(TAG, "FixedSizeList:AddFirst:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //Remove��poll���Ʋ���������ɾ��Ԫ�أ�ֻ��ɾ��һ��
        list.remove();
        Log.i(TAG, "FixedSizeList:Remove:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());
        //��ղ���
        list.clear();
        Log.i(TAG, "FixedSizeList:Clear:" + list.toString() + " " + list.size() + " ," + list.getMaxSize());

        //ʹ��List����,��󳤶�2
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
     * ����������ִ��
     */
    public void testCommand() {
        //ͬ��
        Thread thread = new Thread() {
            @Override
			public void run() {
                //���÷�ʽ��ProcessBuilder���η�ʽһ��
                Command command = new Command("/system/bin/ping",
                        "-c", "4", "-s", "100",
                        "www.baidu.com");
                //ͬ����ʽִ��
                String res = Command.command(command);
                Log.i(TAG, "\n\nCommand ͬ����" + res);
            }
        };
        thread.setDaemon(true);
        thread.start();

        //�첽
        Command command = new Command("/system/bin/ping",
                "-c", "4", "-s", "100",
                "www.baidu.com");

        //�첽��ʽִ��
        //���ûص���ʽ�������Լ������߳�
        //����ص����Զ����ô��ַ�ʽ
        Command.command(command, new Command.CommandListener() {
            @Override
            public void onCompleted(String str) {
                Log.i(TAG, "\n\nCommand �첽 onCompleted��\n" + str);
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "\n\nCommand �첽 onCancel");
            }

            @Override
            public void onError() {
                Log.i(TAG, "\n\nCommand �첽 onError");
            }
        });
    }


    /**
     * �������繦�ܲ���
     */
    public void testNetTool() {
        //����Ŀ�궼��ΪIP��ַ
        Thread thread = new Thread() {
            @Override
			public void run() {
                //����������С��Ŀ�꣬�Ƿ����IP
                Ping ping = new Ping(4, 32, domain, true);
                ping.start();
                Log.i(TAG, "Ping ���ԣ�\n" + ping.toString());
                //Ŀ�꣬��ָ������������
                DnsResolve dns = new DnsResolve(domain);
                dns.start();
                Log.i(TAG, "Dns������\n" + dns.toString());
                //Ŀ�꣬�˿�
                Telnet telnet = new Telnet(domain, Integer.parseInt(port));
                telnet.start();
                Log.i(TAG, "�˿�̽�⣺\n" + telnet.toString());
                //Ŀ��
                TraceRoute traceRoute = new TraceRoute(domain);
                traceRoute.start();
                Log.i(TAG, "\n\nTraceRoute ��Ϣ��\n" + traceRoute.toString());
                //����
                //����Ŀ�꣬���ش�С
                SpeedRoad speedRoad = new SpeedRoad("http://down.360safe.com/se/360se_setup.exe", 1024 * 32);
                speedRoad.start();
                Log.i(TAG, "�����ٶȣ�\n" + speedRoad.getSpeed());
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
