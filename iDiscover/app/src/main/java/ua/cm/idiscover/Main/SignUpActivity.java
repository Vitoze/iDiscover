package ua.cm.idiscover.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ua.cm.idiscover.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    /*FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersListRef = dbRef.child("Users");*/
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button submitButton = (Button) findViewById(R.id.button3);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String uName = ((EditText) findViewById(R.id.name_textbox)).getText().toString().trim();
                EditText password = ((EditText) findViewById(R.id.pass_textbox));
                EditText confirm = ((EditText) findViewById(R.id.confirm_textbox));
                String uMail = ((EditText) findViewById(R.id.mail_textbox)).getText().toString().trim();
                String uPass = password.getText().toString().trim();
                String cPass = confirm.getText().toString().trim();

                //verificar se pass_textbox é igual a confirm_textbox
                if(uPass.equals(cPass)){
                    //verificar se uMail indicado existe na BD

                    DocumentReference doc = db.collection("Users").document(uMail);

                    if(doc == null){
                        Toast.makeText(SignUpActivity.this, "Email incorrect",
                                Toast.LENGTH_SHORT).show();
                        password.setText("");
                        confirm.setText("");

                    }else {

                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(uMail, uPass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(SignUpActivity.this, RegSuccessActivity.class);
                                        startActivity(intent);
                                        Log.println(Log.INFO,"User email: ", user.getEmail());
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Authentication failed:" + task.getException().getMessage().trim(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                    }
                }else {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match",
                            Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirm.setText("");
                }

                /*HashMap<String, String> userInfo = new HashMap<String, String>();
                userInfo.put("Nome", uName);
                userInfo.put("Email", uMail);
                userInfo.put("Password", uPass);

                usersListRef.child("User1").setValue(userInfo);

                Map<String, Object> moreInfo = new HashMap<String, Object>();
                moreInfo.put("Idade", 20);

                usersListRef.child("User1").updateChildren(moreInfo, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.println(Log.INFO, "INFO", "Update Complete");
                    }
                });*/



            }
        });
    }
}
