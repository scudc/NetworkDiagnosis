package net.qiujuer.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.Genius;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Genius.initialize(getApplication());
    }

    @Override
    protected void onDestroy() {
        Genius.dispose();
        super.onDestroy();
    }

    public void onClick(View view) {
    	EditText domainView = (EditText) findViewById(R.id.domainName);
    	EditText portView = (EditText) findViewById(R.id.portName);
    	String domain = (String) domainView.getText().toString();
    	String port = (String) portView.getText().toString();
    	Bundle bundle=new Bundle();
    	bundle.putString("domain", domain);
    	bundle.putString("port",port);
    	
        if (view.getId() == R.id.MaterialButton)
            startActivity(new Intent(this, MaterialActivity.class));
        else
        {
        	Intent it = new Intent(this, TestCaseActivity.class);
        	it.putExtras(bundle);
            startActivity(it);
        }
    }
}
