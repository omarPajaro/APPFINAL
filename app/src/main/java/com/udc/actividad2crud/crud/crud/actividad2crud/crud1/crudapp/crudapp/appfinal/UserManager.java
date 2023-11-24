package com.udc.actividad2crud.crud.crud.actividad2crud.crud1.crudapp.crudapp.appfinal;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private Map<String, String> usuarios; 

    public UserManager() {
        usuarios = new HashMap<>();
    }

    public boolean registrarUsuario(String usuario, String contrasena) {
        // Lógica para registrar un nuevo usuario
        if (!usuarios.containsKey(usuario)) {
            usuarios.put(usuario, contrasena);
            return true; // Registro exitoso
        } else {
            return false; // El usuario ya existe
        }
    }

    public boolean autenticarUsuario(String usuario, String contrasena) {
        // Lógica para autenticar al usuario
        return usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contrasena);
    }

    public String generarContrasena(int longitud, int mayusculas, int minusculas, int numeros) {
        // Lógica para generar la contraseña según las preferencias del usuario

        StringBuilder contrasena = new StringBuilder();

        for (int i = 0; i < mayusculas; i++) {
            contrasena.append(generarCaracterAleatorio('A', 'Z'));
        }

        for (int i = 0; i < minusculas; i++) {
            contrasena.append(generarCaracterAleatorio('a', 'z'));
        }

        for (int i = 0; i < numeros; i++) {
            contrasena.append(generarCaracterAleatorio('0', '9'));
        }

        int caracteresRestantes = longitud - (mayusculas + minusculas + numeros);

        for (int i = 0; i < caracteresRestantes; i++) {
            contrasena.append(generarCaracterAleatorio('A', 'z'));
        }

        return contrasena.toString();
    }

    private char generarCaracterAleatorio(char inicio, char fin) {
        // Lógica para generar un caracter aleatorio en el rango especificado
        return (char) (inicio + Math.random() * (fin - inicio + 1));
    }
}
