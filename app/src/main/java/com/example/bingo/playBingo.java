package com.example.bingo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private HashMap<String, Integer> questionStates; // Keeps track of clicked questions
    private TextView lastClickedTextView; // To update the color after returning
    private String[][] questionGrid; // Stores the 5x5 question mapping
    List<Question> questions;
    private TextView[][] gridTextViews; // Store references to grid TextViews

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Complete game", Toast.LENGTH_SHORT).show();
    }

    void addBingoLetters() {
        GridLayout lettersLayout = findViewById(R.id.lettersLayout);
        String[] bingoLetters = {"B", "I", "N", "G", "O"};
        for (String letter : bingoLetters) {
            TextView letterView = new TextView(this);
            letterView.setText(letter);
            letterView.setTextSize(24);
            letterView.setGravity(android.view.Gravity.CENTER);
            letterView.setBackgroundColor(Color.TRANSPARENT);
            letterView.setTypeface(null, Typeface.BOLD);
            letterView.setTextColor(Color.parseColor("#9DB4C0"));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) (getResources().getDisplayMetrics().density * 70);
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            letterView.setLayoutParams(params);

            lettersLayout.addView(letterView);
        }
    }

    void loadQuestions() {
        questions = new ArrayList<>();
        try (InputStream is = getAssets().open("questions.json")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            String jsonString = jsonBuilder.toString();
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String questionText = jsonObject.getString("questionText");
                JSONArray optionsArray = jsonObject.getJSONArray("options");
                String[] options = new String[optionsArray.length()];

                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }

                int correctAnswerIndex = jsonObject.getInt("correctAnswerIndex");
                questions.add(new Question(questionText, options, correctAnswerIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_bingo);

        questionStates = new HashMap<>();
        questionGrid = (String[][]) getIntent().getSerializableExtra("numbers");
        gridTextViews = new TextView[5][5];
        loadQuestions();

        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "Failed to load questions.", Toast.LENGTH_SHORT).show();
            return;
        }

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        addBingoLetters();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                TextView textView = new TextView(this);
                gridTextViews[row][col] = textView;

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = (int) getResources().getDisplayMetrics().density * 87;
                params.height = (int) getResources().getDisplayMetrics().density * 87;
                params.setMargins(8, 8, 8, 8);
                textView.setLayoutParams(params);


                final String number = questionGrid[row][col];
                textView.setTypeface(null, Typeface.BOLD);
                textView.setText(number);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.round);
                //textView.setTextColor(Color.WHITE);
                textView.setTextColor(Color.parseColor("#253237"));
                textView.setTextSize(16);
                textView.setPadding(16, 16, 16, 16);

                textView.setOnClickListener(v -> {
                    if (questionStates.containsKey(number)) {
                        Toast.makeText(playBingo.this, "This question is already answered.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    lastClickedTextView = textView;

                    Intent intent = new Intent(playBingo.this, quest.class);
                    Question question = questions.get(Integer.parseInt(number) - 1);
                    intent.putExtra("questionText", question.getQuestionText());
                    intent.putExtra("options", question.getOptions());
                    intent.putExtra("correctAnswerIndex", question.getCorrectAnswerIndex());
                    intent.putExtra("questionNumber", number);
                    startActivityForResult(intent, QUEST_REQUEST_CODE);
                });

                gridLayout.addView(textView);
            }
        }
    }
    int hit_one =0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QUEST_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String questionNumber = data.getStringExtra("questionNumber");
            boolean answeredCorrectly = data.getBooleanExtra("answeredCorrectly", false);

            questionStates.put(questionNumber, answeredCorrectly ? 1 : 0);
            Log.d("QuestionStates", "Updated states: " + questionStates.toString());

            if (lastClickedTextView != null) {
                lastClickedTextView.setBackgroundResource(answeredCorrectly ? R.drawable.round_g : R.drawable.round_grey);
                lastClickedTextView.invalidate();
            }


            if (isBINGO() && hit_one == 0) {
                hit_one++;
                Intent i = new Intent(playBingo.this, BINGOhit.class);
                startActivity(i);
            }
        }
    }

    private boolean isBINGO() {
        // Check rows
        for (int row = 0; row < 5; row++) {
            boolean rowComplete = true;
            for (int col = 0; col < 5; col++) {
                String number = questionGrid[row][col];
                Integer state = questionStates.get(number);
                if (state == null || state != 1) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) return true;
        }

        // Check columns
        for (int col = 0; col < 5; col++) {
            boolean colComplete = true;
            for (int row = 0; row < 5; row++) {
                String number = questionGrid[row][col];
                Integer state = questionStates.get(number);
                if (state == null || state != 1) {
                    colComplete = false;
                    break;
                }
            }
            if (colComplete) return true;
        }

        // Check diagonal (top-left to bottom-right)
        boolean diagonal1Complete = true;
        for (int i = 0; i < 5; i++) {
            String number = questionGrid[i][i];
            Integer state = questionStates.get(number);
            if (state == null || state != 1) {
                diagonal1Complete = false;
                break;
            }
        }
        if (diagonal1Complete) return true;

        // Check diagonal (top-right to bottom-left)
        boolean diagonal2Complete = true;
        for (int i = 0; i < 5; i++) {
            String number = questionGrid[i][4-i];
            Integer state = questionStates.get(number);
            if (state == null || state != 1) {
                diagonal2Complete = false;
                break;
            }
        }
        return diagonal2Complete;
    }
}