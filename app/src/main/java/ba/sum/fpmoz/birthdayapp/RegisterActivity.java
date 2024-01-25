package ba.sum.fpmoz.birthdayapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.auth = FirebaseAuth.getInstance();

        Button registerBtn = findViewById(R.id.registerUserBtn);
        EditText firstnameTxt = findViewById(R.id.firstNameTxt);
        EditText lastnameTxt = findViewById(R.id.lastNameTxt);
        EditText emailTxt = findViewById(R.id.registerEmailTxt);
        EditText passwordTxt = findViewById(R.id.registerPasswordTxt);
        EditText passwordConfirmTxt = findViewById(R.id.confirmRegisterTxt);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameTxt.getText().toString();
                String lastname = lastnameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                String confirmPassword = passwordConfirmTxt.getText().toString();

                if (firstname.equals("") || lastname.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Niste popunili sva polja!", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Pogrešna lozinka!", Toast.LENGTH_LONG).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Uspješna registracija", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                                finish();  // Zatvorite trenutnu aktivnost kako korisnik ne bi mogao natrag na ekran registracije
                            } else {
                                Toast.makeText(getApplicationContext(), "Neuspješna registracija. Pokušajte ponovno.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

        });
    }
}