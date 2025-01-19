package com.example.proyectodsa_android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectodsa_android.ApiService;
import com.example.proyectodsa_android.models.LoginRequest;
import com.example.proyectodsa_android.R;
import com.example.proyectodsa_android.RetrofitClient;
import com.example.proyectodsa_android.models.User;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etLoginIdentifier, etLoginPassword;
    private Button btnRegister, btnLogin;
    private TextView tvSwitchToLogin, tvSwitchToRegister;
    private ApiService apiService;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        initializeViews();
        apiService = RetrofitClient.getInstance().getApi();
        setupClickListeners();
    }

    private void initializeViews() {
        viewFlipper = findViewById(R.id.viewFlipper);

        // Register views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSwitchToLogin = findViewById(R.id.tvSwitchToLogin);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);


        // Login views
        etLoginIdentifier = findViewById(R.id.etLoginIdentifier);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSwitchToRegister = findViewById(R.id.tvSwitchToRegister);
    }

    private void setupClickListeners() {
        tvSwitchToLogin.setOnClickListener(v -> viewFlipper.setDisplayedChild(1));
        tvSwitchToRegister.setOnClickListener(v -> viewFlipper.setDisplayedChild(0));
        btnRegister.setOnClickListener(v -> handleRegister());
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Compruebe que todos los campos están rellenados
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Formato del nombre de usuario de autenticación
        if (!username.matches(USERNAME_PATTERN)) {
            etUsername.setError("Username can only contain letters and numbers");
            return;
        }

        // Formato del correo electrónico de verificación
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid e-mail address\n");
            return;
        }

        // Verificar la seguridad de la contraseña
        if (!isPasswordValid(password)) {
            etPassword.setError("Passwords must contain at least 8 characters, including upper and lower case letters, numbers and special characters\n");
            return;
        }

        // Comprueba que las dos contraseñas son iguales
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Inconsistent passwords entered twice\n");
            return;
        }

        User user = new User(username, password, email);


        Log.d("AuthActivity", "Registering user: " +
                "username=" + user.getUsername() +
                ", email=" + user.getMail() +
                ", password=" + user.getPassword());

        apiService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AuthActivity.this,
                            "Successful registration", Toast.LENGTH_SHORT).show();
                    viewFlipper.setDisplayedChild(1); // Switch to the login page
                    etLoginIdentifier.setText(username);
                    etLoginPassword.setText(password);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("AuthActivity", "Registration error: " + errorBody);

                        switch (response.code()) {
                            case 409:
                                etUsername.setError("Username already exists, please select another username");
                                Toast.makeText(AuthActivity.this,
                                        "Username already exists, please select another username",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 400:
                                Toast.makeText(AuthActivity.this,
                                        "The request is formatted incorrectly, please check the input",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 422:
                                Toast.makeText(AuthActivity.this,
                                        "Input validation failed, check all fields",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 500:
                                Toast.makeText(AuthActivity.this,
                                        "Server error, please try again later\n",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(AuthActivity.this,
                                        "Registration Failure\n: " + errorBody,
                                        Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(AuthActivity.this,
                                "Registration failed, please try again later",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("AuthActivity", "Network error", t);
                Toast.makeText(AuthActivity.this,
                        "network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

    private void handleLogin() {
        String identifier = etLoginIdentifier.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest;
        if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            loginRequest = new LoginRequest(null, identifier, password);
        } else {
            loginRequest = new LoginRequest(identifier, null, password);
        }

        apiService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    String token = response.headers().get("Set-Cookie");
                    handleLoginSuccess(response.body(), token);
                } else {
                    Toast.makeText(AuthActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AuthActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(User user, String rawToken) {

        Log.d("AuthActivity", "Raw token: " + rawToken);

        if (rawToken != null) {
            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
            prefs.edit()
                    .putString("username", user.getUsername())
                    .putString("token", rawToken)
                    .apply();


            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", user.getUsername());
            intent.putExtra("userID", user.getId());
            intent.putExtra("token", rawToken);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login failed: No token received", Toast.LENGTH_SHORT).show();
        }
    }
}