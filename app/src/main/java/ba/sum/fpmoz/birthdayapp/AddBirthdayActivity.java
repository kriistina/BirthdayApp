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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import ba.sum.fpmoz.birthdayapp.model.Rodjendan;

import java.util.UUID;

public class AddBirthdayActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_birthday);

        getSupportActionBar().hide();

        mDatabase = FirebaseDatabase.getInstance("https://birthdayapp-7729f-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("timetables/rodjendani");

        storageReference = FirebaseStorage.getInstance("gs://birthdayapp-7729f.appspot.com").getReference();

        EditText birthdayNameTxt = findViewById(R.id.birthdayNameTxt);
        EditText birthdayDateTxt = findViewById(R.id.birthdayDatext);
        EditText giftIdeaTxt = findViewById(R.id.giftIdeaTxt);
        Button addBirthdayBtn = findViewById(R.id.addBirthdayBtn);
        ImageView odaberiSliku = findViewById(R.id.odaberiSliku);

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // Ako je slika izabrana, uzmi URI slike
                        if (result != null) {
                            odaberiSliku.setImageURI(result);
                            imageUri = result;
                        }
                    }
                }
        );

        odaberiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        addBirthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    StorageReference ref = storageReference
                            .child("slike/" + UUID.randomUUID().toString());

                    ref.putFile(imageUri)
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // Dohvati URL za preuzimanje slike
                                        Uri downloadUri = task.getResult().getUploadSessionUri(); // Promijenjeno ovdje
                                        // Spremi podatke u Firebase Realtime Database
                                        String slika = downloadUri.toString();
                                        Rodjendan noviRodjendan = new Rodjendan(birthdayNameTxt.getText().toString(), birthdayDateTxt.getText().toString(), giftIdeaTxt.getText().toString(), slika);

                                        mDatabase.push().setValue(noviRodjendan)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            birthdayNameTxt.setText("");
                                                            birthdayDateTxt.setText("");
                                                            giftIdeaTxt.setText("");

                                                            Intent i = new Intent(getApplicationContext(), StartActivity.class);
                                                            startActivity(i);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Greška pri spremanju podataka", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Greška pri uploadu slike", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getApplicationContext(), "Molimo odaberite sliku", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
