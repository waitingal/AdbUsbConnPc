package com.adb_usc_conn_pc.demo.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.adb_usc_conn_pc.demo.service.UsbSocketService;

/**
 * Created by waiting on 2017/8/15.
 */
public class SYTReceive extends BroadcastReceiver {

    public static final String StarService="com.adb_usc_conn_pc.demo.SYTReceive.StartService";
    public static final String StopService="com.adb_usc_conn_pc.demo.SYTReceive.StopService";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null){
            return;
        }
        String action = intent.getAction();
        if(action == null){
            return;
        }

        if(action.equals(StarService)){
            context.startService(new Intent(context, UsbSocketService.class));
        }else if(action.equals(StopService)){
            context.stopService(new Intent(context, UsbSocketService.class));
        }
    }
}
