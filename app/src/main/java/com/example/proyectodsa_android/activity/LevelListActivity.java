package com.example.proyectodsa_android.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.LevelAdapter;
import com.example.proyectodsa_android.models.CustomLevel;
import com.example.proyectodsa_android.models.Level;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LevelListActivity extends AppCompatActivity {

    private static final String TAG = "LevelListActivity";
    private String username;
    private TextView tvUsername;
    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;
    private List<CustomLevel> levelList = new ArrayList<>(); // Initialize empty list

    // Retrofit instance
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        // Initialize views
        tvUsername = findViewById(R.id.tvUsername);
        username = getIntent().getStringExtra("username");
        tvUsername.setText(username);

        // Initialize Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl("http://147.83.7.205/dsaApp/") // Your API base URL
                .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
                .build();

        recyclerView = findViewById(R.id.recyclerViewLevels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the adapter with an empty list
        levelAdapter = new LevelAdapter(this, levelList);
        recyclerView.setAdapter(levelAdapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Fetch levels from the API
        fetchLevels();
    }

    private void fetchLevels() {
        // Create ApiService using Retrofit instance
        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API call
        apiService.getLevels().enqueue(new Callback<List<CustomLevel>>() {
            @Override
            public void onResponse(Call<List<CustomLevel>> call, Response<List<CustomLevel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Clear existing data
                    levelList.clear();
                    // Add new data
                    levelList.addAll(response.body());
                    // Notify the adapter about data changes
                    levelAdapter.notifyDataSetChanged();
                    Log.d(TAG, "Received level list: " + levelList);
                } else {
                    Log.e(TAG, "Failed to fetch levels: " + response.code());
                    Toast.makeText(LevelListActivity.this, "Failed to load levels", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CustomLevel>> call, Throwable t) {
                Log.e(TAG, "Error fetching levels", t);
                Toast.makeText(LevelListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
