package ba.sum.fpmoz.birthdayapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.birthdayapp.model.Rodjendan;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference mDatabase;
    FirebaseListAdapter<Rodjendan> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://birthdayapp-7729f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("timetables/rodjendani");

        Button logoutBtn = findViewById(R.id.logoutBtn);
        ImageButton logoutBtn2 = findViewById(R.id.logoutBtn2);
        Button addBirthdayBtn = findViewById(R.id.addEventBtn);

        FirebaseListOptions<Rodjendan> options = new FirebaseListOptions.Builder<Rodjendan>()
                .setQuery(mDatabase, Rodjendan.class)
                .setLayout(android.R.layout.simple_list_item_1)
                .build();

        this.adapter = new FirebaseListAdapter<Rodjendan> (options) {
            @Override
            protected void populateView(View v, Rodjendan model, int position) {
                TextView nameTxt = v.findViewById(android.R.id.text1);
                nameTxt.setText(model.getNaziv());
                TextView timeTxt = v.findViewById(android.R.id.text2);
                timeTxt.setText(model.getDatum());
            }
        };

        ListView rodjendaniListView = findViewById(R.id.rodjendaniListView);
        rodjendaniListView.setAdapter(adapter);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                auth.signOut();
            }
        });

        logoutBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                auth.signOut();
            }
        });

        addBirthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddBirthdayActivity.class);
                startActivity(i);
                auth.signOut();
            }
        });
    }
}
