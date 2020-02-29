package com.example.adwatna1project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> usersMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

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
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        Message messages = usersMessage.get(position);

        String fromUserID = messages.getFrom();

        holder.receiverMessageText.setVisibility(View.GONE);
        holder.senderMessageText.setVisibility(View.GONE);
        holder.receiverProfileImage.setVisibility(View.GONE);
        holder.seenTextView.setVisibility(View.GONE);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();

                    Picasso.get().load(receiverImage).into(holder.receiverProfileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (fromUserID.equals(messageSenderId))
        {
            holder.senderMessageText.setVisibility(View.VISIBLE);

            holder.senderMessageText.setText(messages.getMessage());
        }
        else
        {
            holder.receiverMessageText.setVisibility(View.VISIBLE);
            holder.receiverProfileImage.setVisibility(View.VISIBLE);

            holder.receiverMessageText.setText(messages.getMessage());
        }

        if (position == usersMessage.size()-1){
            if (messages.isIsseen()){
                holder.seenTextView.setVisibility(View.VISIBLE);
                holder.seenTextView.setText("seen");
            }
        }

    }

    @Override
    public int getItemCount() {
        return usersMessage.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView senderMessageText, receiverMessageText , seenTextView;
        CircleImageView receiverProfileImage;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            senderMessageText = itemView.findViewById(R.id.sender_messsage_text);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverProfileImage = itemView.findViewById(R.id.message_profile_image);
            seenTextView = itemView.findViewById(R.id.seen_text_view);
        }
    }
}
