package ua.cm.idiscover.Forum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import ua.cm.idiscover.R;

public class ChanelActivity extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter<ChatMessage, MessageViewHolder> FBRA;
    RelativeLayout activity_chanel;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton,submitButton;
    EmojIconActions emojIconActions;

    String titulo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chanel);

        activity_chanel = (RelativeLayout)findViewById(R.id.activity_chanel);
        titulo = getIntent().getStringExtra("item");

        // get descricao
        CollectionReference canais = db.collection("Canais");
        canais.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    Log.d("Chanel", document.getId() + " -> " + titulo);
                    if(document.getId().equals(titulo)) {
                        TextView tview = (TextView) findViewById(R.id.chanelDescription);
                        tview.setText(document.getData().get("chanelDescription").toString());
                    }
                }
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_of_message);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = FirebaseFirestore.getInstance().collection("Canais").document(titulo).collection("Messages").orderBy("messageTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        FBRA = new FirestoreRecyclerAdapter<ChatMessage, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull ChatMessage model) {
                holder.setContent(model.getMessageText(), model.getMessageUser(), model.getMessageTime());
            }

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

                return new MessageViewHolder(view);
            }
        };
        recyclerView.setAdapter(FBRA);

        //Add Emoji
        emojiButton = (ImageView)findViewById(R.id.emoji_button);
        submitButton = (ImageView)findViewById(R.id.submit_button);
        emojiconEditText = (EmojiconEditText)findViewById(R.id.emojicon_edit_text);
        emojIconActions = new EmojIconActions(getApplicationContext(),activity_chanel,emojiButton,emojiconEditText);
        emojIconActions.ShowEmojicon();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(emojiconEditText.getText().toString())) {
                    db.collection("Canais").document(titulo).collection("Messages").add(new ChatMessage(emojiconEditText.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    emojiconEditText.setText("");
                    emojiconEditText.requestFocus();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FBRA.startListening();
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String text, String user, long time) {

            //Get references to the views of list_item.xml
            TextView messageText, messageUser, messageTime;
            messageText = (EmojiconTextView) mView.findViewById(R.id.message_text);
            messageUser = (TextView) mView.findViewById(R.id.message_user);
            messageTime = (TextView) mView.findViewById(R.id.message_time);

            messageText.setText(text);
            messageUser.setText(user);
            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", time));
        }
    }
/*
    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);

        ArrayList<ChatMessage> arraychat = new ArrayList<ChatMessage>();
        ArrayAdapter<ChatMessage> chatadapter = new ArrayAdapter<ChatMessage>(this, R.layout.list_item, arraychat);
        adapter.
        {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {

                //Get references to the views of list_item.xml
                TextView messageText, messageUser, messageTime;
                messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messageTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
    }*/
}
