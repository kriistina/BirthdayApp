package ba.sum.fpmoz.birthdayapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RodjendanActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rodjendan);

        getSupportActionBar().hide();

        final String key = getIntent().getStringExtra("RodjendanKey");

        mDatabase = FirebaseDatabase.getInstance("https://birthdayapp-7729f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("timetables/recepti").child(key);

        TextView naziv = findViewById(R.id.nazivTxt);
        TextView datum = findViewById(R.id.datum);
        TextView poklon = findViewById(R.id.poklon);
        ImageView rodjendanImage = findViewById(R.id.rodjendanImageView);
        FloatingActionButton shareBtn = findViewById(R.id.shareBtn);


        this.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nazivRodjendana = snapshot.child("naziv").getValue().toString();
                String datumRodjendana = snapshot.child("datum").getValue().toString();
                String poklonRodjendana = snapshot.child("poklon").getValue().toString();
                String slikaRodjendana = snapshot.child("slika").getValue().toString();

                Glide.with(rodjendanImage.getContext()).load(slikaRodjendana).into(rodjendanImage);
                naziv.setText(nazivRodjendana);
                datum.setText(datumRodjendana);
                poklon.setText(poklonRodjendana);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String text = "Podijeli rodjendan";
                i.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(i, "Podijeli"));

            }
        });

    }


}