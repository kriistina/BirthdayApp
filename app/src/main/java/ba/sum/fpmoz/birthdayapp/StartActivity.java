package ba.sum.fpmoz.birthdayapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import ba.sum.fpmoz.birthdayapp.model.Rodjendan;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private RodjendanAdapter adapter;

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
        getSupportActionBar().hide();

        this.auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://birthdayapp-7729f-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("timetables/rodjendani");

        Button logoutBtn = findViewById(R.id.logoutBtn);
        ImageButton logoutBtn2 = findViewById(R.id.logoutBtn2);
        FloatingActionButton addNewRodjendanBtn = findViewById(R.id.addNewRodjendanBtn);

        FirebaseRecyclerOptions<Rodjendan> options = new FirebaseRecyclerOptions.Builder<Rodjendan>()
                .setQuery(mDatabase, Rodjendan.class)
                .build();

        adapter = new RodjendanAdapter(options);

        RecyclerView rodjendaniListItems = findViewById(R.id.rodjendaniListView1);
        rodjendaniListItems.setLayoutManager(new LinearLayoutManager(this));
        rodjendaniListItems.setAdapter(adapter);

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

        addNewRodjendanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddBirthdayActivity.class);
                startActivity(i);
            }
        });
    }
}
