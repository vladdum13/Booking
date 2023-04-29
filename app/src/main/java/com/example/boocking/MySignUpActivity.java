package com.example.boocking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MySignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextName,editTextPassword, editTextVerify, editTextEmail, editTextPhone;
    private Button signup, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.signup_name);
        editTextPassword = findViewById(R.id.signup_password);
        editTextVerify = findViewById(R.id.signup_verify);
        editTextEmail = findViewById(R.id.signup_email);
        editTextPhone = findViewById(R.id.signup_phone);
        signup = findViewById(R.id.signup_button);
        back = findViewById(R.id.back_button2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, password, verify, email, phone;
                name = String.valueOf(editTextName.getText());
                password = String.valueOf(editTextPassword.getText());
                verify = String.valueOf(editTextVerify.getText());
                email = String.valueOf(editTextEmail.getText());
                phone = String.valueOf(editTextPhone.getText());

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(verify) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "Complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(verify)) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}