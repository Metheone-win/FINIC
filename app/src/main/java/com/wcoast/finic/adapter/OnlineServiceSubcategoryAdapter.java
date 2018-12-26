package com.wcoast.finic.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelNameLink;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineServiceSubcategoryAdapter extends RecyclerView.Adapter<OnlineServiceSubcategoryAdapter.MyViewHolder> {
    private String TAG = OnlineServiceSubcategoryAdapter.class.getSimpleName();

    private ArrayList<ModelNameLink> imageNames;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;

        View view;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);

            this.view = view;
        }
    }


    public OnlineServiceSubcategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ModelNameLink> imageNames) {
        this.imageNames = imageNames;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OnlineServiceSubcategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_online_service_sub_category, parent, false);

        return new OnlineServiceSubcategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OnlineServiceSubcategoryAdapter.MyViewHolder holder, int position) {

        final ModelNameLink modelNameLink = imageNames.get(position);
        int resId = context.getResources().getIdentifier(modelNameLink.getImageName(), "drawable", context.getPackageName());
        holder.ivImage.setImageResource(resId);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(modelNameLink.getImageLink()));
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return imageNames != null ? imageNames.size() : 0;
    }

}

