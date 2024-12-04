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
import java.util.HashMap;
import java.util.List;

import com.example.bingo.Question;

import org.json.JSONArray;
import org.json.JSONObject;

public class playBingo extends AppCompatActivity {

    private static final int QUEST_REQUEST_CODE = 1;
    private HashMap<String, Boolean> questionStates; // Keeps track of clicked questions
    private TextView lastClickedTextView; // To update the color after returning


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

        questionStates = new HashMap<>(); // Initialize tracking map
        loadQuestions();

        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            return;
        }

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        String[][] numbers = (String[][]) getIntent().getSerializableExtra("numbers");

        addBingoLetters();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);

                // Layout/UI parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 95;
                params.height = (int) getResources().getDisplayMetrics().density * 95;
                params.setMargins(8, 8, 8, 8);
                textView.setLayoutParams(params);

                // TextView properties
                String number = numbers[row][col];
                textView.setText(number);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.round);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
                textView.setPadding(16, 16, 16, 16);

                textView.setOnClickListener(v -> {
                    // Check if the question has already been clicked
                    if (questionStates.containsKey(number)) {
                        Toast.makeText(playBingo.this, "This question is already answered.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    lastClickedTextView = textView; // Save reference for color update

                    Intent intent = new Intent(playBingo.this, quest.class);
                    Question question = questions.get(Integer.parseInt(number) - 1);
                    intent.putExtra("questionText", question.getQuestionText());
                    intent.putExtra("options", question.getOptions());
                    intent.putExtra("correctAnswerIndex", question.getCorrectAnswerIndex());
                    intent.putExtra("questionNumber", number); // Pass question number
                    startActivityForResult(intent, QUEST_REQUEST_CODE);
                });

                gridLayout.addView(textView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUEST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String questionNumber = data.getStringExtra("questionNumber");
            boolean answeredCorrectly = data.getBooleanExtra("answeredCorrectly", false);

            // Mark question as clicked
            questionStates.put(questionNumber, answeredCorrectly);

            // Update TextView color
            if (lastClickedTextView != null) {
                if (answeredCorrectly) {
                    lastClickedTextView.setBackgroundColor(Color.GREEN);
                } else {
                    lastClickedTextView.setBackgroundColor(Color.GRAY);
                }
            }
        }
    }
}