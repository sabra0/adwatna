package com.example.adwatna1project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adwatna1project.Notifications.Client;
import com.example.adwatna1project.Notifications.Data;
import com.example.adwatna1project.Notifications.MyResponse;
import com.example.adwatna1project.Notifications.Sender;
import com.example.adwatna1project.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    static String messageText;
    String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;
    TextView userName;
    CircleImageView userImage;
    Toolbar ChatToolBar;

    ImageView sendMessageButton;
    EditText messageInputText;

    FirebaseAuth mAuth;
    DatabaseReference rootRef;

    List<Message> messageList;
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    RecyclerView userMessageList;

    ValueEventListener seenListener;
    DatabaseReference reference;

    APIService apiService;
//    boolean notify = false;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        messageList = new ArrayList<>();

        messageReceiverID = getIntent().getStringExtra("ownerId");
        messageReceiverName = getIntent().getStringExtra("ownerName");
        messageReceiverImage = getIntent().getStringExtra("ownerImage");
        userid = getIntent().getStringExtra("userid");


        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        ChatToolBar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        actionBar.setCustomView(actionBarView);

        userName = findViewById(R.id.custom_profile_name);
        userImage = findViewById(R.id.custom_profile_image);

        userName.setText(messageReceiverName);
        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile_image).into(userImage);

        sendMessageButton = findViewById(R.id.send_message_btn);
        messageInputText = findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messageList);
        userMessageList = findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messageAdapter);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                notify = true;
                sendMessage();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Messages");
        seenMessage(messageSenderID);
    }


    @Override
    protected void onStart() {
        super.onStart();
        rootRef.child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        messageList.clear();
                        for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                            Message messages = snapshot.getValue(Message.class);
                            if (messages.getTo().equals(messageReceiverID) && messages.getFrom().equals(messageSenderID)
                            ||messages.getFrom().equals(messageReceiverID) && messages.getTo().equals(messageSenderID)){
                                messageList.add(messages);
                            }

                            messageAdapter.notifyDataSetChanged();
                            userMessageList.smoothScrollToPosition(userMessageList.getAdapter().getItemCount());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void sendMessage() {
        messageText = messageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else{

            DatabaseReference userMessageKeyRef = rootRef.child("Messages").push();

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("from", messageSenderID);
            messageTextBody.put("to", messageReceiverID);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("isseen", false);

            rootRef.child("Messages").child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    messageInputText.setText("");
                }
            });

            updateToken(FirebaseInstanceId.getInstance().getToken());

            final String msg = messageText;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(messageSenderID);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
//                    if (notify){
//
//                    }
//                    notify = false;
                    sendNotification(messageReceiverID,user.getName(),msg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                    .child(messageSenderID)
                    .child(messageReceiverID);

            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        chatRef.child("id").setValue(messageReceiverID);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("id", messageSenderID);
            DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList");
            chatRef2.child(messageReceiverID).child(messageSenderID).updateChildren(userMap);
        }


    }

    private void sendNotification(final String messageReceiverID, final String name, final String msg) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(messageReceiverID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(messageSenderID , R.drawable.mysplash, ""
                            ,name +" : " + msg, userid);

                    Sender sender = new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success !=1){
                                            Toast.makeText(ChatActivity.this, "Failed....", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(messageSenderID).setValue(token1);
    }

    public void seenMessage(final String userId){
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    if (message.getTo().equals(messageSenderID) && message.getFrom().equals(userId)){
                        HashMap<String ,Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }

    //for notification
//    private void sendNotification() {
//
//        Intent intent = new Intent(this, DisplayChatWithUsersActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, /*(int) when*/1, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationManager notificationManager = (NotificationManager)
//                getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "MyId";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            @SuppressLint("WrongConstant") NotificationChannel notificationChannel
//                    = new NotificationChannel
//                    (NOTIFICATION_CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_MAX);
//
//            notificationChannel.setDescription("My channel for test FCM");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setShowWhen(true)
//                .setSmallIcon(android.R.drawable.ic_notification_overlay)
//                .setTicker("Ticker")
//                .setContentTitle(messageSenderID)
//                .setContentText(messageText)
//                .setContentInfo("info")
//                .setContentIntent(pendingIntent);
//
//        notificationManager.notify(1, notificationBuilder.build());
//
//    }

}
