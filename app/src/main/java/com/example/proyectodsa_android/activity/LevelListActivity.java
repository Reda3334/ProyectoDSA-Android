package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.LevelAdapter;
import com.example.proyectodsa_android.models.CustomLevel;
import com.google.android.gms.common.util.JsonUtils;
import com.google.gson.Gson;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevelListActivity extends AppCompatActivity {

    private static final String TAG = "LevelListActivity";
    private RecyclerView recyclerView;
    private LevelAdapter levelAdapter;
    private String userID, username, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);

        userID = getIntent().getStringExtra("userID");
        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");

        recyclerView = findViewById(R.id.recyclerViewLevels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnBack = findViewById(R.id.btnBack);

        fetchLevels();

        btnBack.setOnClickListener(v -> finish());
    }

    private void fetchLevels() {
        ApiService apiService = RetrofitClient.getInstance().getApi();

        apiService.getLevels().enqueue(new Callback<List<CustomLevel>>() {
            @Override
            public void onResponse(Call<List<CustomLevel>> call, Response<List<CustomLevel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CustomLevel> levelList = response.body();
                    levelAdapter = new LevelAdapter(LevelListActivity.this, levelList);
                    levelAdapter.setOnItemClickListener(LevelListActivity.this::openGame);
                    Log.d("a", levelList.get(0).getLevelName());
                    recyclerView.setAdapter(levelAdapter);
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

    private void openGame(CustomLevel level){
        SharedPreferences prefs = getSharedPreferences("com.example.proyectodsa_android.v2.playerprefs", MODE_PRIVATE);
        prefs.edit().putString("sceneToLoad", "Main").apply();
        prefs.edit().putString("userId", userID).apply();
        prefs.edit().putString("playerName", username).apply();

        Intent intent = new Intent(this, UnityWrapperActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cookie", token);
        intent.putExtra("customLevel", new Gson().toJson(level));
        startActivity(intent);
    }


}
