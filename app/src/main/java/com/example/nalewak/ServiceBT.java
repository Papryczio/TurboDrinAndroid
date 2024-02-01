package com.example.nalewak;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.nalewak.ui.main.Control;
import com.example.nalewak.ui.main.ManualControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

public class ServiceBT extends Service {
    //log.d tag
    private static final String TAG = "serviceBT";

    //bluetooth connection related
    private BluetoothDevice mDevice;
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private boolean mIsBluetoothConnected = false;
    private ReadInput mReadThread = null;

    public ServiceBT() {
        Log.d(TAG, "ServiceBT constructor");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    public void onCreate() {
        Log.d(TAG, "onCreate");
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        //getting extras from mainActivity
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));
        Log.d(TAG, "Ready");

        //open bluetooth connection
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ServiceBT.ConnectBT().execute();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {

        }

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... devices) {
            Log.d(TAG, "doInBackground");
            //Trying to connect to device
            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                //Unable to connect to device
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Connection attempt failed", Toast.LENGTH_LONG).show();
            } else {
                mIsBluetoothConnected = true;
                Toast.makeText(getApplicationContext(), "Bluetooth connected", Toast.LENGTH_LONG).show();
                mReadThread = new ReadInput();
            }

        }
    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            Toast.makeText(getApplicationContext(), "Bluetooth disconnected", Toast.LENGTH_LONG);
        }

    }

    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private final Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;
            OutputStream outputStream;
            try {
                inputStream = mBTSocket.getInputStream();
                outputStream = mBTSocket.getOutputStream();

                Log.d(TAG, "Checking bluetooth input...");
                while (!bStop) {
                    byte[] buffer = new byte[256];

                    // Read data
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        String line = new String(buffer);
                        line = line.replace("\0", "");
                        Control.requestCallback = line;
                        ManualControl.requestCallback = line;
                        Log.d(TAG, line);
                    }

                    // Write data
                    Control.requests.forEach(request -> {
                        try {
                            outputStream.write(request.getBytes());
                            Control.requests.remove(request);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    ManualControl.requests.forEach(request -> {
                        try {
                            outputStream.write(request.getBytes());
                            ManualControl.requests.remove(request);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }

    }

}