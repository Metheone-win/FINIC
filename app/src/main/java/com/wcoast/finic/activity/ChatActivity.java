package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.wcoast.finic.adapter.MessageListAdapter;
import com.wcoast.finic.model.ModelChat;
import com.wcoast.finic.notifications.NotificationHelper;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

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
    private String senderID;

    private List<ModelChat> messageList;

    private DatabaseReference databaseReference;
    private ModelChat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        HashMap<String, String> data = new SessionManager(this).getSubCatIDAndName();
        HashMap<String, String> userData = new SessionManager(this).getUserDetails();

        String subCatID = data.get(Constant.SUB_CAT_ID);
        String subCatName = data.get(Constant.SUB_CAT_NAME);

        senderID = userData.get(SessionManager.KEY_USER_ID);
        String senderName = userData.get(SessionManager.KEY_NAME);

        toolbar.setTitle(subCatName);

        messageList = new ArrayList<>();
        chat = new ModelChat();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Chats/" + senderID + "@" + subCatID + "@" + senderName + "@" + subCatName + "@admin");

        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);

        try {
            new NotificationHelper(this).cancelNotification(this, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

            }
        });

        /*
          Get New Children nodes.
         */
        databaseReference.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                chat = dataSnapshot.getValue(ModelChat.class);
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

        MessageListAdapter messageListAdapter = new MessageListAdapter(this, messageList);

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

                message = etMessageBox.getText().toString().trim();
                databaseReference.push().setValue(new ModelChat(senderID, message));
                etMessageBox.setText("");
            }
        });

    }

    @Override
    protected void onStart() {
        new SessionManager(this).putRunningStatus(false, true, false, false);

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


}