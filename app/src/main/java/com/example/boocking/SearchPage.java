package com.example.boocking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boocking.data.Hotel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageButton signout;
    private Toolbar toolbar;
    private LinearLayout layout;

    private void listHotel(Hotel hotel, LinearLayout.LayoutParams layoutParams_img, LinearLayout.LayoutParams layoutParams_desc) {
        ImageButton imageButton = new ImageButton(this);
        TextView title = new TextView(this);
        TextView description = new TextView(this);

        imageButton.setLayoutParams(layoutParams_img);
        title.setLayoutParams(layoutParams_desc);
        description.setLayoutParams(layoutParams_desc);

        imageButton.setBackgroundResource(R.drawable.hotel_panorama);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HotelActivity.class);
                intent.putExtra("hotel_data", hotel);
                startActivity(intent);
            }
        });

        title.setPadding(0, 10, 0 , 0);
        title.setText(hotel.getName());
        title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);

        description.setPadding(0, 0, 0, 10);
        description.setText(hotel.getLocation() + "\t\t\t\t\t\t" + hotel.getRating().toString());
        description.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f);
        description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.location_icon, 0, R.drawable.star_icon, 0);

        layout.addView(title);
        layout.addView(imageButton);
        layout.addView(description);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        signout = findViewById(R.id.signout_button2);
        layout = findViewById(R.id.hotel_content);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        LinearLayout.LayoutParams layoutParams_img = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams_img = new LinearLayout.LayoutParams(
                800, 600);
        LinearLayout.LayoutParams layoutParams_desc = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        db.collection("hotels").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Hotel hotel = document.toObject(Hotel.class);
                                listHotel(hotel, layoutParams_img, layoutParams_desc);
                                listHotel(hotel, layoutParams_img, layoutParams_desc);
                                listHotel(hotel, layoutParams_img, layoutParams_desc);
                                listHotel(hotel, layoutParams_img, layoutParams_desc);
                                listHotel(hotel, layoutParams_img, layoutParams_desc);
                            }
                        } else {
                            Log.w("hotel_info_err", "Error getting documents.", task.getException());
                        }
                    }
                });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}