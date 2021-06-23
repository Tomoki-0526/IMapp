package com.example.course29.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.course29.MainActivity;
import com.example.course29.R;
import com.example.course29.chat.chatContent.ChatContent;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.content.Context.NOTIFICATION_SERVICE;

class WsListener extends WebSocketListener {

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        Log.e("tag","连接失败");
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
//        Activity currentActivity = getCurrentActivity();
//        String str1 = currentActivity.getLocalClassName();
//        String str2 =currentActivity.getPackageName();
//        Log.e("str1",str1);
//        Log.e("str",str2);
//        if(currentActivity==null) {Log.e("ttt","ee");}
//        Log.e("eq",String.valueOf(currentActivity.equals(ChatContent.class)));
        Log.e("tag", "客户端收到消息:" + text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.e("tag", "客户端收到消息:");
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        Log.e("tag","连接成功！");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",GlobalVariable.getGlobalUsername());
            jsonObject.put("password",GlobalVariable.getGlobalPassword());
            jsonObject.put("bizType","USER_LOGIN");
            webSocket.send(jsonObject.toString());
            Log.e("tag","登录成功");
        } catch (JSONException e) {
            Log.e("tag","登录失败！");
            e.printStackTrace();
        }

    }
//    public static Activity getCurrentActivity () {
//        try {
//            Class activityThreadClass = Class.forName("android.app.ActivityThread");
//            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
//                    null);
//            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
//            activitiesField.setAccessible(true);
//            Map activities = (Map) activitiesField.get(activityThread);
//            for (Object activityRecord : activities.values()) {
//                Class activityRecordClass = activityRecord.getClass();
//                Field pausedField = activityRecordClass.getDeclaredField("paused");
//                pausedField.setAccessible(true);
//                if (!pausedField.getBoolean(activityRecord)) {
//                    Field activityField = activityRecordClass.getDeclaredField("activity");
//                    activityField.setAccessible(true);
//                    Activity activity = (Activity) activityField.get(activityRecord);
//                    return activity;
//                }
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}



//
//    //发送String消息
//    public void send(final String message) {
//        if (mWebSocket != null) {
//            mWebSocket.send(message);
//        }
//    }
//
//    //发送byte消息
//    public void send(final ByteString message) {
//        if (mWebSocket != null) {
//            mWebSocket.send(message);
//        }
//    }
//
//    //主动断开连接
//    public void disconnect(int code, String reason) {
//        if (mWebSocket != null)
//            mWebSocket.close(code, reason);
//    }


