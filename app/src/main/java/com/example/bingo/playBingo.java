package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.bingo.Question;

import org.json.JSONArray;
import org.json.JSONObject;

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

    List<Question> questions;

    void loadQuestions() {
        questions = new ArrayList<>();
        try (InputStream is = getAssets().open("questions.json")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            // read .json file
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            //string ->json
            String jsonString = jsonBuilder.toString();
            JSONArray jsonArray = new JSONArray(jsonString);

            //loop quests in .json
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String questionText = jsonObject.getString("questionText");
                JSONArray optionsArray = jsonObject.getJSONArray("options");
                String[] options = new String[optionsArray.length()];

                //options converted to array
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }

                int correctAnswerIndex = jsonObject.getInt("correctAnswerIndex");

                //add to list
                questions.add(new Question(questionText, options, correctAnswerIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void passToQuest(TextView textView ){
        textView.setOnClickListener(v -> {
            String number = textView.getText().toString();
            int questionIndex = Integer.parseInt(number) - 1; // Convert to 0-based index

            if (questionIndex >= 0 && questionIndex < questions.size()) {
                Question selectedQuestion = questions.get(questionIndex);

                Intent intent = new Intent(playBingo.this, quest.class);
                // Pass question data via intent
                intent.putExtra("questionText", selectedQuestion.getQuestionText());
                intent.putExtra("options", selectedQuestion.getOptions());
                intent.putExtra("correctAnswerIndex", selectedQuestion.getCorrectAnswerIndex());
                startActivity(intent);
            } else {
                Toast.makeText(playBingo.this, "No question for this number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bingo);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        //numbers passed from intent 1 (input bingo)
        String[][] numbers = (String[][]) getIntent().getSerializableExtra("numbers");

        loadQuestions();
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


               passToQuest(textView);

                gridLayout.addView(textView);
            }
        }
    }
}