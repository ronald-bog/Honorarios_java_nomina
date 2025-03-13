/*package com.sonria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BuscarVCSV {

    public static void csv() {
        // Iniciar el contador de tiempo
        long inicioTiempo = System.nanoTime();

        String archivo1 = "result.csv"; // Archivo principal (se modificará directamente)
        String archivo2 = "PLANES_TARIFARIOS.csv"; // Archivo de búsqueda (de donde se obtendrán los valores)

        int columnaBusquedaArchivo1 = 5; // Columna en archivo1 para buscar coincidencias (índice basado en 0)
        int columnaBusquedaArchivo2 = 5; // Columna en archivo2 para buscar coincidencias (índice basado en 0)
        int columnaCopiarArchivo2 = 6;   // Columna en archivo2 de donde se copiará el valor
        int columnaPegarArchivo1 = 6;    // Columna en archivo1 donde se pegará el valor

        // Mapa para almacenar los valores de búsqueda y copia del archivo2
        Map<String, String> mapaBusqueda = new ConcurrentHashMap<>();

        // Leer el archivo2 y almacenar los valores en un mapa
        try (BufferedReader br = new BufferedReader(new FileReader(archivo2))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(";");
                if (valores.length > columnaCopiarArchivo2) {
                    String clave = valores[columnaBusquedaArchivo2].trim(); // Valor de búsqueda
                    String valor = valores[columnaCopiarArchivo2].trim();   // Valor a copiar
                    mapaBusqueda.put(clave, valor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Crear un ExecutorService con un número de hilos igual al número de núcleos disponibles
        int numHilos = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numHilos);

        // Leer el archivo1 y almacenar las líneas en una lista
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo1))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Arreglo para almacenar las líneas procesadas en el orden original
        String[] lineasProcesadas = new String[lineas.size()];

        // Dividir las líneas en bloques para procesamiento concurrente
        int numLineas = lineas.size();
        int tamanoBloque = (numLineas + numHilos - 1) / numHilos; // Tamaño de cada bloque

        List<Callable<Void>> tareas = new ArrayList<>();
        for (int i = 0; i < numHilos; i++) {
            int inicio = i * tamanoBloque;
            int fin = Math.min(inicio + tamanoBloque, numLineas);

            // Verificar que el índice de inicio no sea mayor que el número de líneas
            if (inicio >= numLineas) {
                break; // Salir del bucle si no hay más líneas para procesar
            }

            List<String> subLista = lineas.subList(inicio, fin);

            // Crear una tarea para procesar el bloque de líneas
            Callable<Void> tarea = () -> {
                for (int j = 0; j < subLista.size(); j++) {
                    String lin = subLista.get(j);
                    String[] valores = lin.split(";");
                    if (valores.length > columnaPegarArchivo1) {
                        String clave = valores[columnaBusquedaArchivo1].trim(); // Valor de búsqueda en archivo1
                        // Si hay coincidencia, copiar el valor al archivo1
                        valores[columnaPegarArchivo1] = mapaBusqueda.getOrDefault(clave, "error");
                    }
                    // Reconstruir la línea actualizada y guardarla en la posición correcta
                    lineasProcesadas[inicio + j] = String.join(";", valores);
                }
                return null;
            };
            tareas.add(tarea);
        }

        // Ejecutar todas las tareas en paralelo
        try {
            executor.invokeAll(tareas);
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Sobrescribir el archivo1 con el contenido actualizado
        try (FileWriter fw = new FileWriter(archivo1)) {
            for (String linea : lineasProcesadas) {
                fw.write(linea + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Apagar el ExecutorService
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        // Calcular el tiempo total de ejecución
        long finTiempo = System.nanoTime();
        long duracion = finTiempo - inicioTiempo;
        double duracionSegundos = duracion / 1_000_000_000.0; // Convertir a segundos

        System.out.println("Proceso completado. Archivo1 actualizado.");
        System.out.printf("Tiempo total de ejecución: %.4f segundos%n", duracionSegundos);
    }
}
*/

/*package com.sonria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuscarVCSV {
    public static void csv() {
        String archivo1 = "archivo1.csv"; // Archivo principal (se modificará directamente)
        String archivo2 = "archivo2.csv"; // Archivo de búsqueda (de donde se obtendrán los valores)

        int columnaBusquedaArchivo1 = 2; // Columna en archivo1 para buscar coincidencias (índice basado en 0)
        int columnaBusquedaArchivo2 = 1; // Columna en archivo2 para buscar coincidencias (índice basado en 0)
        int columnaCopiarArchivo2 = 2;   // Columna en archivo2 de donde se copiará el valor
        int columnaPegarArchivo1 = 3;    // Columna en archivo1 donde se pegará el valor

        // Mapa para almacenar los valores de búsqueda y copia del archivo2
        Map<String, String> mapaBusqueda = new HashMap<>();

        // Leer el archivo2 y almacenar los valores en un mapa
        try (BufferedReader br = new BufferedReader(new FileReader(archivo2))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(";");
                if (valores.length > columnaCopiarArchivo2) {
                    String clave = valores[columnaBusquedaArchivo2].trim(); // Valor de búsqueda
                    String valor = valores[columnaCopiarArchivo2].trim();   // Valor a copiar
                    mapaBusqueda.put(clave, valor);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Leer el archivo1, buscar coincidencias y modificar las líneas
        StringBuilder contenidoActualizado = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo1))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(";");
                if (valores.length > columnaPegarArchivo1) {
                    String clave = valores[columnaBusquedaArchivo1].trim(); // Valor de búsqueda en archivo1
                    // Si hay coincidencia, copiar el valor al archivo1
                    // Si no hay coincidencia, dejar la columna vacía o con un valor predeterminado
                    // Puedes cambiar"" por un valor predeterminado
                    valores[columnaPegarArchivo1] = mapaBusqueda.getOrDefault(clave, "error");
                }
                // Reconstruir la línea actualizada
                contenidoActualizado.append(String.join(";", valores)).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Sobrescribir el archivo1 con el contenido actualizado
        try (FileWriter fw = new FileWriter(archivo1)) {
            fw.write(contenidoActualizado.toString());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("Proceso completado. Archivo1 actualizado.");
    }
}*/

