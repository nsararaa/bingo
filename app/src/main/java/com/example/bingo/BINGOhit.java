package com.example.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BINGOhit extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingohit);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Your challenge, should you choose to accept it: is to make a shape (ie a square)");

        Button buttonPlay = findViewById(R.id.buttonPlay);
        Button buttonQuit = findViewById(R.id.buttonQuit);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BINGOhit.this, "Challenge Accepted! Let's play!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BINGOhit.this, "Challenge Declined. Exiting the game!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(BINGOhit.this, loginView.class); // Replace with your game activity
                startActivity(intent);
            }
        });
    }
}