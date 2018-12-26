package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelLevelDetails;

import java.util.ArrayList;

public class LevelDetailsAdapter extends RecyclerView.Adapter<LevelDetailsAdapter.MyViewHolder> {
    private String TAG = LevelDetailsAdapter.class.getSimpleName();

    private ArrayList<ModelLevelDetails> levelDetailsArrayList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChildAdded, tvChildTotal, tvLevelAmount2, tvLevelAmount1, tvPointsTotal, tvPointsAdded, tvLevelNo;

        MyViewHolder(View view) {
            super(view);
            tvChildAdded = view.findViewById(R.id.tv_child_added);
            tvChildTotal = view.findViewById(R.id.tv_child_total);
            tvLevelAmount2 = view.findViewById(R.id.tv_level_amount2);
            tvLevelAmount1 = view.findViewById(R.id.tv_level_amount);
            tvPointsAdded = view.findViewById(R.id.tv_point_added);
            tvPointsTotal = view.findViewById(R.id.tv_point_total);
            tvLevelNo = view.findViewById(R.id.tv_level_no);

        }
    }

    public LevelDetailsAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelLevelDetails> levelDetailsArrayList) {
        this.levelDetailsArrayList = levelDetailsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LevelDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_level_details, parent, false);

        return new LevelDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LevelDetailsAdapter.MyViewHolder holder, int position) {

        final ModelLevelDetails levelDetails = levelDetailsArrayList.get(position);

        holder.tvLevelNo.setText(String.valueOf(levelDetails.getLevel()));

        holder.tvPointsAdded.setText(String.format("%s Points", levelDetails.getPointAdded()));
        holder.tvChildAdded.setText(String.format("%s Members", levelDetails.getChildAdded()));
        holder.tvLevelAmount2.setText(String.format("%s Points", levelDetails.getLevelAmount()));
        holder.tvPointsTotal.setText(String.format("%s Points", levelDetails.getPointTotal()));
        holder.tvChildTotal.setText(String.format("%s Members", levelDetails.getChildTotal()));
        holder.tvLevelAmount1.setText(String.format("%s Points", levelDetails.getLevelAmount()));

    }

    @Override
    public int getItemCount() {
        return levelDetailsArrayList != null ? levelDetailsArrayList.size() : 0;
    }
}