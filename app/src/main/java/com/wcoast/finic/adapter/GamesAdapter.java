package com.wcoast.finic.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelNameLink;

import java.util.ArrayList;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ModelNameLink> modelNameLinkArrayList;

    public GamesAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ModelNameLink> modelNameLinkArrayList) {
        this.modelNameLinkArrayList = modelNameLinkArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rv_games, parent, false);
        return new GamesAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ModelNameLink modelNameLink = modelNameLinkArrayList.get(position);

        int resId = context.getResources().getIdentifier(modelNameLink.getImageName(), "drawable", context.getPackageName());
        holder.ivGames.setImageResource(resId);

        holder.ivGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(modelNameLink.getImageLink());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    context.startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, " Unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelNameLinkArrayList != null ? modelNameLinkArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGames;

        public ViewHolder(View itemView) {
            super(itemView);

            ivGames = itemView.findViewById(R.id.iv_game_image);

        }
    }
}
