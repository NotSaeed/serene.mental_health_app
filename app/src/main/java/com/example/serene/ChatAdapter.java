package com.example.serene;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isUser ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.item_message_user, parent, false);
            return new UserMessageHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_bot, parent, false);
            return new BotMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof UserMessageHolder) {
            ((UserMessageHolder) holder).text.setText(message.message);
        } else {
            ((BotMessageHolder) holder).text.setText(message.message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserMessageHolder extends RecyclerView.ViewHolder {
        TextView text;
        UserMessageHolder(View view) {
            super(view);
            text = view.findViewById(R.id.textMessage);
        }
    }

    static class BotMessageHolder extends RecyclerView.ViewHolder {
        TextView text;
        BotMessageHolder(View view) {
            super(view);
            text = view.findViewById(R.id.textMessage);
        }
    }
}
