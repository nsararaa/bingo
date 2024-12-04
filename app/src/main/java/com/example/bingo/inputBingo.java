package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class inputBingo extends AppCompatActivity {
    private String[][] numbers = new String[5][5];

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
        setContentView(R.layout.activity_input_bingo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        Button setGridButton = findViewById(R.id.setGridButton);

        EditText[][] editTexts = new EditText[5][5];


        addBingoLetters();



        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                EditText editText = new EditText(this);


                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 95; // 50dp width
                params.height = (int) getResources().getDisplayMetrics().density * 95; // 50dp height
                params.setMargins(8, 8, 8, 8);
                editText.setLayoutParams(params);

                // properties of edit txt
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

        //VALIDATION FOR DUP NUMBERS MISSING!!!!!!!
        setGridButton.setOnClickListener(v -> {
            boolean validInput = true;

            //input vals stored
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