package ua.cm.idiscover.Forum;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ua.cm.idiscover.BeginMenu.ForumActivity;
import ua.cm.idiscover.R;

public class CreateChanel extends AppCompatActivity {

    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addchanel);

        Button create = (Button) findViewById(R.id.createChanel);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText chanelName = (EditText) findViewById(R.id.chanelName);
                EditText description = (EditText) findViewById(R.id.description);

                Map<String, String> data = new HashMap<>();
                data.put("chanelDescription", description.getText().toString().trim());

                db.collection("Canais").document(chanelName.getText().toString().trim()).set(data);

                Intent intent = new Intent(CreateChanel.this, ForumActivity.class);
                startActivity(intent);
            }
        });

    }
}
