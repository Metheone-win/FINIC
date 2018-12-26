package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wcoast.finic.R;

import java.util.ArrayList;

public class KhanaKhazanaImageAdapter extends RecyclerView.Adapter<KhanaKhazanaImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> imagesNames;

    public KhanaKhazanaImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public KhanaKhazanaImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_khana_khazana, parent, false);
        return new KhanaKhazanaImageAdapter.ViewHolder(itemView);
    }

    public void setData(ArrayList<String> imagesNames) {
        this.imagesNames = imagesNames;
    }

    @Override
    public void onBindViewHolder(@NonNull KhanaKhazanaImageAdapter.ViewHolder holder, int position) {

        int resId = context.getResources().getIdentifier(imagesNames.get(position), "drawable", context.getPackageName());
        holder.ivKhanaKhazanaImage.setImageResource(resId);

    }


    @Override
    public int getItemCount() {
        return imagesNames!=null ? imagesNames.size() :0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivKhanaKhazanaImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ivKhanaKhazanaImage = itemView.findViewById(R.id.iv_khana_khazana);
        }
    }
}
