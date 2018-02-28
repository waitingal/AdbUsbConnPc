package com.adb_usc_conn_pc.demo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.adb_usc_conn_pc.demo.R;
import org.simple.eventbus.EventBus;

/**
 * Created by waiting on 2017/8/15.
 */
public class MainActivity extends AppCompatActivity {

    TextView text;

    public static final String Test_Accept="AcceptMsg";
    public static final String Test_Send="SendMsg";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
        text = (TextView)findViewById(R.id.text);

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Test_Accept);
        intentFilter.addAction(Test_Send);
        registerReceiver(MyTestBroadcastReceiver,intentFilter);

        findViewById(R.id.test_wi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("","witedata");
            }
        });

        findViewById(R.id.cancel_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(MyTestBroadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver MyTestBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent == null){
                return;
            }
            String action=intent.getAction();
            if(action==null){
                return;
            }
            if(action.equals(Test_Accept)){
                Log.e("","Test_Accept-----");
                String msg=intent.getStringExtra("msg");
                text.setText(text.getText().toString()+"\n"+msg);
            }if(action.equals(Test_Send)){
                Log.e("","Test_Send-----");
                text.setText(text.getText().toString()+"\n"+"已向客户端 发送一个测试数据");
            }
        }
    };
}
