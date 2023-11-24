package com.udc.actividad2crud.crud.crud.actividad2crud.crud1.crudapp.crudapp.appfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);


        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticarUsuario();
            }
        });
    }

    private void autenticarUsuario() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals("omar") && password.equals("12345")) {
            // Guardar el nombre de usuario en SharedPreferences
            guardarUsuarioAutenticado();

            // Iniciar la actividad principal (MainActivity)
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cerrar la actividad actual para que el usuario no pueda regresar
        } else {
            // Autenticación fallida, mostrar un mensaje de error
            showToast("Autenticación fallida. Verifica tus credenciales.");
        }
    }

    private void guardarUsuarioAutenticado() {
        // Guardar la información de autenticación en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", "omar");
        editor.apply();
    }

    private void showToast(String message) {
        // Método de utilidad para mostrar mensajes Toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean usuarioAutenticado(Context context) {
        // Verificar si el usuario está autenticado usando SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        return !username.isEmpty();
    }
}
