package ua.cm.idiscover.SlideBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

import ua.cm.idiscover.Main.SignInActivity;
import ua.cm.idiscover.R;

/**
 * Created by vitib on 23/03/2018.
 */

public class ProfileActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/{

    EditText nome;
    EditText email;
    EditText prof;
    EditText nac;
    EditText idade;
    EditText telef;
    String codigo;

    Button editBTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nome = (EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        prof = (EditText) findViewById(R.id.editTextProf);
        nac = (EditText) findViewById(R.id.editTextNac);
        idade = (EditText) findViewById(R.id.editText9);
        telef = (EditText) findViewById(R.id.editText10);
        editBTN = (Button) findViewById(R.id.button5);

        KeyboardFocusChangeListener keyboardFocusChangeListener = new KeyboardFocusChangeListener();
        nome.setOnFocusChangeListener(keyboardFocusChangeListener);
        prof.setOnFocusChangeListener(keyboardFocusChangeListener);
        nac.setOnFocusChangeListener(keyboardFocusChangeListener);
        idade.setOnFocusChangeListener(keyboardFocusChangeListener);
        telef.setOnFocusChangeListener(keyboardFocusChangeListener);

        setUnavailable(nome);
        setUnavailable(email);
        setUnavailable(prof);
        setUnavailable(nac);
        setUnavailable(idade);
        setUnavailable(telef);

        FirebaseFirestore fs = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        String uID = intent.getStringExtra("user");

        CollectionReference col = fs.collection("Users");
        DocumentReference document = col.document(uID);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null) {

                            if (doc.getData().get("Nome") != null) {
                                nome.setText(doc.getData().get("Nome").toString());
                            }
                            if (doc != null) {
                                email.setText(doc.getId());
                            }
                            if (doc.getData().get("Profissão") != null) {
                                prof.setText(doc.getData().get("Profissão").toString());
                            }
                            if (doc.getData().get("Nacionalidade") != null) {
                                nac.setText(doc.getData().get("Nacionalidade").toString());
                            }
                            if (doc.getData().get("Idade") != null) {
                                idade.setText(doc.getData().get("Idade").toString());
                            }
                            if (doc.getData().get("Telefone") != null) {
                                telef.setText(doc.getData().get("Telefone").toString());
                            }
                            codigo = doc.getData().get("codigo").toString();
                        }
                    }
                }
            });

            editBTN.setOnClickListener(new editAction());

        }

        public void setUnavailable(EditText et){

            et.setEnabled(false);
            //et.setInputType(InputType.TYPE_NULL);
            //et.setFocusable(false);
        }

    public void setAvailable(EditText et){

        et.setEnabled(true);
        //et.setInputType(InputType.TYPE_NULL);
        //et.setFocusable(true);
    }

    private class editAction implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            setAvailable(nome);
            setAvailable(prof);
            setAvailable(nac);
            setAvailable(idade);
            setAvailable(telef);

            editBTN.setText("Save");

            editBTN.setOnClickListener(new saveAction());
        }
    }

    private class saveAction implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            setUnavailable(nome);
            setUnavailable(prof);
            setUnavailable(nac);
            setUnavailable(idade);
            setUnavailable(telef);

            editBTN.setText("Editar");

            editBTN.setOnClickListener(new editAction());

            Map<String, Object> user = new HashMap<>();
            if(!nome.getText().toString().equals("")){
                user.put("Nome", nome.getText().toString());
            }
            if(!prof.getText().toString().equals("")){
                user.put("Profissao", prof.getText().toString());
            }
            if(!nac.getText().toString().equals("")){
                user.put("Nacionalidade", nac.getText().toString());
            }
            if(!idade.getText().toString().equals("")){
                user.put("Idade", idade.getText().toString());
            }
            if(!telef.getText().toString().equals("")){
                user.put("Telefone", telef.getText().toString());
            }
            user.put("codigo", codigo);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").document(email.getText().toString())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error writing document", e);
                        }
                    });
        }
    }

    private class KeyboardFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(!hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

}
