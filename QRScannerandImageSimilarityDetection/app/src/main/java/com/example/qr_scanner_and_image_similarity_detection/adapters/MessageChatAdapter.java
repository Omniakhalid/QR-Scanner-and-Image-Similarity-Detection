package com.example.qr_scanner_and_image_similarity_detection.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.MessageChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatAdapter.ViewHolder> {

   private List<MessageChatModel> messageChatModelList;
   private Context context;
    FirebaseUser fuser;

    private static final int MESSAGE_TYPE_RIGHT = 1;
    private static final int MESSAGE_TYPR_LEFT = 2;


    public MessageChatAdapter(List<MessageChatModel> messageChatModelList, Context context) {
        this.messageChatModelList = messageChatModelList;
        this.context = context;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @NonNull
    @Override
    public int getItemViewType(int position) {
       fuser= FirebaseAuth.getInstance().getCurrentUser();
       if(messageChatModelList.get(position).getSender().equals(fuser.getUid()))
           return MESSAGE_TYPE_RIGHT;
       else
           return  MESSAGE_TYPR_LEFT;


    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == MESSAGE_TYPE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, parent, false);
            return new MessageChatAdapter.ViewHolder(view);
        }
        else  {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receive_layout, parent, false);
            return new MessageChatAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageChatAdapter.ViewHolder holder, int position) {

        MessageChatModel message = messageChatModelList.get(position);
        holder.show_message.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {

        return messageChatModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder( View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.message);
        }

    }






}
