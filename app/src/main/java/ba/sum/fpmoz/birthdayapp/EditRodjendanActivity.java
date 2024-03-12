package ba.sum.fpmoz.birthdayapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

import ba.sum.fpmoz.birthdayapp.model.Rodjendan;

public class EditRodjendanActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rodjendan);

        getSupportActionBar().hide();

        final String key = getIntent().getStringExtra("RodjendanKey");

        mDatabase = FirebaseDatabase.getInstance("https://birthdayapp-7729f-default-rtdb.europe-west1.firebasedatabase.app/").getReference("timetables/recepti").child(key);
        storageReference = FirebaseStorage.getInstance("gs://birthdayapp-7729f.appspot.com").getReference();

        EditText editNaziv = findViewById(R.id.editRodjendanNaziv);
        EditText editDatum= findViewById(R.id.editRodjendanDatum);
        EditText editPoklon = findViewById(R.id.editRodjendanPoklon);
        ImageView novaSlika = findViewById(R.id.odaberiNovuSliku);
        Button editBtn = findViewById(R.id.editBtn);

        ActivityResultLauncher<String> mGetConcent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //ako je slika izabrana uzmi uri slike
                        if(result != null){
                            novaSlika.setImageURI(result);
                            imageUri = result;
                        }
                    }
                }
        );

        novaSlika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetConcent.launch("image/*");

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri != null){
                    StorageReference ref = storageReference
                            .child("slike/" + UUID.randomUUID().toString());

                    ref.putFile(imageUri);
                }

                Rodjendan r = new Rodjendan();
                r.naziv = editNaziv.getText().toString();
                r.datum = editDatum.getText().toString();
                r.poklon = editPoklon.getText().toString();
                r.slika = imageUri.toString();
                mDatabase.setValue(r);

                Intent i = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(i);
            }
        });

        this.mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Rodjendan rodjendan = snapshot.getValue(Rodjendan.class);
                editNaziv.setText(rodjendan.naziv);
                editDatum.setText(rodjendan.datum);
                editPoklon.setText(rodjendan.poklon);
                novaSlika.setImageURI(Uri.parse(rodjendan.slika));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });



    }
}