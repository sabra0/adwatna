package com.example.adwatna1project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> usersMessage;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Message> usersMessage) {
        this.usersMessage = usersMessage;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_custom_bar, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Message messages = usersMessage.get(position);

        String fromUserID = messages.getFrom();

        holder.receiverMessageText.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);

        if (fromUserID.equals(messageSenderId))
        {
            holder.senderMessageText.setVisibility(View.VISIBLE);

            holder.senderMessageText.setBackgroundResource(R.drawable.send_message_text_view);
            holder.senderMessageText.setTextColor(Color.WHITE);
            holder.senderMessageText.setText(messages.getMessage());
        }
        else
        {
            holder.receiverMessageText.setVisibility(View.VISIBLE);

            holder.receiverMessageText.setBackgroundResource(R.drawable.message_text_view);
            holder.receiverMessageText.setTextColor(Color.BLACK);
            holder.receiverMessageText.setText(messages.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return usersMessage.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView senderMessageText, receiverMessageText;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);

        }
    }
}
