package com.example.proyectodsa_android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.CustomLevel;
import com.example.proyectodsa_android.models.ScoreData;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerGameActivity;

import java.io.IOException;

import retrofit2.Response;

public class UnityWrapperActivity extends UnityPlayerGameActivity {
    static ApiService apiService;
    static String token;
    static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("cookie");
        userId = getIntent().getStringExtra("userId");
        apiService = RetrofitClient.getInstance().getApi();
    }

    public static void sendNewLevel(String json){
        if(token == null || token.isEmpty()){
            sendToast("Unexpected internal error");
            return;
        }
        try{
            CustomLevel level = new GsonBuilder().create().fromJson(json, CustomLevel.class);
            Response<Void> r = apiService.uploadLevel(token, level).execute();
            if(!r.isSuccessful()){
                sendToast("Unexpected internal error");
            }else{
                closeActivity();
            }
        }catch(IOException ignored){
            sendToast("Unexpected internal error");
        }
    }

    public static void sendSL(String jsonData){
        Log.d("a", jsonData);
        if(token == null || token.isEmpty()){
            sendToast("Unexpected internal error");
            return;
        }
        try{
            ScoreData cl = new GsonBuilder().create().fromJson(jsonData, ScoreData.class);
            Response<Void> r = apiService.sendSL(token, userId, cl).execute();
            if(!r.isSuccessful()){
                sendToast("Unexpected internal error");
            }
        }catch(IOException ignored){
            sendToast("Unexpected internal error");
        }
    }

    public static void closeActivity(){
        UnityPlayer.currentActivity.finish();
    }

    public static void sendToast(String text){
        new Handler(Looper.getMainLooper()).post(() -> {
            Toast toast = Toast.makeText(UnityPlayer.currentActivity, text, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}
