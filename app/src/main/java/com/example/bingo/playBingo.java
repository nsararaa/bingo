package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class playBingo extends AppCompatActivity {


    void addBingoLetters(){



// Add "BINGO" letters to the lettersLayout
        GridLayout lettersLayout = findViewById(R.id.lettersLayout);
        String[] bingoLetters = {"B", "I", "N", "G", "O"};
        for (String letter : bingoLetters) {
            TextView letterView = new TextView(this);

            // Set properties for each letter
            letterView.setText(letter);
            letterView.setTextSize(24);
            // letterView.setTextColor(Color.BLUE);  // Set text color to blue
            letterView.setGravity(android.view.Gravity.CENTER);
            letterView.setBackgroundColor(Color.TRANSPARENT);
            letterView.setTypeface(null, Typeface.BOLD);  // Set text to bold

            // Set layout parameters
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) (getResources().getDisplayMetrics().density * 75); // Same width as EditText
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            letterView.setLayoutParams(params);

            // Add letter to lettersLayout
            lettersLayout.addView(letterView);  // Corrected line
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bingo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        //numbers passed from intent 1 (input bingo)
        String[][] numbers = (String[][]) getIntent().getSerializableExtra("numbers");


        addBingoLetters();

        //grid layout
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);

                //layout//ui parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 95; // 50dp width
                params.height = (int) getResources().getDisplayMetrics().density * 95; // 50dp height
                params.setMargins(8, 8, 8, 8);
                textView.setLayoutParams(params);

                //txt view properties
                textView.setText(numbers[row][col]);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.round);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setPadding(16, 16, 16, 16);


                textView.setOnClickListener(v -> {
                    String number = textView.getText().toString();
                    Toast.makeText(playBingo.this, "Clicked: " + number, Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(playBingo.this, quest.class);
                    intent.putExtra("inputNumber", number);
                    startActivity(intent);
                });

                gridLayout.addView(textView);
            }
        }
    }
}