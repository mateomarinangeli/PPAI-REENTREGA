package com.redsismica.demo.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    // DatabaseInitializer.java
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database/sismos.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            if (!tablaExiste(conn, "evento_sismico")) {
                System.out.println("Base vacía → Ejecutando schema.sql y data.sql...");
                ejecutarScript(conn, "/database/schema.sql");
                ejecutarScript(conn, "/database/data.sql");
            } else {
                System.out.println("Base ya inicializada.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean tablaExiste(Connection conn, String nombreTabla) throws Exception {
        var rs = conn.getMetaData().getTables(null, null, nombreTabla, null);
        return rs.next();
    }

    private static void ejecutarScript(Connection conn, String path) throws Exception {
        InputStream is = DatabaseInitializer.class.getResourceAsStream(path);
        if (is == null) throw new RuntimeException("No se encontró el archivo: " + path);

        // 1. Leer el archivo COMPLETO
        String fullScript = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);

        // 2. Dividir la cadena por el delimitador (;)
        String[] statements = fullScript.split(";");

        for (String statement : statements) {
            // Limpiar cada sentencia de saltos de línea y comentarios (--)
            String cleanedStatement = statement
                    .replaceAll("--.*", "") // Eliminar comentarios de fin de línea (--)
                    .trim();

            if (cleanedStatement.isEmpty()) {
                continue; // Saltar sentencias vacías o solo comentarios
            }

            System.out.println("Ejecutando: " + cleanedStatement.substring(0, Math.min(cleanedStatement.length(), 50)) + "...");

            try (Statement stmt = conn.createStatement()) {
                // 3. Ejecutar una única sentencia
                stmt.execute(cleanedStatement);
            } catch (Exception e) {
                System.err.println("Error al ejecutar SQL: " + cleanedStatement);
                throw e; // Lanza la excepción para detener y depurar
            }
        }
        System.out.println("✅ Script " + path + " ejecutado y finalizado con éxito.");
    }
}
