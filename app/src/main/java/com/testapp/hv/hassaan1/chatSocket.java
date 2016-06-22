package com.testapp.hv.hassaan1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class chatSocket extends AppCompatActivity {
    String server = "http://192.168.0.118:3000/";
    String name = "";
    private io.socket.client.Socket mSocket;

    {
        try {
            // mSocket = IO.socket("http://chat.socket.io");
            mSocket = IO.socket(server);
        } catch (URISyntaxException e) {

        }
    }

    private EditText mInputMessageView;
    private TextView tv;
    Button b1;

    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            return;
        }
        // mInputMessageView.setText("");
        mSocket.emit("new message", name, message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_socket);
        mInputMessageView = (EditText) findViewById(R.id.editText2);

        tv = (TextView) findViewById(R.id.tv);
        mSocket.on("new message", onNewMessage);
        mSocket.on(Socket.EVENT_CONNECT, onConnection);
        mSocket.on("disconnect", onDisconnect);
        mSocket.connect();
        b1 = (Button) findViewById(R.id.send);
        if (name.length() < 2) {
            name = makeid();
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSend();
            }
        });
    }

    int c = 0;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String username = args[0].toString();
                    String message = "|| HASSAAN ||" + c;
                    mInputMessageView.setText("");
/*
                    try {
                        JSONObject dat=new JSONObject(args[0].toString());
                        username = dat.getString("username");
                        message = dat.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
*/
                    try {
                        JSONArray arr = new JSONArray(args[0].toString());
                        for (int k = 0; k < arr.length(); k++) {
                            username = arr.get(k).toString();
                            JSONObject dat = new JSONObject(username.toString());
                            username = dat.getString("username");
                            message = dat.getString("message");
                        }
                        c += 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // add the message to view
                    addMessage(username, message);
                }
            });
        }
    };
    private Emitter.Listener onConnection = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInputMessageView.setText("");
                    try {
                        tv.append("\n\n\n\n\n Connected");
                    } catch (Exception e) {
                    }
                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mInputMessageView.setText("");
                    try {
                        tv.append("\n\n\n\n\n DisConnect");
                    } catch (Exception e) {
                    }
                }
            });
        }
    };

    private void addMessage(String username, String message) {
        tv.append("\n" + username + " : " + message);
    }

    private String makeid() {
        String text = "";
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < 5; i++) {
            text += possible.charAt((int) Math.floor(Math.random() * possible.length()));
        }
        return text;
    }
}
