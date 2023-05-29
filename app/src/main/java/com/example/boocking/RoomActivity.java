package com.example.boocking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.boocking.data.Hotel;
import com.example.boocking.data.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class RoomActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Spinner myspiner;
    private Toolbar toolbar;
    private LinearLayout layout;
    private ImageView imageView;
    private TextView nameView;
    private TableLayout table;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        myspiner = findViewById(R.id.spinner2);
        ArrayAdapter<String> myadapter = new ArrayAdapter<String>(RoomActivity.this, R.layout.spinner_item, getResources().getStringArray(R.array.names_spinner5));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspiner.setAdapter(myadapter);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = findViewById(R.id.hotel_content);
        imageView = findViewById(R.id.hotel_image);
        nameView = findViewById(R.id.hotel_name);
        table = findViewById(R.id.tableLayout);
        button = findViewById(R.id.date_button);

        LinearLayout.LayoutParams layoutParams_room = new LinearLayout.LayoutParams(
                1000, 800);
        LinearLayout.LayoutParams layoutParams_button = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Intent intent = getIntent();
        Hotel hotel = (Hotel) intent.getSerializableExtra("hotel_data");
        Room room = (Room) intent.getSerializableExtra("room_data");

        imageView.setLayoutParams(layoutParams_room);
        nameView.setText(room.getName() + " Room");
        StorageReference roomRef = storageRef.child(hotel.getName() + "/Rooms/" + room.getImage());
        try {
            File roomImage = File.createTempFile(room.getName(), "jpg");
            roomImage.deleteOnExit();
            roomRef.getFile(roomImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap roomBitmap = BitmapFactory.decodeFile(roomImage.getAbsolutePath());
                    Drawable roomDrawable = new BitmapDrawable(getResources(), roomBitmap);

                    imageView.setBackground(roomDrawable);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("image_get_err", "Error getting image");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        populateTable();
        populateTable2(room);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarConstraints.Builder constraints = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());
                MaterialDatePicker picker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select dates")
                        .setCalendarConstraints(constraints.build())
                        .build();
                picker.show(RoomActivity.this.getSupportFragmentManager(), "TAG");
            }
        });

        myspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (parentView.getItemAtPosition(position).toString().equals("Logout")) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
                if (parentView.getItemAtPosition(position).toString().equals("Reservations")) {
                    Intent intent = new Intent(getApplicationContext(), ReservationHistoryActivity.class);
                    startActivity(intent);
                }
                if (parentView.getItemAtPosition(position).toString().equals("Account")) {
                    Intent intent = new Intent(getApplicationContext(), Account_change.class);
                    startActivity(intent);
                }
                if (parentView.getItemAtPosition(position).toString().equals("Home")) {
                    Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    protected void populateTable() {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView column1TextView = new TextView(this);
        column1TextView.setText("PRICE");
        column1TextView.setPadding(0,20,20,20);
        tableRow.addView(column1TextView);

        TextView column2TextView = new TextView(this);
        column2TextView.setText("CAPACITY");
        column2TextView.setPadding(0,20,20,20);
        tableRow.addView(column2TextView);

        TextView column3TextView = new TextView(this);
        column3TextView.setText("BED COUNT");
        column3TextView.setPadding(0,20,20,20);
        tableRow.addView(column3TextView);

        TextView column4TextView = new TextView(this);
        column4TextView.setText("BED TYPE");
        column4TextView.setPadding(0,20,20,20);
        tableRow.addView(column4TextView);

        table.addView(tableRow);
    }

    protected void populateTable2(Room room) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView column1TextView = new TextView(this);
        column1TextView.setText(room.getPrice().toString());
        column1TextView.setPadding(0,0,20,20);
        tableRow.addView(column1TextView);

        TextView column2TextView = new TextView(this);
        column2TextView.setText(room.getCapacity());
        column2TextView.setPadding(0,0,20,20);
        tableRow.addView(column2TextView);

        TextView column3TextView = new TextView(this);
        column3TextView.setText(room.getBed_count().toString());
        column3TextView.setPadding(0,0,20,20);
        tableRow.addView(column3TextView);

        TextView column4TextView = new TextView(this);
        column4TextView.setText(room.getBed_type());
        column4TextView.setPadding(0,0,20,20);
        tableRow.addView(column4TextView);

        table.addView(tableRow);
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