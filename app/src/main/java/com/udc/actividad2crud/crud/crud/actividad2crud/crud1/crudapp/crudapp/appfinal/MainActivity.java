package com.udc.actividad2crud.crud.crud.actividad2crud.crud1.crudapp.crudapp.appfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView contrasenaTextView;
    private TextView ultimaContrasenaTextView;
    private Spinner mayusculasSpinner;
    private Spinner minusculasSpinner;
    private Spinner numerosSpinner;
    private SharedPreferences sharedPreferences;
    private Button generarButton;
    private Button borrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si el usuario ya está autenticado
        if (!LoginActivity.usuarioAutenticado(this)) {

            // SI el usuario no está autenticado, mostrar la actividad de inicio de sesión
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cerrar la actividad actual para que el usuario no pueda regresar
            return;
        } else {
            // El usuario ya está autenticado, mostrar la actividad principal
            setContentView(R.layout.activity_main);
        }

        generarButton = findViewById(R.id.generarButton);
        borrarButton = findViewById(R.id.borrarButton);

        contrasenaTextView = findViewById(R.id.contrasenaTextView);
        ultimaContrasenaTextView = findViewById(R.id.ultimaContrasenaTextView);

        mayusculasSpinner = findViewById(R.id.mayusculasSpinner);
        minusculasSpinner = findViewById(R.id.minusculasSpinner);
        numerosSpinner = findViewById(R.id.numerosSpinner);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        // Inicializar los Spinners con opciones descriptivas y cantidades
        ArrayAdapter<CharSequence> cantidadAdapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_cantidad, android.R.layout.simple_spinner_item);
        cantidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (mayusculasSpinner != null) {
            mayusculasSpinner.setAdapter(cantidadAdapter);
        }

        if (minusculasSpinner != null) {
            minusculasSpinner.setAdapter(cantidadAdapter);
        }

        if (numerosSpinner != null) {
            numerosSpinner.setAdapter(cantidadAdapter);
        }

        // Recuperar la última contraseña generada al iniciar la actividad
        String ultimaContrasena = sharedPreferences.getString("ultima_contrasena", "");
        if (!ultimaContrasena.isEmpty()) {
            contrasenaTextView.setText("");
            ultimaContrasenaTextView.setText("Última contraseña generada: " + ultimaContrasena);
            ultimaContrasenaTextView.setVisibility(View.VISIBLE);
        } else {
            generarContrasena();
        }


        Log.d("MainActivity", "generarButton: " + generarButton);

        if (generarButton != null) {
            generarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MainActivity", "generarButton clicked");
                    generarContrasena();
                }
            });
        } else {
            Log.e("MainActivity", "generarButton is null");
        }

        Log.d("MainActivity", "After setting click listener for generarButton");

        if (borrarButton != null) {
            borrarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarContrasena();
                }
            });
        } else {
            Log.e("MainActivity", "borrarButton is null");
        }

        // Encuentra la ImageView en la actividad
        ImageView cerrarSesionImageView = findViewById(R.id.cerrarSesionButton);

        // Establece un OnClickListener para la ImageView
        cerrarSesionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        // Borrar la información de autenticación en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();

        // Volver a la vista de inicio de sesión
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad actual para que el usuario no pueda regresar
    }


    private void generarContrasena() {
        String mayusculasStr = obtenerCantidadSeleccionada(mayusculasSpinner);
        String minusculasStr = obtenerCantidadSeleccionada(minusculasSpinner);
        String numerosStr = obtenerCantidadSeleccionada(numerosSpinner);

        if (mayusculasStr.isEmpty() || minusculasStr.isEmpty() || numerosStr.isEmpty()) {
            Toast.makeText(this, "Por favor, elige la cantidad de cada tipo de caracter", Toast.LENGTH_SHORT).show();
            return;
        }

        int mayusculas = Integer.parseInt(mayusculasStr);
        int minusculas = Integer.parseInt(minusculasStr);
        int numeros = Integer.parseInt(numerosStr);

        int longitudTotal = mayusculas + minusculas + numeros;

        Log.d("MainActivity", "Before generating password");
        String contrasenaGenerada = generarContrasenaAleatoria(longitudTotal, mayusculas, minusculas, numeros);
        Log.d("MainActivity", "After generating password");

        contrasenaTextView.setText("Contraseña generada: " + contrasenaGenerada);

        if (!contrasenaGenerada.isEmpty()) {
            ultimaContrasenaTextView.setText("Última contraseña generada: " + contrasenaGenerada);
            ultimaContrasenaTextView.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ultima_contrasena", contrasenaGenerada);
            editor.apply();
        } else {
            Toast.makeText(this, "Error al generar la contraseña", Toast.LENGTH_SHORT).show();
        }
    }

    private void borrarContrasena() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ultima_contrasena");
        editor.apply();

        contrasenaTextView.setText("Contraseña borrada");
        ultimaContrasenaTextView.setVisibility(View.GONE);
        Toast.makeText(this, "Contraseña borrada", Toast.LENGTH_SHORT).show();
    }

    private String obtenerCantidadSeleccionada(Spinner spinner) {
        if (spinner != null && spinner.getSelectedItem() != null) {
            return spinner.getSelectedItem().toString().trim();
        } else {
            return "";
        }
    }



    private String generarContrasenaAleatoria(int longitud, int mayusculas, int minusculas, int numeros) {
        String caracteresMayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String caracteresMinusculas = "abcdefghijklmnopqrstuvwxyz";
        String caracteresNumeros = "0123456789";

        Random random = new Random();
        StringBuilder contrasena = new StringBuilder();

        for (int i = 0; i < mayusculas; i++) {
            int indice = random.nextInt(caracteresMayusculas.length());
            contrasena.append(caracteresMayusculas.charAt(indice));
        }

        for (int i = 0; i < minusculas; i++) {
            int indice = random.nextInt(caracteresMinusculas.length());
            contrasena.append(caracteresMinusculas.charAt(indice));
        }

        for (int i = 0; i < numeros; i++) {
            int indice = random.nextInt(caracteresNumeros.length());
            contrasena.append(caracteresNumeros.charAt(indice));
        }

        return contrasena.toString();
    }
}
