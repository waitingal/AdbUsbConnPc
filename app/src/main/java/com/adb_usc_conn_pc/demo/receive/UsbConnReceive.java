package com.adb_usc_conn_pc.demo.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.adb_usc_conn_pc.demo.service.UsbSocketService;

/**
 * Created by waiting on 2017/8/15.
 */
public class UsbConnReceive extends BroadcastReceiver {

    public static final String USB_STATE_ACTION="android.hardware.usb.action.USB_STATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null){
            return;
        }
         String action=intent.getAction();
        if(action!=null && action.equals(USB_STATE_ACTION)){
              boolean flag=intent.getBooleanExtra("connected",false);
            if(flag){
                context.startService(new Intent(context, UsbSocketService.class));
                Toast.makeText(context,"usb--插入",Toast.LENGTH_SHORT).show();
            }else{
                context.stopService(new Intent(context, UsbSocketService.class));
                Toast.makeText(context,"usb--拨出",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
