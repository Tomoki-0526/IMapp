package com.example.course29;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketService extends Service {
    WebSocket mWebSocket;
    private InnerIBinder mBinder = new InnerIBinder();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化websocket
        getWebSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mWebSocket.close(1005,"");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class InnerIBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    private void getWebSocket () {
        Request request = new Request.Builder()
                .url("ws://8.140.133.34:7562/ws")
                .build();
        new Thread() {
            @Override
            public void run() {

                //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
                mWebSocket = HttpUtil.client.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                        super.onClosed(webSocket, code, reason);
                    }

                    @Override
                    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                        super.onClosing(webSocket, code, reason);
                    }

                    @Override
                    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
                        super.onFailure(webSocket, t, response);
                        Log.e("tag", "连接失败");
                    }

                    @Override
                    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                        super.onMessage(webSocket, text);
                        Log.e("tag", "客户端收到消息:" + text);
                        Intent intent = new Intent();
                        intent.setAction("CCTV5");
                        intent.putExtra("message", text);
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                        super.onMessage(webSocket, bytes);

                    }

                    @Override
                    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                        super.onOpen(webSocket, response);

                        super.onOpen(webSocket, response);
                        Log.e("tag", "连接成功！");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("username", GlobalVariable.getGlobalUsername());
                            jsonObject.put("password", GlobalVariable.getGlobalPassword());
                            jsonObject.put("bizType", "USER_LOGIN");
                            webSocket.send(jsonObject.toString());
                            Log.e("tag", "登录成功");
                        } catch (JSONException e) {
                            Log.e("tag", "登录失败！");
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();

    }


//    private void connect() {
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    //connectBlocking多出一个等待操作，会先连接再发送，否则未连接发送会报错
//                    client.connectBlocking();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

//    private void closeConnect() {
//        try {
//            if (null != client) {
//                client.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            client = null;
//        }
//    }
}
