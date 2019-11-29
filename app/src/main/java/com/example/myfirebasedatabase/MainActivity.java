package com.example.myfirebasedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText medtname,medtmail,medtidnumber;
    private Button mbtnsave,mbtnview;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medtname= findViewById(R.id.edtname);
        medtmail= findViewById(R.id.edtemail);
        medtidnumber= findViewById(R.id.edtnumber);
        mbtnsave= findViewById(R.id.btnsave);
        mbtnview= findViewById(R.id.btnview);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please Wait...");

        //start by coonecting your app to firebse and then,
        //set an onclick listener to the save button to start saving

        mbtnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start by getting data from the user
                String name = medtname.getText().toString().trim();
                String email = medtmail.getText().toString().trim();
                String id_number = medtidnumber.getText().toString().trim();

                if (name.isEmpty()){
                    medtname.setError("Enter Name");
                }else
                if (email.isEmpty()){
                    medtmail.setError("Enter Email");
                }else
                if (id_number.isEmpty()){
                    medtidnumber.setError("Enter ID No.");
                }else {

                        long time = System.currentTimeMillis();
                        String timeconv = String.valueOf(time);
                    //erite code to create the table/a child/get the firebase database instance to save data
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/" + timeconv);

                    //use the itemconstructor to save data from the user
                    ItemConstructor x = new ItemConstructor(timeconv, name, email, id_number);
                    dialog.show();
                    ref.setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Saved Succesfully", Toast.LENGTH_SHORT).show();
                                clear();
                            } else {
                                Toast.makeText(MainActivity.this, "Saving Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });

        mbtnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(MainActivity.this,ViewusersActivity.class);
                startActivity(view);
            }
        });
    }
    public void clear(){
        medtname.setText("");
        medtmail.setText("");
        medtidnumber.setText("");
    }
}
