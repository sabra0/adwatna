package com.example.adwatna1project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    List<Users> userList;
    onItemClickListener onItemClickListener;
    Context context;
    private boolean isChat;


    public UserAdapter(List<Users> userList, Context context,boolean isChat) {
        this.userList = userList;
        this.context = context;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users currentUser = userList.get(position);
        holder.username.setText(currentUser.getName());
        Picasso.get().load(currentUser.getImage()).into(holder.profileImage);

        if(isChat){
            if (currentUser.getStatus().equals("online")){
                holder.imgOn.setVisibility(View.VISIBLE);
                holder.imgOff.setVisibility(View.GONE);
            }else{
                holder.imgOn.setVisibility(View.GONE);
                holder.imgOff.setVisibility(View.VISIBLE);
            }
        }else {
            holder.imgOff.setVisibility(View.GONE);
            holder.imgOn.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("ownerId",currentUser.getUid());
                intent.putExtra("ownerName",currentUser.getName());
                intent.putExtra("ownerImage",currentUser.getImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView username;
        public CircleImageView profileImage;
        private ImageView imgOn;
        private ImageView imgOff;
        onItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, onItemClickListener onItemClickListener) {
            super(itemView);
            username = itemView.findViewById(R.id.username_text_view);
            profileImage = itemView.findViewById(R.id.profile_image);
            imgOn = itemView.findViewById(R.id.img_on);
            imgOff = itemView.findViewById(R.id.img_off);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v,getAdapterPosition());
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(View view,int position);
    }
}
