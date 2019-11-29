package com.example.myfirebasedatabase;


//CustomAdapter
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ItemConstructor> data;//modify here

    public CustomAdapter(Context mContext, ArrayList<ItemConstructor> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();// # of items in your arraylist
    }
    @Override
    public Object getItem(int position) {
        return data.get(position);// get the actual item
    }
    @Override
    public long getItemId(int id) {
        return id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_layout, parent,false);//modify here
            viewHolder = new ViewHolder();
            //modify this block of code
            viewHolder.mTvname = (TextView) convertView.findViewById(R.id.tvname);
            viewHolder.mtvmail = (TextView) convertView.findViewById(R.id.tvmail);
            viewHolder.mtvidnumber = (TextView) convertView.findViewById(R.id.tvnumber);
            viewHolder.mbtnupdate = convertView.findViewById(R.id.btnupdate);
            viewHolder.mtndelete = convertView.findViewById(R.id.btndelete);
            //Up to here
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //MODIFY THIS BLOCK OF CODE
        final ItemConstructor person = data.get(position);//modify here
        viewHolder.mTvname.setText(person.getName_column());//modify here
        viewHolder.mtvmail.setText(person.getEmail_column());//modify here
        viewHolder.mtvidnumber.setText(person.getId_number_column());//modify here
        viewHolder.mtndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to delete,navigate to your table and get a specific row using column id
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Deleting");
                builder.setMessage("Are you sure you to delete?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+person.getId_column());
                        ref.removeValue();
                        Toast.makeText(mContext, "Deleted Succesfully", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });

        viewHolder.mbtnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
                View view = inflater.inflate(R.layout.update_layout,null);

                final EditText uname = view.findViewById(R.id.edtupdatename);
                final EditText uemail = view.findViewById(R.id.edtupdateemail);
                final EditText uidnumber = view.findViewById(R.id.edtupdateid);

                uname.setText(person.getName_column());
                uemail.setText(person.getEmail_column());
                uidnumber.setText(person.getId_number_column());

                builder.setView(view);
                builder.setTitle("Editing Record!!");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = uname.getText().toString().trim();
                        String email = uemail.getText().toString().trim();
                        String idnumber = uidnumber.getText().toString().trim();

                        if (name.isEmpty()){
                            uname.setError("Enter Name");
                            return;
                        }
                        if (email.isEmpty()){
                            uemail.setError("Enter Email");
                            return;
                        }
                        if (idnumber.isEmpty()){
                            uidnumber.setError("Enter ID No.");
                            return;
                        }    else {
                             //proceed to update
                            person.setName_column(name);
                            person.setEmail_column(email);
                            person.setId_number_column(idnumber);
                            //connect to the db
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+person.getId_column());
                            ref.setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                  if (task.isSuccessful()){
                                      Toast.makeText(mContext, "Update Successful", Toast.LENGTH_SHORT).show();
                                  }  else {
                                      Toast.makeText(mContext, "Updating Failed", Toast.LENGTH_SHORT).show();
                                  }
                                }
                            });

                        }
                    }
                });
                builder.create().show();
            }

        });

        return convertView;

    }
    static class ViewHolder {
        TextView mTvname,mtvmail,mtvidnumber;
        Button mtndelete,mbtnupdate;

    }

}