package com.example.qr_scanner_and_image_similarity_detection.activities;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_scanner_and_image_similarity_detection.MessageChatAdapter;
import com.example.qr_scanner_and_image_similarity_detection.R;
import com.example.qr_scanner_and_image_similarity_detection.models.MessageChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.optionmenu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block:

                return true;
            case R.id.reminder:

                return true;
        }
        return false;
    }

    List<MessageChatModel> messageChatModelList =  new ArrayList<>();
    RecyclerView recyclerView;
    MessageChatAdapter adapter ;

    EditText messageET;
    ImageView sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageView menu=(ImageView)findViewById(R.id.optionmenu) ;
        registerForContextMenu(menu);

        messageET = (EditText)findViewById(R.id.messageET);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);


        //*********************8test recycler view *************************************************************
        MessageChatModel model1 = new MessageChatModel(
                "صباح الخير",
                "10:00 PM",
                0
        );
        MessageChatModel model2 = new MessageChatModel(
                "صباح النور",
                "10:01 PM",
                1
        );
        MessageChatModel model3 = new MessageChatModel(
                "ازيك يا امل عامله ايه",
                "10:02 PM",
                0
        );
        MessageChatModel model4 = new MessageChatModel(
                "الحمد لله تمام وانتي",
                "10:03 PM",
                1
        );
        MessageChatModel model5 = new MessageChatModel(
                "الحمد لله ",
                "10:04 PM",
                0
        );
        MessageChatModel model6 = new MessageChatModel(
                "عامله ايه في المشروع",
                "10:05 PM",
                1
        );
        MessageChatModel model7 = new MessageChatModel(
                "الحمد لله تمام",
                "10:05 PM",
                0
        );
        MessageChatModel model8 = new MessageChatModel(
                "ديما يارب",
                "10:06 PM",
                1
        );
        MessageChatModel model9 = new MessageChatModel(
                "سلمتي البروجيكت",
                "10:07 PM",
                0
        );
        MessageChatModel model10 = new MessageChatModel(
                "اه سلمته وانتي",
                "10:08 PM",
                1
        );
        MessageChatModel model11 = new MessageChatModel(
                "لسه بخلص فيه شويه حاجات",
                "10:09 PM",
                0
        );
        MessageChatModel model12 = new MessageChatModel(
                "ربنا معاكي يارب",
                "10:10 PM",
                1
        );
        MessageChatModel model13 = new MessageChatModel(
                "يارب",
                "10:11 PM",
                0
        );


        messageChatModelList.add(model1);
        messageChatModelList.add(model2);
        messageChatModelList.add(model3);
        messageChatModelList.add(model4);
        messageChatModelList.add(model5);
        messageChatModelList.add(model6);
        messageChatModelList.add(model7);
        messageChatModelList.add(model8);
        messageChatModelList.add(model9);
        messageChatModelList.add(model10);
        messageChatModelList.add(model11);
        messageChatModelList.add(model12);
        messageChatModelList.add(model13);

//********************* recycler view *************************************************************
        recyclerView.smoothScrollToPosition(messageChatModelList.size());
        adapter = new MessageChatAdapter(messageChatModelList, ChatActivity.this );
        recyclerView.setAdapter(adapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageET.getText().toString();

                MessageChatModel model = new MessageChatModel(
                        msg,
                        "10:12 PM",
                        0
                );
                messageChatModelList.add(model);
                recyclerView.smoothScrollToPosition(messageChatModelList.size());
                adapter.notifyDataSetChanged();
                messageET.setText("");


            }
        });



    }
}
