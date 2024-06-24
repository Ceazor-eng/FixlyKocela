package com.example.fixly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    EditText emailEditText, usernameEditText, phoneNumberEditText, passwordEditText, confirmPasswordEditText;
    Button signupButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.signupactivity);

        emailEditText = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressbar);
        usernameEditText = findViewById(R.id.username);
        phoneNumberEditText = findViewById(R.id.phonenumber);
        passwordEditText = findViewById(R.id.password);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordEditText = findViewById(R.id.confirmpassword);
        confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        TextView signupLink = findViewById(R.id.signuplink);
        signupButton = findViewById(R.id.signupbutton);

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Validate input
                if (email.isEmpty() || username.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Start the signup task
                new SignupTask().execute(username, email, phoneNumber, password);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class SignupTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // Construct the request data
            JSONObject postData = new JSONObject();
            try {
                postData.put("username", params[0]);
                postData.put("email", params[1]);
                postData.put("phone_number", params[2]);
                postData.put("password", params[3]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Make the HTTP POST request
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(postData.toString(), MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:80/register.php") // Replace localhost with 10.0.2.2
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
            progressBar.setVisibility(View.GONE);

            if (response == null) {
                Toast.makeText(MainActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
                return;
            }

            // Log the response to see what is coming from the server
            Log.d("SignupResponse", response);

            // Process API response
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                // Show appropriate message to the user
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                // If registration is successful, navigate to the next activity
                if (status.equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
