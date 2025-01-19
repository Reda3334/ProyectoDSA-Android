package com.example.proyectodsa_android.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.QuestionPayload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubmitQuestionActivity extends AppCompatActivity {

    private EditText etTitle, etMessage;
    private Button btnSubmit, btnBackToHome;
    private ApiService apiService;
    private String userID, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_question);

        etTitle = findViewById(R.id.etTitle);
        etMessage = findViewById(R.id.etMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        apiService = RetrofitClient.getInstance().getApi();


        userID = getIntent().getStringExtra("userID");
        username = getIntent().getStringExtra("username");

        if (userID == null || username == null) {
            Toast.makeText(this, "User ID or Username not found!", Toast.LENGTH_SHORT).show();
            return;
        }


        btnSubmit.setOnClickListener(v -> submitQuestion());


        btnBackToHome.setOnClickListener(v -> finish());
    }

    private void submitQuestion() {
        String title = etTitle.getText().toString().trim();
        String message = etMessage.getText().toString().trim();

        if (title.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        QuestionPayload payload = new QuestionPayload(date, userID, title, message, username);

        Log.d("SubmitQuestionActivity", "Sending to backend: " +
                "date=" + date + ", title=" + title + ", message=" + message + ", sender=" + userID + ", username=" + username);

        apiService.postQuestion(payload).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SubmitQuestionActivity.this, "Question submitted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SubmitQuestionActivity.this, "Submission failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SubmitQuestionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SubmitQuestionActivity", "Error submitting question", t);
            }
        });
    }
}