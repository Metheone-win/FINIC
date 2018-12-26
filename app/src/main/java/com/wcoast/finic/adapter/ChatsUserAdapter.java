package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelChatUsers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatsUserAdapter extends RecyclerView.Adapter {
    private String TAG = ChatsUserAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<ModelChatUsers> mMessageList;
    private int senderId;

    public ChatsUserAdapter(Context context) {
        Context mContext = context;
    }

    public void setData(List<ModelChatUsers> messageList, int receiverId, int senderId) {
        mMessageList = messageList;
        int receiverId1 = receiverId;
        this.senderId = senderId;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ModelChatUsers chat = mMessageList.get(position);
        Log.d(TAG, "getItemViewType: " + chat.getSenderID());
        if (chat.getSenderID() == senderId) {
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
        ModelChatUsers message = mMessageList.get(position);

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

        void bind(ModelChatUsers chat) {
            messageText.setText(chat.getMessage());

            SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            timeText.setText(localDateFormat.format(new Date(chat.getMessageTime())));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView profileImage;
CardView cvImg;
        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.txt_msg_body);
            cvImg = itemView.findViewById(R.id.cv_img);
            timeText = itemView.findViewById(R.id.text_message_time);
            profileImage = itemView.findViewById(R.id.image_message_profile);
            cvImg.setVisibility(View.GONE);
        }

        void bind(ModelChatUsers chat) {
            messageText.setText(chat.getMessage());

            // Format the stored timestamp into a readable String using method.
            SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm a");
            timeText.setText(localDateFormat.format(new Date(chat.getMessageTime())));

        }
    }
}