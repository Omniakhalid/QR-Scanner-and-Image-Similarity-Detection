package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.MessageActivity;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.MessageChatModel;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> muser;
    private  boolean ischat;
    String theLastMessage;

    public UsersChatAdapter(Context mcontext, List<User> muser,boolean ischat) {
        this.mcontext = mcontext;
        this.muser = muser;
        this.ischat=ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);
        return new UsersChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    User user=muser.get(position);
    //problem
        //user.getname();
    holder.Username.setText(user.getName());
    holder.profile_image.setImageResource(R.drawable.images);
   //to move to chat activity
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent=new Intent(mcontext, MessageActivity.class);
            intent.putExtra("user_id",user.getUId());
            mcontext.startActivity(intent);
        }
    });

        if(ischat){
            lastMessage(user.getUId(),holder.last_message);
        }
        else {
            holder.last_message.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username;
        public ImageView profile_image;
        private TextView last_message;
        public ViewHolder(View itemView){
            super(itemView);
            Username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);
            last_message=itemView.findViewById(R.id.last_message);


        }
    }

    private void lastMessage(String userid,TextView last_msg){
        theLastMessage="default";
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    MessageChatModel chat = snapshot.getValue(MessageChatModel.class);
                    if (chat.getReciver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReciver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {

                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("no message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage="default";
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}
