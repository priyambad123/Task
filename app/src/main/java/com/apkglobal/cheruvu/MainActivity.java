package com.apkglobal.cheruvu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import com.apkglobal.cheruvu.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import static com.apkglobal.cheruvu.R.id.item_logout;
import static com.apkglobal.cheruvu.R.menu.main;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    Spinner spinner1, spinner2;
    EditText serail_no, farmrer_name, farmer_age;
    Button btn_submit, btn_viewData;
    private TextView txtDetails;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    List<Listdata> list;
    RecyclerView recyclerview;

    private String userId;

    String state[] = new String[]{"Mandal1", "Mandal2", "Mandal3"};
    String Mandal1[] = new String[]{"Village A", "Village B", "Village C"};
    String Mandal2[] = new String[]{"Village D", "Village E", "Village F"};
    String Mandal3[] = new String[]{"Village G", "Village H", "Village I"};

    ArrayAdapter<String> adap;
    ArrayAdapter<String> adap1;
    ArrayAdapter<String> adap2;
    ArrayAdapter<String> adap3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        serail_no = (EditText) findViewById(R.id.serial_no);
        farmer_age = (EditText) findViewById(R.id.farmer_age);
        farmrer_name = (EditText) findViewById(R.id.farmer_name);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        txtDetails = (TextView) findViewById(R.id.txt_user);
        btn_viewData = (Button) findViewById(R.id.btn_viewData);
        recyclerview = (RecyclerView) findViewById(R.id.rview);
        btn_viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, FarmerDataActivity.class);
                startActivity(intent);

            }
        });




        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");


        adap = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, state);

        spinner1.setAdapter(adap);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (state[i].equalsIgnoreCase("Mandal1")) {
                    adap1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Mandal1);
                    spinner2.setAdapter(adap1);

                }
                if (state[i].equalsIgnoreCase("Mandal2")) {
                    adap2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Mandal2);
                    spinner2.setAdapter(adap2);

                }

                if (state[i].equalsIgnoreCase("Mandal3")) {
                    adap3 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, Mandal3);
                    spinner2.setAdapter(adap3);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serial = serail_no.getText().toString();
                String name = farmrer_name.getText().toString();
                String age = farmer_age.getText().toString();
                String mandal = spinner1.getSelectedItem().toString();
                String village = spinner2.getSelectedItem().toString();
                age = age.trim();
                final int a = !age.equals("")?Integer.parseInt(age) : 0;
                if(serial.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must enter serial no", Toast.LENGTH_LONG).show();
                }
                else if(name.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must enter name", Toast.LENGTH_LONG).show();
                }
                else if(age.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must enter age", Toast.LENGTH_LONG).show();
                }
                else if(a>100 || a<0){
                    Toast.makeText(MainActivity.this, "You must enter age between 1 to 100", Toast.LENGTH_LONG).show();
                }
                else if(mandal.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must enter mandal", Toast.LENGTH_LONG).show();
                }
                else if(mandal=="select"){
                    Toast.makeText(MainActivity.this, "You must select mandal", Toast.LENGTH_LONG).show();
                }
                else if(village=="select" || village==""){
                    Toast.makeText(MainActivity.this, "You must select village", Toast.LENGTH_LONG).show();
                }
                else if(village.isEmpty()){
                    Toast.makeText(MainActivity.this, "You must enter village", Toast.LENGTH_LONG).show();
                }
                else{
                    if (TextUtils.isEmpty(userId)) {
                        createUser(serial, name, age, mandal, village);
                    } else {
                        updateUser(serial, name, age, mandal, village);
                    }

                }
                // Check for already existed userId

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case item_logout:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createUser(String serial, String name, String age, String mandal, String village) {
        // In real apps this userId should be fetched
        // by implementing firebase auth
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        User user = new User(serial, name, age, mandal, village);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }


                // clear edit text
                serail_no.setText("");
                farmrer_name.setText("");
                farmer_age.setText("");
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    private void updateUser(String serial, String name, String age, String mandal, String village) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(village))
            mFirebaseDatabase.child(userId).child("village").setValue(village);

        if (!TextUtils.isEmpty(serial))
            mFirebaseDatabase.child(userId).child("serial").setValue(serial);
        if (!TextUtils.isEmpty(mandal))
            mFirebaseDatabase.child(userId).child("mandal").setValue(mandal);
        if (!TextUtils.isEmpty(age))
            mFirebaseDatabase.child(userId).child("age").setValue(age);
        if (!TextUtils.isEmpty(name))
            mFirebaseDatabase.child(userId).child("name").setValue(name);
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}

