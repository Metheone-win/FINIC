package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelNotification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private String TAG = NotificationAdapter.class.getSimpleName();

    private ArrayList<ModelNotification> notificationArrayList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationTitle, tvNotificationDate, tvNotificationDesc;


        MyViewHolder(View view) {
            super(view);
            tvNotificationDate = view.findViewById(R.id.tv_date);
            tvNotificationDesc = view.findViewById(R.id.tv_summary);
            tvNotificationTitle = view.findViewById(R.id.tv_game_name);
        }
    }


    public NotificationAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelNotification> notifications) {
        this.notificationArrayList = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.MyViewHolder holder, int position) {

        ModelNotification notification = notificationArrayList.get(position);
        holder.tvNotificationTitle.setText(notification.getNotificationTitle());
        holder.tvNotificationDesc.setText(notification.getNotificationDesc());
        holder.tvNotificationDate.setText(notification.getNotificationDate());


    }


    @Override
    public int getItemCount() {
        return notificationArrayList != null ? notificationArrayList.size() : 0;
    }

}