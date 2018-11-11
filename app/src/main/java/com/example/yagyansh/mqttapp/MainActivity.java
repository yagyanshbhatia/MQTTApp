package com.example.yagyansh.mqttapp;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    MqttAndroidClient client;
    EditText textBox;
    TextView subText;
    TextView slot;
    Vibrator vibrator;
    String topic = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subText = (TextView)findViewById(R.id.subText);
        slot = (TextView)findViewById(R.id.slot);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//        textBox = findViewById(R.id.editText);
//        String clientId = MqttClient.generateClientId();
        String message = readFromFile(getApplicationContext());
        String clientId = message.split(" ")[0];
        topic = "student/" + message.split(" ")[0];
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://192.168.0.110:1883", clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
//                    Log.d(TAG, "onSuccess");
                    Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_LONG).show();
                    setSubsciption();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d(TAG, "onFailure");
                    Toast.makeText(MainActivity.this, "connected fail", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                subText.setText("connection lost :(");
                vibrator.vibrate(500);  
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                updateSlot(message.toString());
                subText.setText(new String(message.getPayload()));
                vibrator.vibrate(500);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }


    private void updateSlot(String message){
        if (message.split(" ").length == 4){
            slot.setText(message.split(" ")[3]);
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("credentials.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void pub(View v){
        Toast.makeText(MainActivity.this, topic, Toast.LENGTH_LONG).show();
        String message = readFromFile(getApplicationContext());
        topic = "student/" + message.split(" ")[0];
        try {
            client.publish("parking", message.getBytes(), 1, false);
        } catch (MqttException e) {
            Toast.makeText(MainActivity.this, "Message not sent", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void setSubsciption(){
        try{
                client.subscribe(topic, 1);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void conn(View v){
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
//                    Log.d(TAG, "onSuccess");
                    Toast.makeText(MainActivity.this, "connected", Toast.LENGTH_LONG).show();
                    setSubsciption();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d(TAG, "onFailure");
                    Toast.makeText(MainActivity.this, "connected fail", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void disconn(View v){
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "disconnected", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this, "could not disconnect", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
