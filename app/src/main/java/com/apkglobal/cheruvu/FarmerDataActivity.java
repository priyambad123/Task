package com.apkglobal.cheruvu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FarmerDataActivity extends AppCompatActivity {
TextView farmer_data;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    List<Listdata> list;
    RecyclerView recyclerview;
    Button retrieve;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_data);
        recyclerview = (RecyclerView) findViewById(R.id.rview);
        retrieve =(Button)findViewById(R.id.retrieve);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list = new ArrayList<>();
                        for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                            Userdetails userdetails = dataSnapshot1.getValue(Userdetails.class);
                            Listdata listdata = new Listdata();
                            String serial=userdetails.getSerial();
                            String name=userdetails.getName();
                            String age=userdetails.getAge();
                            String mandal=userdetails.getMandal();
                            String village=userdetails.getVillage();
                            listdata.setSerial(serial);
                            listdata.setName(name);
                            listdata.setAge(age);
                            listdata.setMandal(mandal);
                            listdata.setVillage(village);
                            list.add(listdata);
                        }

                        RecyclerviewAdapter recycler = new RecyclerviewAdapter(list);
                        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(FarmerDataActivity.this);
                        recyclerview.setLayoutManager(layoutmanager);
                        recyclerview.setItemAnimator( new DefaultItemAnimator());
                        recyclerview.setAdapter(recycler);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });

            }
        });

    }
}
