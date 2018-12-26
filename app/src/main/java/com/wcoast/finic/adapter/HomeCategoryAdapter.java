package com.wcoast.finic.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.activity.KhanaKhazanaSubCategory;
import com.wcoast.finic.activity.LoginActivity;
import com.wcoast.finic.activity.ReferActivity;
import com.wcoast.finic.activity.SubCatActivity;
import com.wcoast.finic.model.ModelCategory;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder> {
    int position;
    private Context context;
    private List<ModelCategory> modelCategoryList;

    public HomeCategoryAdapter(Context context, List<ModelCategory> modelCategoryList) {
        this.context = context;
        this.modelCategoryList = modelCategoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_categories, parent, false);

        return new HomeCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.imgCat.setImageDrawable(context.getResources().getDrawable(modelCategoryList.get(position).getCatImage()));
        holder.nameCat.setText(modelCategoryList.get(position).getCatName());

        holder.view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (modelCategoryList.get(position).getCatId() > 9) {

                    Intent in = new Intent(context, SubCatActivity.class);
                    in.putExtra(Constant.CAT_ID, modelCategoryList.get(position).getCatId());
                    in.putExtra(Constant.CAT_NAME, modelCategoryList.get(position).getCatName());
                    in.putExtra(Constant.CAT_INFO_TEXT, modelCategoryList.get(position).getCatInformationText());
                    in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(in);

                } else if (modelCategoryList.get(position).getCatId() == 9) {

                    if (new SessionManager(context).isUserLoggedIn()) {
                        Intent intent = new Intent(context, ReferActivity.class);
                        context.startActivity(intent);
                    } else {
                        Snackbar.make(holder.nameCat, context.getResources().getString(R.string.activate_your_account), Snackbar.LENGTH_SHORT).setAction("Login", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }).show();
                    }

                } else {
                    Intent intent = new Intent(context, KhanaKhazanaSubCategory.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelCategoryList != null ? modelCategoryList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameCat;
        ImageView imgCat;
        View view;

        MyViewHolder(View view) {
            super(view);

            imgCat = view.findViewById(R.id.image_category);
            nameCat = view.findViewById(R.id.name_cat);
            this.view = view;

        }
    }


}
