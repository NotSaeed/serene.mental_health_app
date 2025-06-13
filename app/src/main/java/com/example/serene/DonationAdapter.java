package com.example.serene;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private final List<DonationHistoryActivity.DonationRecord> donationList;
    private final String username;

    public DonationAdapter(List<DonationHistoryActivity.DonationRecord> donationList, String username) {
        this.donationList = donationList;
        this.username = username;
    }

    @NonNull
    @Override
    public DonationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.ViewHolder holder, int position) {
        DonationHistoryActivity.DonationRecord record = donationList.get(position);

        String name = record.anonymous ? "Anonymous" : username;
        holder.txtDonatedBy.setText("From: " + name);
        holder.txtRecipient.setText("To: " + record.recipient);
        holder.txtAmount.setText(String.format(Locale.getDefault(),
                "RM%.2f (Fee: RM%.2f)", record.amount, record.appFee));
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDonatedBy, txtRecipient, txtAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonatedBy = itemView.findViewById(R.id.txtDonatedBy);
            txtRecipient = itemView.findViewById(R.id.txtRecipient);
            txtAmount = itemView.findViewById(R.id.txtAmount);
        }
    }
}
