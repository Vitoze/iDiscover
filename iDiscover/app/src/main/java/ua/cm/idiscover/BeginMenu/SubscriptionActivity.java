package ua.cm.idiscover.BeginMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ua.cm.idiscover.R;
import ua.cm.idiscover.SlideBar.TicketActivity;

/**
 * Created by vitib on 16/03/2018.
 */

public class SubscriptionActivity extends AppCompatActivity {
    ImageButton type1, type2, type3;
    ImageView qrImage;
    TextView textAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        initComponents();

        //click type1
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubscriptionActivity.this, TicketActivity.class);
                intent.putExtra("text", "type1");
                intent.putExtra("class", "subscription");
                startActivity(intent);
            }
        });

        //click type2
        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubscriptionActivity.this, TicketActivity.class);
                intent.putExtra("text", "type2");
                startActivity(intent);
            }
        });

        //click type3
        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubscriptionActivity.this, TicketActivity.class);
                intent.putExtra("text", "type3");
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        type1 = (ImageButton) findViewById(R.id.type1);
        type2 = (ImageButton) findViewById(R.id.type2);
        type3 = (ImageButton) findViewById(R.id.type3);
    }
}
