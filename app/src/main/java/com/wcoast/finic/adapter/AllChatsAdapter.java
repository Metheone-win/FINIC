package com.wcoast.finic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wcoast.finic.R;
import com.wcoast.finic.activity.ChatUsersActivity;
import com.wcoast.finic.model.ModelChatUsers;
import com.wcoast.finic.model.ModelUser;
import com.wcoast.finic.utility.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AllChatsAdapter extends RecyclerView.Adapter<AllChatsAdapter.MyViewHolder> {
    private String TAG = AllChatsAdapter.class.getSimpleName();

    private List<ModelUser> messageList;

    private Context context;

    int senderId;
    private ArrayList<String> chatsNames;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvMessage, tvInitials;
        ImageView ivReceiverImg;
        private View view;


        MyViewHolder(View view) {

            super(view);

            tvName = view.findViewById(R.id.tv_name);
            tvTime = view.findViewById(R.id.tv_time);
            tvMessage = view.findViewById(R.id.tv_message);
            tvInitials = view.findViewById(R.id.tv_name_initials);
            ivReceiverImg = view.findViewById(R.id.iv_user_image);
            this.view = view;
        }
    }


    public AllChatsAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelUser> messageList, int senderId, ArrayList<String> chatsNames) {
        this.messageList = messageList;
        this.senderId = senderId;
        this.chatsNames = chatsNames;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AllChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_chat_threads, parent, false);

        return new AllChatsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllChatsAdapter.MyViewHolder holder, final int position) {

        final ModelUser modelUser = messageList.get(position);

        holder.tvName.setText(modelUser.getSecondUserName());


        if (modelUser.getSecondUserImage().isEmpty()) {

            holder.tvInitials.setVisibility(View.VISIBLE);
            holder.ivReceiverImg.setVisibility(View.GONE);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            holder.tvInitials.setBackgroundColor(color);
            holder.tvInitials.setText(getInitials(modelUser.getSecondUserName()));

        } else {
            holder.tvInitials.setVisibility(View.GONE);
            holder.ivReceiverImg.setVisibility(View.VISIBLE);

            Picasso.get().load(modelUser.getSecondUserImage()).into(holder.ivReceiverImg);
        }

        final ModelChatUsers modelChat = modelUser.getChat();

        if (modelChat.getSenderID() == senderId) {

            holder.tvMessage.setText(String.format("You: %s", modelChat.getMessage()));

        } else holder.tvMessage.setText(modelChat.getMessage());

        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        holder.tvTime.setText(localDateFormat.format(new Date(modelChat.getMessageTime())));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chatName = chatsNames.get(position);

                Intent in = new Intent(context, ChatUsersActivity.class);

                in.putExtra(Constant.CHAT_NAME, chatName);

                in.putExtra(Constant.RECEIVER_ID, modelUser.getSecondUserId() + "");
                in.putExtra(Constant.RECEIVER_NAME, modelUser.getSecondUserName());
                in.putExtra(Constant.RECEIVER_IMG, modelUser.getSecondUserImage());
                in.putExtra(Constant.RECEIVER_TOKEN, modelUser.getSecondUserToken());
                context.startActivity(in);

            }
        });
    }


    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    private String getInitials(String subCatName) {
        StringBuilder initials = new StringBuilder();
        String[] words = subCatName.split(" ");

        if (words.length > 1) {
            for (int i = 0; i < 2; i++) {
                if (initials.length() == 0) {
                    initials = new StringBuilder(words[i].charAt(0) + "");
                } else
                    initials.append(" ").append(words[i].charAt(0));
            }
        } else {
            initials = new StringBuilder(words[0].charAt(0) + " " + words[0].charAt(1));
        }
        return initials.toString();
    }
}
