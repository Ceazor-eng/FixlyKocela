package com.example.fixly;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    EditText usernametext, passwordtext;
    Button signinbutton;
    ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.loginactivity);

        usernametext = findViewById(R.id.username);
        passwordtext = findViewById(R.id.password);
        progressbar = findViewById(R.id.progressbar);
        passwordtext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        signinbutton = findViewById(R.id.signinbutton);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernametext.getText().toString().trim();
                String password = passwordtext.getText().toString().trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show progress dialog
               progressbar.setVisibility(View.VISIBLE);

                // Make API request
                new LoginTask().execute(username, password);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<String, Void, String> {
        private String username;

        @Override
        protected String doInBackground(String... params) {
            this.username = params[0]; // Store the username in a member variable

            // Construct the request data
            JSONObject postData = new JSONObject();
            try {
                postData.put("username", params[0]);
                postData.put("password", params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make the HTTP POST request
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(postData.toString(), MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:80/login_api.php")
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressbar.setVisibility(View.GONE);

            if (response == null) {
                // Handle null response (e.g., network error)
                Toast.makeText(LoginActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process API response
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                // Show appropriate message to the user
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                // If login is successful, navigate to the dashboard activity
                if (status.equals("success")) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("username", username); // Pass the username
                    startActivity(intent);
                    finish(); // Close the login activity to prevent user from going back
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON parsing error
            }
        }
    }
}
