package com.example.proyectodsa_android.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectodsa_android.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.FAQAdapter;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.FAQ;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQActivity extends AppCompatActivity {
    private RecyclerView rvFAQs;
    private FAQAdapter faqAdapter;
    private ApiService apiService;
    private static final String TAG = "FAQActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        rvFAQs = findViewById(R.id.rvFAQs);
        ImageButton btnBack = findViewById(R.id.btnBack);

        apiService = RetrofitClient.getInstance().getApi();
        faqAdapter = new FAQAdapter();
        rvFAQs.setAdapter(faqAdapter);
        rvFAQs.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadFAQs();
    }

    private void loadFAQs() {
        Log.d(TAG, "Loading FAQs...");
        apiService.getFAQs().enqueue(new Callback<List<FAQ>>() {
            @Override
            public void onResponse(Call<List<FAQ>> call, Response<List<FAQ>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "FAQs loaded successfully: " + response.body().size() + " items");
                    faqAdapter.setFaqs(response.body());
                } else {
                    Log.e(TAG, "Error loading FAQs: " + response.code());
                    Toast.makeText(FAQActivity.this,
                            "Error loading FAQs: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FAQ>> call, Throwable t) {
                Log.e(TAG, "Failed to load FAQs", t);
                Toast.makeText(FAQActivity.this,
                        "Error loading FAQs: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}