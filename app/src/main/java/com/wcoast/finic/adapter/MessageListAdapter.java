package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelChat;
import com.wcoast.finic.utility.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageListAdapter extends RecyclerView.Adapter {
    private String TAG = MessageListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<ModelChat> mMessageList;


    public MessageListAdapter(Context context, List<ModelChat> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    public MessageListAdapter() {
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ModelChat chat = mMessageList.get(position);
        Log.d(TAG, "getItemViewType: " + chat.getSenderID());
        if (chat.getSenderID().equals(new SessionManager(mContext).getUserId())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_send, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModelChat message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.txt_msg_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(ModelChat chat) {
            messageText.setText(chat.getMessage());

            SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            timeText.setText(localDateFormat.format(new Date(Long.parseLong(chat.getMessageTime()))));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.txt_msg_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(ModelChat chat) {
            messageText.setText(chat.getMessage());

            // Format the stored timestamp into a readable String using method.
            SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            timeText.setText(localDateFormat.format(new Date(Long.parseLong(chat.getMessageTime()))));

            profileImage.setImageResource(R.drawable.placeholder);
        }
    }
}