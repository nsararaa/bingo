package com.example.bingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class loginView extends AppCompatActivity {

    static class User {
        String username;
        String password;
    }

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load users from JSON
        users = loadUsersFromJson();

        // Set up login button
        Button loginButton = findViewById(R.id.btn_login);
        EditText usernameField = findViewById(R.id.et_username);
        EditText passwordField = findViewById(R.id.et_password);

        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (isValidLogin(username, password)) {

                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(loginView.this, inputBingo.class);
                i.putExtra("USERNAME", username);
                startActivity(i);
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidLogin(String username, String password) {
        if (users == null) return false;
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return true;
            }
        }
        return false;
    }
    private List<User> loadUsersFromJson() {
        List<User> users = new ArrayList<>();
        try {
            // Open the file from the assets folder
            InputStream inputStream = getAssets().open("usernames.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // Convert the JSON file content to a string
            String json = new String(buffer, "UTF-8");

            // Parse the JSON string using JSONArray
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Create a new User object and populate it
                User user = new User();
                user.username = jsonObject.getString("username");
                user.password = jsonObject.getString("password");

                // Add the User to the list
                users.add(user);
            }
        } catch (IOException | JSONException e) {
            Log.e("loginView", "Error reading or parsing usernames.json", e);
        }
        return users;
    }
}