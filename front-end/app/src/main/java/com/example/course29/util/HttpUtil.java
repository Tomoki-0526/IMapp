package com.example.course29.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static String headUrl = "http://8.140.133.34:7563";
//    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    //获取OkHttpClient对象 且 cookie管理
    private static OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar()
            {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                //这里可以做cookie传递，保存等操作
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
                {//可以做保存cookies操作
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url)
                {//加载新的cookies
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();
    public static Map get(String url, Context context){

        //构建Request对象
        Request request = new Request.Builder()
                .url(headUrl.concat(url))
                .get()
                .build();

        //构建call对象
        Call call = client.newCall(request);

        //异步get请求
        final Map[] ans = {new HashMap()};
        final CountDownLatch latch = new CountDownLatch(1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getFail","failed");
                Looper.prepare();
                ToastUtil.showMsg(context, "Connection failed");
                Looper.loop();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final Map res = JsonMapUtil.getMap(response.body().string());
                Log.e("res", String.valueOf(res));
                ans[0] = res;
                latch.countDown();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        contentTv.setText(res);
//                    }
//                });
            }
        });
        try {
            latch.await();
        }
        catch (InterruptedException e) {
        }
        return ans[0];
    }

    public static Map post (String url, Map params, Context context) {

        //创建RequestBody
        String jsonStr = JsonMapUtil.getJson(params);
        RequestBody requestBody =  RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                , jsonStr);
        Log.e("jsonstr",jsonStr);
        //构建Request对象
        Request request = new Request.Builder()
                .url(headUrl.concat(url))
                .post(requestBody)
                .build();

        //构建call对象
        Call call = client.newCall(request);

        //异步post请求
        final Map[] ans = {new HashMap()};
        final CountDownLatch latch = new CountDownLatch(1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("postFail","failed");
                Looper.prepare();
                ToastUtil.showMsg(context, "Connection failed");
                Looper.loop();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final Map res = JsonMapUtil.getMap(response.body().string());
                Log.e("res", String.valueOf(res));
                ans[0] = res;
                latch.countDown();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        contentTv.setText(res);
//                    }
//                });
            }
        });
        try {
            latch.await();
        }
        catch (InterruptedException e) {
        }
        return ans[0];
    }

    public static Map postFiles (String url, Map params, List<File> files, String fileKey, Context context) {

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"));
        //创建RequestBody
        if (params != null) {
            String jsonStr = JsonMapUtil.getJson(params);
            RequestBody paramsBody =  RequestBody.create(MediaType.parse("multipart/form-data")
                    , jsonStr);
            Log.e("jsonstr",jsonStr);
            multipartBodyBuilder.addPart(paramsBody);
        }
        if (files != null){
            Log.e("files",files.toString());
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(fileKey, file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }

        MultipartBody multipartBody = multipartBodyBuilder.build();


        //构建Request对象
        Request request = new Request.Builder()
                .url(headUrl.concat(url))
                .post(multipartBody)
                .build();


        //构建call对象
        Call call = client.newCall(request);

        //异步post请求
        final Map[] ans = {new HashMap()};
        final CountDownLatch latch = new CountDownLatch(1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("postFail","failed");
                Looper.prepare();
                ToastUtil.showMsg(context, "Connection failed");
                Looper.loop();
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final Map res = JsonMapUtil.getMap(response.body().string());
                Log.e("res", String.valueOf(res));
                ans[0] = res;
                latch.countDown();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        contentTv.setText(res);
//                    }
//                });
            }
        });
        try {
            latch.await();
        }
        catch (InterruptedException e) {
        }
        return ans[0];
    }
}
