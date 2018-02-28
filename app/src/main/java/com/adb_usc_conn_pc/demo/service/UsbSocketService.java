package com.adb_usc_conn_pc.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.adb_usc_conn_pc.demo.ui.MainActivity;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;

/**
 * Created by waiting on 2017/8/15.
 */
public class UsbSocketService extends Service {

    //   private ServerSocket mServerSocket;
    //   private Socket mSocket;
    private static final int Port = 8768;
    private StartSocketThread mStartSocketThread;
    private SocketDataThread mSocketDataThread;

    //public static final String EndTag = "\n";
    public static final String ByteEndTag = "@END@";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        StartAcceptSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        StopAcceptSocket();
        StopDataSocket();
        super.onDestroy();
    }

    private void StartAcceptSocket() {
        if (mStartSocketThread == null) {
            mStartSocketThread = new StartSocketThread();
            mStartSocketThread.start();
        }
    }

    private void StopAcceptSocket() {
        if (mStartSocketThread != null) {
            mStartSocketThread.cancel();
            mStartSocketThread = null;
        }
    }

    private void StopDataSocket() {
        if (mSocketDataThread != null) {
            mSocketDataThread.IsRead = false;
            mSocketDataThread.cancel();
            mSocketDataThread = null;
        }
    }


    private class StartSocketThread extends Thread {
        private boolean IsListen = true;
        private ServerSocket mServerSocket;
        private Socket mSocket;

        public StartSocketThread() {
        }

        public void cancel() {
            try {
                IsListen = false;
                mServerSocket.close();
                mServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (IsListen) {
                    if (mServerSocket == null) {
                        mServerSocket = new ServerSocket(Port);
                    }
                    mSocket = mServerSocket.accept();

                    StopDataSocket();
                    mSocketDataThread = new SocketDataThread(mSocket);
                    mSocketDataThread.start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class SocketDataThread extends Thread {
        private Socket mmSocket;

        private static final int ReadOneMax = 2048;
        public boolean IsRead = true;

        public InputStream mInputStream;
        public OutputStream mOutputStream;

        // public BufferedReader mBufferedReader;
        public SocketDataThread(Socket socket) {
            try {
                mmSocket = socket;
                mInputStream = mmSocket.getInputStream();
                mOutputStream = mmSocket.getOutputStream();

                //   mBufferedReader = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
                mmSocket = null;
                mInputStream = null;
                mOutputStream = null;
            } catch (IOException localIOException) {
            }
        }

        public void WitaEndData() {
            try {
                if (mmSocket != null && mmSocket.isConnected() && mOutputStream != null) {
                    mOutputStream.write(ByteEndTag.getBytes("UTF-8"));
                    mOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void WiteData() {
            try {
                if (mmSocket != null && mmSocket.isConnected() && mOutputStream != null) {
                    String str = "1234567890";
                    mOutputStream.write(str.getBytes("UTF-8"));
                    mOutputStream.flush();

                    WitaEndData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            while (true) {
                try {
                    if (mInputStream != null) {
                        String content = "";
                        byte[] buffer = new byte[ReadOneMax];
                        int len = -1;
                        while (( len = mInputStream.read(buffer)) != -1) {
                            String str = new String(buffer,0,len,"UTF-8");
                            if (!str.contains(ByteEndTag)) {
                                content += str;
                            } else {
                                content += str;
                                String temp = content.replace(ByteEndTag, "");
                                Intent intent = new Intent(MainActivity.Test_Accept);
                                intent.putExtra("msg","msg:"+ temp);
                                sendBroadcast(intent);
                                content = "";
                                continue;
                            }
                        }
                        Intent intent = new Intent(MainActivity.Test_Accept);
                        intent.putExtra("msg", "与客户端断开连接");
                        sendBroadcast(intent);
                        StopDataSocket();

                    }

                } catch (Exception e) {

                }
            }
        }
    }

    @Subscriber(tag = "witedata")
    private void WiteTan(String str) {
        if (mSocketDataThread != null) {
            mSocketDataThread.WiteData();
        }
    }



}
