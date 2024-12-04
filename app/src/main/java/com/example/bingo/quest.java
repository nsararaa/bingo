package com.example.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class quest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        String questionTextStr = getIntent().getStringExtra("questionText");
        String[] options = getIntent().getStringArrayExtra("options");
        int correctAnswerIndex = getIntent().getIntExtra("correctAnswerIndex", -1);

        TextView questionText = findViewById(R.id.questionText);
        questionText.setText(questionTextStr);

        Button[] optionButtons = {
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4)
        };

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.length) {
                optionButtons[i].setText(options[i]);
                int finalI = i;
                optionButtons[i].setOnClickListener(v -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("questionNumber", getIntent().getStringExtra("questionNumber"));
                    if (finalI == correctAnswerIndex) {
                        showToast("Correct!");
                        resultIntent.putExtra("answeredCorrectly", true);
                    } else {
                        showToast("Incorrect!");
                        resultIntent.putExtra("answeredCorrectly", false);
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close quest activity
                });
            } else {
                optionButtons[i].setVisibility(View.GONE);//if less than 4 options (shouldnt go into this)
            }
        }
    }


    @Override
    public void onBackPressed() {
        // Prevent going back unless explicitly allowed
        Toast.makeText(this, "Select an answer", Toast.LENGTH_SHORT).show();
    }

    //toast for correct/incorrect
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}