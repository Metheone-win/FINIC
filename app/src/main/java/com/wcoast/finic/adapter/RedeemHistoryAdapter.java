package com.wcoast.finic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcoast.finic.R;
import com.wcoast.finic.model.ModelRedeemHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.wcoast.finic.activity.BaseActivity.getDateInMillis;

public class RedeemHistoryAdapter extends RecyclerView.Adapter<RedeemHistoryAdapter.MyViewHolder> {
    private String TAG = RedeemHistoryAdapter.class.getSimpleName();

    private ArrayList<ModelRedeemHistory> modelRedeemHistories;
    private Context context;

    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTxnDate, tvSummary, tvTxnNo, tvTxnAmount, tvTxnStatus;

        MyViewHolder(View view) {
            super(view);
            tvTxnDate = view.findViewById(R.id.tv_date);
            tvSummary = view.findViewById(R.id.tv_summary);
            tvTxnNo = view.findViewById(R.id.tv_game_name);
            tvTxnAmount = view.findViewById(R.id.tv_txn_amount);
            tvTxnStatus = view.findViewById(R.id.tv_txn_status);
        }
    }


    public RedeemHistoryAdapter(Context context) {

        this.context = context;
    }

    public void setData(ArrayList<ModelRedeemHistory> modelRedeemHistories) {
        this.modelRedeemHistories = modelRedeemHistories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RedeemHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_redeem_history, parent, false);

        return new RedeemHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RedeemHistoryAdapter.MyViewHolder holder, int position) {

        ModelRedeemHistory modelRedeemHistory = modelRedeemHistories.get(position);

        CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(getDateInMillis(modelRedeemHistory.getDate()), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

        holder.tvTxnDate.setText(niceDateStr);
        holder.tvSummary.setText(modelRedeemHistory.getSummary());
        holder.tvTxnNo.setText(String.format("Redeem Request Id: %s", modelRedeemHistory.getTxnId()));
        holder.tvTxnAmount.setText(String.format("Redeem Points: %s", modelRedeemHistory.getTxnAmount()));

        if(modelRedeemHistory.getTxnStatus().equals("Rejected"))
        {
            holder.tvTxnStatus.setTextColor(context.getResources().getColor(R.color.colorRejected));
        }else if(modelRedeemHistory.getTxnStatus().equals("Pending"))
        {
            holder.tvTxnStatus.setTextColor(context.getResources().getColor(R.color.colorPending));
        }
        else
            holder.tvTxnStatus.setTextColor(context.getResources().getColor(R.color.colorSuccess));

        holder.tvTxnStatus.setText(modelRedeemHistory.getTxnStatus());
    }


    @Override
    public int getItemCount() {
        return modelRedeemHistories != null ? modelRedeemHistories.size() : 0;
    }

}