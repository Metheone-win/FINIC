package com.wcoast.finic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wcoast.finic.R;
import com.wcoast.finic.activity.ChatUsersActivity;
import com.wcoast.finic.activity.ReferActivitySubTree;
import com.wcoast.finic.model.ModelReferredPeople;
import com.wcoast.finic.utility.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ReferredPeopleAdapter extends RecyclerView.Adapter<ReferredPeopleAdapter.MyViewHolder> {
    private String TAG = ReferredPeopleAdapter.class.getSimpleName();

    private ArrayList<ModelReferredPeople> referredPeopleArrayList;
    private Context context;
    private int levelNo;

    private ArrayList<Integer> idStack;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvName, tvMobile, tvEmail, tvChild, tvInitials, tvReferCode;
        private Button btnChat;
        private ImageView ivArrow;
        private ImageView ivProfileImage;
        private View viewLower, viewSpace; //view space will bring the tv name bit down when there is not data in holder
        private View viewUpper;
        private CardView cvParent;

        MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tv_date);
            tvEmail = view.findViewById(R.id.tv_email);
            tvMobile = view.findViewById(R.id.tv_mobile);
            tvName = view.findViewById(R.id.tv_name);
            btnChat = view.findViewById(R.id.btn_chat);
            tvChild = view.findViewById(R.id.tv_childs);
            ivArrow = view.findViewById(R.id.btn_arrow);
            cvParent = view.findViewById(R.id.cv_parent);
            tvInitials = view.findViewById(R.id.tv_name_initials);
            tvReferCode = view.findViewById(R.id.tv_refer_code);
            viewUpper = view.findViewById(R.id.view_upper);
            ivProfileImage = view.findViewById(R.id.iv_image_profile);
            viewLower = view.findViewById(R.id.view_lower);
            viewSpace = view.findViewById(R.id.view_space);
        }
    }

    public ReferredPeopleAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelReferredPeople> referredPeopleArrayList, int levelNo, ArrayList<Integer> idStack) {

        this.referredPeopleArrayList = referredPeopleArrayList;
        this.levelNo = levelNo;
        this.idStack = idStack;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReferredPeopleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_refer_and_earn, parent, false);

        return new ReferredPeopleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReferredPeopleAdapter.MyViewHolder holder, int position) {

        final ModelReferredPeople referredPeople;
        // check whether there is any data in array list or not, if not show empty 4 slots.
        if (referredPeopleArrayList.size() == 0) {

           /*  holder.tvChild.setVisibility(View.INVISIBLE);
            holder.tvReferCode.setVisibility(View.INVISIBLE);
            holder.tvEmail.setVisibility(View.INVISIBLE);
            holder.tvMobile.setVisibility(View.INVISIBLE);

            holder.viewSpace.getLayoutParams().height = 16; // the view will bring tvName a bit down for proper display
          */
            referredPeople = new ModelReferredPeople("", "Not Available", "", ""
                    , "", "", "0", "0", "", "");

        } else {
            referredPeople = referredPeopleArrayList.get(position);
        }

        //convert date in required format
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        Date date = null;

        if (!referredPeople.getDate().isEmpty()) {
            try {
                date = originalFormat.parse(referredPeople.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tvDate.setText(targetFormat.format(date));
            Log.d(TAG, "onBindViewHolder: " + date + "");

        } else holder.tvDate.setText(referredPeople.getDate());

        holder.tvEmail.setText(referredPeople.getEmail());
        holder.tvName.setText(referredPeople.getName());
        holder.tvMobile.setText(referredPeople.getMobile());
        holder.tvReferCode.setText(referredPeople.getReferCode());

        String childCount = "Downline: " + referredPeople.getChildAdded() + "/" + referredPeople.getChildTotal();
        if (referredPeople.getChildTotal() == "0") {
            holder.tvChild.setVisibility(View.INVISIBLE);
        } else
            holder.tvChild.setText(childCount);
        //hide the last vertical line after 4th item.
        if (holder.getLayoutPosition() == 3) {
            holder.viewLower.setVisibility(View.INVISIBLE);
        }

        //set name initials if profile pic is empty
        if (referredPeople.getProfilePic().isEmpty()) {

            holder.tvInitials.setVisibility(View.VISIBLE);
            holder.ivProfileImage.setVisibility(View.GONE);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            holder.tvInitials.setBackgroundColor(color);
            holder.tvInitials.setText(getInitials(referredPeople.getName()));

        } else {
            holder.tvInitials.setVisibility(View.GONE);
            holder.ivProfileImage.setVisibility(View.VISIBLE);

            Picasso.get().load(referredPeople.getProfilePic()).into(holder.ivProfileImage);
        }

        int childCountInt = Integer.parseInt(referredPeople.getChildAdded());

        //show empty slots if item has no child, and level is <7.
        if (childCountInt == 0 && levelNo < 7) {

            holder.ivArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(context, ReferActivitySubTree.class);
                    in.putExtra(Constant.USER_ID, "0");
                    in.putExtra(Constant.PROFILE_PIC, "");
                    in.putExtra(Constant.FULL_NAME, !referredPeople.getName().isEmpty() ? referredPeople.getName() : "Not Available");
                    in.putExtra(Constant.LEVEL_NO, levelNo + 1);
                    in.putExtra(Constant.ID, idStack);
                    context.startActivity(in);
                    ((Activity) context).finish();
                }
            });
        } else if (childCountInt == 0 & levelNo == 7) {  //hide arrow
            holder.ivArrow.setVisibility(View.INVISIBLE);
            holder.ivArrow.setClickable(false);

        } else {
            holder.ivArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(context, ReferActivitySubTree.class);

                    in.putExtra(Constant.USER_ID, referredPeople.getUserId());
                    in.putExtra(Constant.FULL_NAME, referredPeople.getName());
                    in.putExtra(Constant.PROFILE_PIC, referredPeople.getProfilePic());
                    in.putExtra(Constant.LEVEL_NO, levelNo + 1);
                    in.putExtra(Constant.ID, idStack);
                    context.startActivity(in);
                    ((Activity) context).finish();
                }
            });
        }

        if (position == 3) {
            holder.viewLower.setVisibility(View.INVISIBLE);
        }

        if (levelNo > 1) {
            //  holder.btnChat.setVisibility(View.GONE);
            holder.btnChat.setSelected(false);
            holder.btnChat.setVisibility(View.GONE);
            holder.tvMobile.setVisibility(View.GONE);
            holder.tvEmail.setVisibility(View.GONE);
            holder.viewLower.requestLayout();
            holder.viewLower.getLayoutParams().height = holder.viewLower.getLayoutParams().height - 10;
            holder.viewUpper.requestLayout();
            holder.viewUpper.getLayoutParams().height = holder.viewLower.getLayoutParams().height - 10;

        } else {
            holder.btnChat.setSelected(true);
        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(context, ChatUsersActivity.class);
                in.putExtra(Constant.RECEIVER_ID, referredPeople.getUserId());
                in.putExtra(Constant.RECEIVER_NAME, referredPeople.getName());
                in.putExtra(Constant.RECEIVER_IMG, referredPeople.getProfilePic());
                in.putExtra(Constant.RECEIVER_TOKEN, referredPeople.getReceiverToken());
                in.putExtra(Constant.LEVEL_NO, levelNo + 1);
                context.startActivity(in);

            }
        });
    }

    @Override
    public int getItemCount() {
           return referredPeopleArrayList!=null ? referredPeopleArrayList.size() :0;

        //return 4;
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