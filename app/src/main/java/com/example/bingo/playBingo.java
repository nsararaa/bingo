package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class playBingo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bingo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        // Get the numbers from the intent
        String[][] numbers = (String[][]) getIntent().getSerializableExtra("numbers");

        // Create the 5x5 grid of TextViews
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);

                // Set layout parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 95; // 50dp width
                params.height = (int) getResources().getDisplayMetrics().density * 95; // 50dp height
                params.setMargins(8, 8, 8, 8);
                textView.setLayoutParams(params);

                // Set properties for TextView
                textView.setText(numbers[row][col]);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.round);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setPadding(16, 16, 16, 16);

                // Set click listener
                textView.setOnClickListener(v -> {
                    String number = textView.getText().toString();
                    Toast.makeText(playBingo.this, "Clicked: " + number, Toast.LENGTH_SHORT).show();

                    // Navigate to quest activity
                    Intent intent = new Intent(playBingo.this, quest.class);
                    intent.putExtra("inputNumber", number);
                    startActivity(intent);
                });

                gridLayout.addView(textView);
            }
        }
    }
}