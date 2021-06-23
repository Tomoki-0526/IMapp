package com.example.course29.chat.chatContent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.course29.R;
import com.example.course29.UserInfo.UserActivity;
import com.example.course29.WebSocketService;
import com.example.course29.chat.chatContent.member.MemberActivity;
import com.example.course29.chat.map.AmapActivity;
import com.example.course29.chat.record.AudioRecorderButton;
import com.example.course29.contact.friend.FriendActivity;
import com.example.course29.contact.group.GroupInviteActivity;
import com.example.course29.moment.MomentFragment;
import com.example.course29.moment.MomentPictureActivity;
import com.example.course29.moment.MomentTextActivity;
import com.example.course29.moment.MomentVideoActivity;
import com.example.course29.util.FileUriUtil;
import com.example.course29.util.GlobalVariable;
import com.example.course29.util.HttpUtil;
import com.example.course29.util.JsonMapUtil;
import com.example.course29.util.ToastUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatContentActivity extends AppCompatActivity {
    private ImageView mIvChatContentReturn;
    private ImageView mIvChatContentMore;
    private RecyclerView mRvChatContentList;
    private TextView mTvChatContentName;
    private ImageView mIvChatContentTalk;
    private ImageView mIvChatContentWrite;
    private ImageView mIvChatContentSend;
    private ImageView mIvChatContentExplore;
    private ImageView mIvChatContentPicture;
    private ImageView mIvChatContentVideo;
    private ImageView mIvChatContentPosition;
    private EditText mEtChatContentText;
    private LinearLayout mLlChatContentExploreList;
    private AudioRecorderButton mArbChatContentRecord;
    private String visibly = "GONE";
    private Uri imageUri;
    private Uri videoUri;
    private String type = "picture";
    private String mStrUsername;
    private String mStrName;
    private String mStrMultiple;
    private String mStrLinkId;
    private ChatContentAdapter adapter;
    private ArrayList<ChatContent> ChatContents = new ArrayList<>();
    private String quit = "false";
    private String input = "keyboard";

    private InnerServiceConnection serviceConnection = new InnerServiceConnection();
    private WebSocketService.InnerIBinder binder;
    private WebSocketService webSocketService;
    private ChatMessageReceiver chatMessageReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        getWebSocket();

        //启动service
        startWebSocketService();
        //绑定服务
        bindService();

        doRegisterReceiver();

        Intent getIntent = getIntent();
        mStrUsername = getIntent.getStringExtra("strUsername");
        mStrName = getIntent.getStringExtra("strName");
        mStrMultiple = getIntent.getStringExtra("strMultiple");
        mStrLinkId = getIntent.getStringExtra("strLinkId");

        mTvChatContentName = findViewById(R.id.tv_chatContent_name);
        mIvChatContentReturn = findViewById(R.id.iv_chatContent_return);
        mIvChatContentMore = findViewById(R.id.iv_chatContent_more);
        mRvChatContentList = findViewById(R.id.rv_chatContent_list);
        mIvChatContentTalk = findViewById(R.id.iv_chatContent_talk);
        mIvChatContentWrite = findViewById(R.id.iv_chatContent_write);
        mIvChatContentSend = findViewById(R.id.iv_chatContent_send);
        mIvChatContentExplore = findViewById(R.id.iv_chatContent_explore);
        mIvChatContentPicture = findViewById(R.id.iv_chatContent_picture);
        mIvChatContentVideo = findViewById(R.id.iv_chatContent_video);
        mIvChatContentPosition = findViewById(R.id.iv_chatContent_position);
        mLlChatContentExploreList = findViewById(R.id.ll_chatContent_exploreList);
        mEtChatContentText = findViewById(R.id.et_chatContent_text);
        mArbChatContentRecord = findViewById(R.id.arb_chatContent_record);

        mTvChatContentName.setText(mStrName);
        mIvChatContentWrite.setVisibility(View.GONE);
        mArbChatContentRecord.setVisibility(View.GONE);

        mIvChatContentTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecord();
                if (ContextCompat.checkSelfPermission(ChatContentActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    return;
                }
                mIvChatContentWrite.setVisibility(View.VISIBLE);
                mArbChatContentRecord.setVisibility(View.VISIBLE);
                mIvChatContentTalk.setVisibility(View.GONE);
                mEtChatContentText.setVisibility(View.GONE);
                mIvChatContentSend.setVisibility(View.GONE);
            }
        });
        mArbChatContentRecord.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(int seconds, String FilePath) {
                //拿到文件地址和时长后就可以去做发送语音的操作了
                File file = new File(FilePath);
                Log.e("111","语音文件为："+file.toString());
                sendAudio(file);
//                postProfile(file);

            }
        });

        mIvChatContentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGPS();
            }
        });

        mIvChatContentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvChatContentWrite.setVisibility(View.GONE);
                mArbChatContentRecord.setVisibility(View.GONE);
                mIvChatContentTalk.setVisibility(View.VISIBLE);
                mEtChatContentText.setVisibility(View.VISIBLE);
                mIvChatContentSend.setVisibility(View.VISIBLE);
            }
        });

        mIvChatContentExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visibly.equals("GONE")) {
                    visibly = "VISIBLY";
                    mLlChatContentExploreList.setVisibility(View.VISIBLE);
                }
                else {
                    visibly = "GONE";
                    mLlChatContentExploreList.setVisibility(View.GONE);
                }
            }
        });
        mIvChatContentReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvChatContentPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "picture";
                openPhotos();
            }
        });
        mIvChatContentVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "video";
                openPhotos();
            }
        });
        mIvChatContentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtChatContentText.getText().toString().equals("")) {
                    ToastUtil.showMsg(ChatContentActivity.this,"请先输入文字");
                }
                else {
                    sendText();
                }
            }
        });

        mIvChatContentMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStrMultiple.equals("false")) {
                    moreChatOne(v);
                }
                else {
                    moreChatMore(v);
                }

            }
        });

        try {
            if(mStrMultiple.equals("false")) {
                initChatOne();
            }
            else {
                initChatMore();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRvChatContentList.setLayoutManager(new LinearLayoutManager(ChatContentActivity.this));
        adapter = new ChatContentAdapter(R.layout.item_chat_content, ChatContents);
        mRvChatContentList.setAdapter(adapter);
        mRvChatContentList.scrollToPosition(ChatContents.size()-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(mStrMultiple.equals("false")) {
                initChatOne();
            }
            else {
                initChatMore();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        mRvChatContentList.scrollToPosition(ChatContents.size()-1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(quit.equals("true")) {
            return;
        }
        if(mStrMultiple.equals("false")) {
            quitChatOne();
        }
        else {
            quitChatMore();
        }
    }

    private void getGPS() {
        if (ContextCompat.checkSelfPermission(ChatContentActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(ChatContentActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GlobalVariable.GPS_REQUEST_CODE);
        }  else {
            Intent intent = new Intent(ChatContentActivity.this, AmapActivity.class);
            intent.putExtra("longitude","");
            intent.putExtra("latitude","");
            intent.putExtra("strLinkId",mStrLinkId);
            intent.putExtra("strMultiple",mStrMultiple);
            startActivity(intent);
        }
    }

    private void openPhotos() {
        if (ContextCompat.checkSelfPermission(ChatContentActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(ChatContentActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.GALLERY_REQUEST_CODE);

        } else { //权限已经被授予，在这里直接写要执行的相应方法即可
            if(type.equals("picture")) {
                choosePhoto();
            }
            else {
                chooseVideo();
            }
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GlobalVariable.GALLERY_REQUEST_CODE);
    }

    private void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型" 所有类型则写 "image/*"
        intent.setType("video/*");
        startActivityForResult(intent, GlobalVariable.GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == GlobalVariable.GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(type.equals("picture")) {
                    choosePhoto();
                }
                else {
                    chooseVideo();
                }
            } else {
                // Permission Denied
                ToastUtil.showMsg(ChatContentActivity.this, "Permission Denied");
            }
        }
        else if (requestCode == GlobalVariable.GPS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(ChatContentActivity.this, AmapActivity.class);
                intent.putExtra("longitude","");
                intent.putExtra("latitude","");
                startActivity(intent);
            } else {
                // Permission Denied
                ToastUtil.showMsg(ChatContentActivity.this, "Permission Denied");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UserActivity.RESULT_OK) {   // 回传成功
            switch (requestCode) {                // 选择请求码
                case GlobalVariable.CAMERA_REQUEST_CODE: {
                    if(type.equals("picture")){
                        try {
                            // 裁剪
                            startUCrop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Log.e("test","video");
                        videoUri = data.getData();
                        File file = null;
                        try {

                            File Afile = FileUriUtil.getFileByUri(videoUri, ChatContentActivity.this);
                            Uri uriFile = null;
                            if(Afile.exists()) {
                                uriFile = Uri.fromFile(Afile);
                            }
                            else {
                                uriFile = Uri.parse(Uri.decode(videoUri.toString()));
                                Log.e("noFile", "noFile");
                            }
                            file = new File(new URI(uriFile.toString()));
                            Log.e("video",file.toString());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        sendVideo(file);
                    }
                    break;
                }
                case GlobalVariable.GALLERY_REQUEST_CODE: {
                    // 获取图片
                    if(type.equals("picture")) {
                        try {
                            imageUri = data.getData();
                            if (imageUri != null) {
                                startUCrop();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        videoUri = data.getData();
                        File file = null;
                        try {

                            File Afile = FileUriUtil.getFileByUri(videoUri, ChatContentActivity.this);
                            Uri uriFile = null;
                            if(Afile.exists()) {
                                uriFile = Uri.fromFile(Afile);
                            }
                            else {
                                uriFile = Uri.parse(Uri.decode(videoUri.toString()));
                                Log.e("noFile", "noFile");
                            }
                            file = new File(new URI(uriFile.toString()));
                            Log.e("video",file.toString());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        sendVideo(file);
                    }
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    // 裁剪照片
                    final Uri croppedUri = UCrop.getOutput(data);
                    Log.e("uri",croppedUri.toString());
                    try {
                        if(croppedUri!=null) {
//                            Glide.with(this)
//                                    .load(croppedUri.toString())
//                                    .into(mIvUserEditProfile);
                            File file = new File(new URI(croppedUri.toString()));
                            sendPicture(file);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case UCrop.RESULT_ERROR: {
                    final Throwable cropError = UCrop.getError(data);
                    Log.i("RESULT_ERROR","UCrop_RESULT_ERROR");
                    break;
                }
            }
        }
    }

    private void startUCrop(){
        //裁剪后保存到文件中
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), new Date().getTime() + ".jpeg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.SecondaryYellow));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.SecondaryYellow));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }

    private void sendText () {
        Map map = new HashMap();
        map.put("linkId",mStrLinkId);
        map.put("isMultiple",mStrMultiple);
        map.put("type",0);
        map.put("text",mEtChatContentText.getText().toString());
        Map res = HttpUtil.post("/chat/sendMessage",map, ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
            mEtChatContentText.setText("");
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void sendPicture (File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        Map map = new HashMap();
        map.put("linkId",mStrLinkId);
        map.put("isMultiple",mStrMultiple);
        map.put("type",1);
        Map res = HttpUtil.postFiles("/chat/sendMessage",map, files, "image",ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void sendVideo (File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        Map map = new HashMap();
        map.put("linkId",mStrLinkId);
        map.put("isMultiple",mStrMultiple);
        map.put("type",3);
        Map res = HttpUtil.postFiles("/chat/sendMessage",map, files, "video",ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
//            Log.e("test","视频成功");
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void sendAudio (File file) {
        List<File> files = new ArrayList<>();
        files.add(file);
        Map map = new HashMap();
        map.put("linkId",mStrLinkId);
        map.put("isMultiple",mStrMultiple);
        map.put("type",2);
        Map res = HttpUtil.postFiles("/chat/sendMessage",map, files, "audio",ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
//            Log.e("test","视频成功");
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void initChatOne() throws JSONException {
        Map map1 = new HashMap();
        map1.put("toUsername",mStrUsername);
        Map res = HttpUtil.post("/chat/getChatLink",map1, ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
            mStrLinkId = res.get("linkId").toString();
            String username = res.get("username").toString();
            String nickname = res.get("nickname").toString();
            String remark = res.get("remark").toString();
            String avatar = res.get("avatar").toString();
            JSONArray jsonArray = (JSONArray) res.get("msgs");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            ChatContents.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                ChatContents.add(new ChatContent(map.get("username").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("avatar").toString(),
                        map.get("msgId").toString(),
                        map.get("sendTime").toString(),
                        map.get("fromMyself").toString(),
                        map.get("type").toString(),
                        map.get("text").toString(),
                        map.get("image").toString(),
                        map.get("audio").toString(),
                        map.get("video").toString(),
                        map.get("longitude").toString(),
                        map.get("latitude").toString()));
            }
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void initChatMore() throws JSONException {
        Map map1 = new HashMap();
        map1.put("groupId",mStrLinkId);
        Map res = HttpUtil.post("/chat/enterGroupChat",map1, ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
            String groupAvatar = res.get("avatar").toString();
            String groupName = res.get("groupName").toString();
            JSONArray jsonArray = (JSONArray) res.get("msgs");
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            ChatContents.clear();
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                ChatContents.add(new ChatContent(map.get("username").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("avatar").toString(),
                        map.get("msgId").toString(),
                        map.get("sendTime").toString(),
                        map.get("fromMyself").toString(),
                        map.get("type").toString(),
                        map.get("text").toString(),
                        map.get("image").toString(),
                        map.get("audio").toString(),
                        map.get("video").toString(),
                        map.get("longitude").toString(),
                        map.get("latitude").toString()));
            }
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void quitChatOne() {
        Map map = new HashMap();
        map.put("linkId",mStrLinkId);
        Map res = HttpUtil.post("/chat/quitChat", map, ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void quitChatMore() {
        Map map = new HashMap();
        map.put("groupId",mStrLinkId);
        Map res = HttpUtil.post("/chat/quitGroupChat", map, ChatContentActivity.this);
        if (res.get("success").toString() == "true") {
//                overridePendingTransition(0,0);
        }
        else {
            ToastUtil.showMsg(ChatContentActivity.this,
                    res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
        }
    }

    private void moreChatOne(View v) {
        PopupMenu popup = new PopupMenu(ChatContentActivity.this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.chat_one_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.chatOne_check :
                        intent = new Intent(ChatContentActivity.this, FriendActivity.class);
                        intent.putExtra("strUsername",mStrUsername);
                        startActivity(intent);
                        break;
                    case R.id.chatOne_history:
                        intent = new Intent(ChatContentActivity.this, ChatHistoryActivity.class);
                        intent.putExtra("strLinkId",mStrLinkId);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void moreChatMore(View v) {
        PopupMenu popup = new PopupMenu(ChatContentActivity.this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.chat_more_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.chatMore_check:
                        intent = new Intent(ChatContentActivity.this, MemberActivity.class);
                        intent.putExtra("strGroupId",mStrLinkId);
                        startActivity(intent);
                        break;
                    case R.id.chatMore_invite:
                        intent = new Intent(ChatContentActivity.this, ChatHistoryActivity.class);
                        intent.putExtra("strGroupId",mStrLinkId);
                        startActivity(intent);
                        break;
                    case R.id.chatMore_history:
                        intent = new Intent(ChatContentActivity.this, ChatHistoryActivity.class);
                        intent.putExtra("strUsername",mStrUsername);
                        intent.putExtra("strMultiple","true");
                        intent.putExtra("strLinkId",mStrLinkId);
                        startActivity(intent);
                        break;
                    case R.id.chatMore_quit:
                        View contentView = LayoutInflater.from(ChatContentActivity.this).inflate(R.layout.dialog_confirm,null);
                        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(ChatContentActivity.this);
                        TextView dialogTvText = contentView.findViewById(R.id.tv_dialogConfirm_text);
                        dialogTvText.setText("确认退出该群聊？");
                        Button dialogBtnConfirm = contentView.findViewById(R.id.btn_dialogConfirm_confirm);
                        Button dialogBtnCancel = contentView.findViewById(R.id.btn_dialogConfirm_cancel);
                        layoutDialog.setView(contentView);
                        Dialog dialog = layoutDialog.create();
                        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map map = new HashMap();
                                map.put("groupId",mStrLinkId);
                                Map res = HttpUtil.post("/chat/quitGroup",map, ChatContentActivity.this);
                                if (res.get("success").toString() == "true") {
                                    quit = "true";
                                    finish();
                                }
                                else {
                                    ToastUtil.showMsg(ChatContentActivity.this,
                                            res.get("msg") != null?res.get("msg").toString() : "Unknown Error");
                                }
                                dialog.dismiss();
                            }
                        });
                        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void getRecord() {
        if (ContextCompat.checkSelfPermission(ChatContentActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {   //权限还没有授予，需要在这里写申请权限的代码
            // 第二个参数是一个字符串数组，里面是你需要申请的权限 可以设置申请多个权限
            // 最后一个参数是标志你这次申请的权限，该常量在onRequestPermissionsResult中使用到
            ActivityCompat.requestPermissions(ChatContentActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GlobalVariable.RECORD_REQUEST_CODE);

        } else if (ContextCompat.checkSelfPermission(ChatContentActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatContentActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    GlobalVariable.RECORD_REQUEST_CODE);
        }
    }

    private void  oneMore(String text) {

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(text);
            List<Map<String,Object>> list = JsonMapUtil.jsonArrayToList(jsonArray);
            if(list == null) return;
            for (int i=0; i<list.size(); i++)
            {
                Map<String,Object> map = list.get(i);
                if(!map.get("flag").toString().equals("4")&&!map.get("flag").toString().equals("5")
                &&!map.get("flag").toString().equals("6")&&!map.get("flag").toString().equals("7")
                &&!map.get("flag").toString().equals("8")) continue;
                ChatContents.add(new ChatContent(map.get("username").toString(),
                        map.get("nickname").toString(),
                        map.get("remark").toString(),
                        map.get("avatar").toString(),
                        map.get("msgId").toString(),
                        map.get("sendTime").toString(),
                        map.get("fromMyself").toString(),
                        map.get("type").toString(),
                        map.get("text").toString(),
                        map.get("image").toString(),
                        map.get("audio").toString(),
                        map.get("video").toString(),
                        map.get("longitude").toString(),
                        map.get("latitude").toString()));
                Log.e("oneMore","do");
            }
            adapter.notifyDataSetChanged();
            mRvChatContentList.scrollToPosition(ChatContents.size()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWebSocket () {
        Request request = new Request.Builder()
                .url("ws://8.140.133.34:7562/ws")
                .build();
//        WebSocket mWebSocket =
        HttpUtil.client.newWebSocket(request, new WebSocketListener() {
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
                Log.e("tag", "客户端收到消息:" + text);
//                oneMore(text);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                super.onMessage(webSocket, bytes);

            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                super.onOpen(webSocket, response);

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
        });
    }


    private class ChatMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra("message");
            Log.e("message",message);
            oneMore(message);
        }
    }

    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("CCTV5");
        registerReceiver(chatMessageReceiver, filter);
    }

    private void startWebSocketService() {
        Intent intent = new Intent(this, WebSocketService.class);
        startService(intent);
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, WebSocketService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    private class InnerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service","onServiceConnected()->当Activity和Service连接");
            binder = (WebSocketService.InnerIBinder) service;
            webSocketService = binder.getService();
//            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Service","onServiceConnected()->当Activity和Service断开连接");
        }
    }
}