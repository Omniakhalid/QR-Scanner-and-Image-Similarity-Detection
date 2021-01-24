package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.MessageActivity;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.MessageChatModel;
import com.example.qr_scanner_and_image_similarity_detection.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersChatAdapter extends RecyclerView.Adapter<UsersChatAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> muser;

    public UsersChatAdapter(Context mcontext, List<User> muser) {
        this.mcontext = mcontext;
        this.muser = muser;
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
            intent.putExtra("user_id",user.getName());
            mcontext.startActivity(intent);
        }
    });


    }

    @Override
    public int getItemCount() {
        return muser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView Username;
        public ImageView profile_image;
        public ViewHolder(View itemView){
            super(itemView);
            Username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);

        }


    }


}
