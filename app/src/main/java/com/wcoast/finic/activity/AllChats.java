package com.wcoast.finic.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wcoast.finic.R;
import com.wcoast.finic.adapter.AllChatsAdapter;
import com.wcoast.finic.model.ModelUser;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllChats extends BaseActivity {

    private static final String TAG = "AllChats";
    int senderId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_message_list)
    RecyclerView rvMsgList;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    private DatabaseReference UserRef;
    private ArrayList<ModelUser> messageList = new ArrayList<>();

    private AllChatsAdapter allChatsAdapter;

    private ArrayList<String> chatsNames = new ArrayList<>();
    private Map<ModelUser, String> index = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        init();
    }

    private void init() {
        showLoading(true, Constant.REQ_CODE_PROGRESS_BLUE);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        UserRef = database.getReference("UsersChat/Users");

        senderId = Integer.parseInt(new SessionManager(this).getUserId());

        // checkNewMessages();

        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);

                if (dataSnapshot.hasChild(senderId + "")) {
                    checkNewMessages();

                } else {
                    showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);
                    tvEmpty.setVisibility(View.VISIBLE);
                    Log.d(TAG, "haschild: " + false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        allChatsAdapter = new AllChatsAdapter(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvMsgList.setLayoutManager(manager);


    }

    private void checkNewMessages() {
//        q.child(senderId + "").addChildEventListener(new ChildEventListener() {
        UserRef.child(senderId + "").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                Log.d(TAG, "onChildAdded() called with: dataSnapshot = [" + dataSnapshot + "]");

                showLoading(false, Constant.REQ_CODE_PROGRESS_BLUE);

                ModelUser chat = dataSnapshot.getValue(ModelUser.class);

                messageList.add(chat);

                index.put(messageList.get(messageList.size() - 1), dataSnapshot.getKey());

                Collections.sort(messageList, new Comparator<ModelUser>() {
                    @Override
                    public int compare(ModelUser o1, ModelUser o2) {

                        return compareLong(o1.getChat().getMessageTime(), o2.getChat().getMessageTime());

                    }
                });

                chatsNames.clear();
                for (int i = 0; i < messageList.size(); i++) {
                    chatsNames.add(index.get(messageList.get(i)));
                }

                allChatsAdapter.notifyDataSetChanged();

                if (chatsNames.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                }

                allChatsAdapter.setData(messageList, senderId, chatsNames);

                rvMsgList.setAdapter(allChatsAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                int i = chatsNames.indexOf(dataSnapshot.getKey());
                messageList.remove(i);
                chatsNames.remove(i);

                ModelUser chat = dataSnapshot.getValue(ModelUser.class);

                messageList.add(0, chat);
                chatsNames.add(0, dataSnapshot.getKey());

                Log.d(TAG, "onChildChanged() called with: dataSnapshot = [" + dataSnapshot + "], s = [" + s + "]");

                allChatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private int compareLong(long messageTime, long messageTime1) {
        if (messageTime > messageTime1) {
            return -1;
        } else if (messageTime == messageTime1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
