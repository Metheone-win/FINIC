package com.wcoast.finic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.activity.KhanaKhazanaIImages;
import com.wcoast.finic.model.ModelKhanaKhazana;
import com.wcoast.finic.utility.Constant;

import java.util.ArrayList;

public class KhanaKhazanaAdapter extends RecyclerView.Adapter<KhanaKhazanaAdapter.ViewHolder> {
    Context context;

    private ArrayList<ModelKhanaKhazana> khanaKhazanaArrayList;

    public KhanaKhazanaAdapter(Context context) {
        this.context = context;

    }

    public void setData(ArrayList<ModelKhanaKhazana> khanaKhazanaArrayList) {
        this.khanaKhazanaArrayList = khanaKhazanaArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_subcategories, parent, false);

        return new KhanaKhazanaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        ModelKhanaKhazana khanaKhazana = khanaKhazanaArrayList.get(position);

        int resId = context.getResources().getIdentifier(khanaKhazana.getIconNames(), "drawable", context.getPackageName());
        holder.ivIcon.setImageResource(resId);
        holder.tvSubCatName.setText(khanaKhazana.getSubCategoryName());

        holder.ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KhanaKhazanaIImages.class);
                intent.putExtra(Constant.ID, holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return khanaKhazanaArrayList != null ? khanaKhazanaArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubCatName;

        TextView tvSubCatInitials;

        ImageView ivArrow;
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            tvSubCatName = itemView.findViewById(R.id.tv_sub_cat);
            tvSubCatInitials = itemView.findViewById(R.id.tv_name_initials);
            ivArrow = itemView.findViewById(R.id.iv_arrow_reference);
            ivIcon = itemView.findViewById(R.id.iv_category_image);
        }
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
