package com.wcoast.finic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.activity.DetailActivity;
import com.wcoast.finic.activity.LoginActivity;
import com.wcoast.finic.model.ModelSubCategory;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.ArrayList;
import java.util.Random;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {
    private String TAG = SubCatAdapter.class.getSimpleName();

    private ArrayList<ModelSubCategory> modelSubCategoryList;
    private Context context;
    private String catName;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubCatName, tvLink, tvNameInitials;
        ImageView imgSubCatIcon, imgArrow;


        MyViewHolder(View view) {
            super(view);
            tvSubCatName = view.findViewById(R.id.tv_sub_cat);
            tvLink = view.findViewById(R.id.tv_link);
            tvNameInitials = view.findViewById(R.id.tv_name_initials);
            imgSubCatIcon = view.findViewById(R.id.iv_category_image);
            imgSubCatIcon = view.findViewById(R.id.iv_category_image);
            imgArrow = view.findViewById(R.id.iv_arrow_reference);
        }
    }


    public SubCatAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelSubCategory> modelSubCategoryList, String catName) {
        this.modelSubCategoryList = modelSubCategoryList;
        this.catName = catName;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_subcategories, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final ModelSubCategory subCategory = modelSubCategoryList.get(position);
        holder.tvSubCatName.setText(subCategory.getSubcatName());
        holder.tvLink.setText(subCategory.getSubcatLink());

        holder.imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  try {
                    Uri uri = Uri.parse(subCategory.getSubcatLink());
                    Intent intent = new Intent("android.intent.action.VIEW", uri);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(holder.tvSubCatName, "Navigation to the assigned url", Snackbar.LENGTH_LONG).show();

                }*/

                if (new SessionManager(context).isUserLoggedIn()) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(Constant.SUB_CAT_NAME, subCategory.getSubcatName());
                    intent.putExtra(Constant.SUB_CAT_LINK, subCategory.getSubcatLink());
                    intent.putExtra(Constant.CAT_NAME, catName);
                    intent.putExtra(Constant.SUB_CAT_DETAIL, subCategory.getSubcatDetail());
                    intent.putExtra(Constant.SUB_CAT_ID, subCategory.getSubcatId() + "");

                    context.startActivity(intent);
                } else
                    Snackbar.make(holder.tvSubCatName, context.getResources().getString(R.string.activate_your_account), Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }).show();

            }
        });

        if (subCategory.getSubcatImage().isEmpty()) {
            setVisibilty(holder.tvNameInitials, true);
            setVisibilty(holder.imgSubCatIcon, false);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            holder.tvNameInitials.setBackgroundColor(color);
            holder.tvNameInitials.setText(getInitials(subCategory.getSubcatName()));
        } else {
            setVisibilty(holder.tvNameInitials, false);
            setVisibilty(holder.imgSubCatIcon, true);

            int resId = context.getResources().getIdentifier(subCategory.getSubcatImage(), "drawable", context.getPackageName());
            holder.imgSubCatIcon.setImageResource(resId);
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

    @Override
    public int getItemCount() {
        return modelSubCategoryList != null ? modelSubCategoryList.size() : 0;
    }

    private void setVisibilty(View v, Boolean visible) {
        if (visible) {
            v.setVisibility(View.VISIBLE);
        } else v.setVisibility(View.INVISIBLE);
    }
}