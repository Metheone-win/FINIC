package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.ChatsUserAdapter;
import com.wcoast.finic.model.ModelChatUsers;
import com.wcoast.finic.model.ModelUser;
import com.wcoast.finic.notifications.FcmNotificationBuilder;
import com.wcoast.finic.notifications.NotificationHelper;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatUsersActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_message_list)
    RecyclerView rvMsgList;
    @BindView(R.id.et_chat_box)
    EditText etMessageBox;
    @BindView(R.id.btn_chat_box_send)
    ImageView btnSend;
    private String TAG = ChatActivity.class.getSimpleName();
    private String message;
    private String senderName;

    private List<ModelChatUsers> messageList;

    private DatabaseReference chatRef, senderRef, receiverRef;
    private ModelChatUsers chat;
    private String receiverName, receiverImg, receiverToken, chatName;

    private int senderId, receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HashMap<String, String> userData = new SessionManager(this).getUserDetails();

        senderId = Integer.parseInt(userData.get(SessionManager.KEY_USER_ID));
        senderName = userData.get(SessionManager.KEY_NAME);

        receiverId = Integer.parseInt(getIntent().getStringExtra(Constant.RECEIVER_ID));
        receiverName = getIntent().getStringExtra(Constant.RECEIVER_NAME);
        receiverImg = getIntent().getStringExtra(Constant.RECEIVER_IMG);
        receiverToken = getIntent().getStringExtra(Constant.RECEIVER_TOKEN);

        toolbar.setTitle(receiverName);

        messageList = new ArrayList<>();
        chat = new ModelChatUsers();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (getIntent().hasExtra(Constant.CHAT_NAME)) {

            chatName = getIntent().getStringExtra(Constant.CHAT_NAME);

        } else {
            chatName = senderId + "@" + receiverId;
            int levelNo = getIntent().getIntExtra(Constant.LEVEL_NO, 0);
        }
        chatRef = database.getReference("UsersChat/Chats/" + chatName);

        senderRef = database.getReference("UsersChat/Users/" + senderId);
        receiverRef = database.getReference("UsersChat/Users/" + receiverId);


        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);

        try {
            new NotificationHelper(this).cancelNotification(this, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
          Get New Children nodes.
         */

        chatRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                chat = dataSnapshot.getValue(ModelChatUsers.class);
                messageList.add(chat);
                rvMsgList.scrollToPosition(messageList.size() - 1);

            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });

        ChatsUserAdapter messageListAdapter = new ChatsUserAdapter(this);
        messageListAdapter.setData(messageList, receiverId, senderId);

        LinearLayoutManager manager = new LinearLayoutManager(this);

        rvMsgList.setLayoutManager(manager);

        rvMsgList.setAdapter(messageListAdapter);

        rvMsgList.scrollToPosition(messageList.size() - 1);

        rvMsgList.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override

            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                rvMsgList.scrollToPosition(messageList.size() - 1);

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //  databaseReference.setValue("Hello, World!");
                        message = etMessageBox.getText().toString().trim();

                        if (!message.isEmpty()) {

                            ModelChatUsers chat = new ModelChatUsers(senderId, message, receiverId);
                            chatRef.push().setValue(chat);
                            senderRef.child(chatName).setValue(new ModelUser(receiverImg, receiverName, receiverToken, chat, receiverId));
                            receiverRef.child(chatName).setValue(new ModelUser(new SessionManager(ChatUsersActivity.this).getUserDetails().get(SessionManager.KEY_PROFILE_PIC), senderName, new SessionManager(ChatUsersActivity.this).getFcmToken(), chat, senderId));

                            FcmNotificationBuilder.initialize();
                            FcmNotificationBuilder fcmNotificationBuilder = new FcmNotificationBuilder();
                            fcmNotificationBuilder.message(message);
                            fcmNotificationBuilder.title(senderName);
                            fcmNotificationBuilder.senderId(receiverId + "");
                            fcmNotificationBuilder.receiverImg(receiverImg);
                            fcmNotificationBuilder.receiverName(receiverName);
                            fcmNotificationBuilder.receiverFirebaseToken(receiverToken);
                            fcmNotificationBuilder.send();

                            etMessageBox.setText("");

                        } else {
                            Snackbar.make(etMessageBox, "type something", Snackbar.LENGTH_SHORT).show();
                            etMessageBox.requestFocus();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        new SessionManager(ChatUsersActivity.this).setLastChatUserId(receiverId);
        super.onResume();
    }

    @Override
    protected void onStart() {
        new SessionManager(this).putRunningStatus(true, false, false, false);

        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {

        new SessionManager(this).putRunningStatus(false, false, false, false);

        Log.d(TAG, "onPause: ");
        super.onPause();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


