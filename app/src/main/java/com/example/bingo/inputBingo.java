package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class inputBingo extends AppCompatActivity {
    private String[][] numbers = new String[5][5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_bingo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button setGridButton = findViewById(R.id.setGridButton);

        EditText[][] editTexts = new EditText[5][5];

        // Create the 5x5 grid of EditTexts
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                EditText editText = new EditText(this);

                // Set layout parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 95; // 50dp width
                params.height = (int) getResources().getDisplayMetrics().density * 95; // 50dp height
                params.setMargins(8, 8, 8, 8);
                editText.setLayoutParams(params);

                // Set properties for EditText
                editText.setHint("Num");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.setGravity(android.view.Gravity.CENTER);
                editText.setBackgroundResource(R.drawable.round);
                editText.setTextColor(Color.WHITE);
                editText.setTextSize(16);
                editText.setPadding(16, 16, 16, 16);

                gridLayout.addView(editText);
                editTexts[row][col] = editText;
            }
        }

        // Set the button's click listener
        setGridButton.setOnClickListener(v -> {
            boolean validInput = true;

            // Store input in the 2D array
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    String input = editTexts[row][col].getText().toString();
                    if (input.isEmpty()) {
                        validInput = false;
                        break;
                    }
                    numbers[row][col] = input;
                }
            }

//            if (!validInput) {
//                Toast.makeText(inputBingo.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }

            // Pass the 2D array to the next activity
            Intent intent = new Intent(inputBingo.this, playBingo.class);
            intent.putExtra("numbers", numbers);
            startActivity(intent);
        });
    }
}