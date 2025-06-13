package com.example.serene;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {
    private List<String> moodList;

    public MoodAdapter(List<String> moodList) {
        this.moodList = moodList;
    }

    @Override
    public MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoodViewHolder holder, int position) {
        holder.moodText.setText(moodList.get(position));
    }

    @Override
    public int getItemCount() {
        return moodList.size();
    }

    static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView moodText;

        MoodViewHolder(View itemView) {
            super(itemView);
            moodText = itemView.findViewById(R.id.moodText);
        }
    }
}
