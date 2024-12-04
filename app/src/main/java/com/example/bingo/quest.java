package com.example.bingo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class quest extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);


        String inputNumber = getIntent().getStringExtra("inputNumber");


        TextView questionText = findViewById(R.id.questionText);

        String question = "What is the capital of France? ";
        questionText.setText("Question" +inputNumber + ": "+ question);


        Button option1 = findViewById(R.id.option1);
        Button option2 = findViewById(R.id.option2);
        Button option3 = findViewById(R.id.option3);
        Button option4 = findViewById(R.id.option4);


        option1.setOnClickListener(v -> showToast("Correct!"));
        option2.setOnClickListener(v -> showToast("Incorrect!"));
        option3.setOnClickListener(v -> showToast("Incorrect!"));
        option4.setOnClickListener(v -> showToast("Incorrect!"));
    }

    //toast for correct/incorrect
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}