package org.example;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ciudadesRecorrido{

    public static void main(String[] args) {
        String filePath = "C:\\Users\\santi\\Desktop\\Grafos-ED\\Grafos-EDD\\src\\main\\java\\org\\example\\data.csv";  // Reemplaza con la ruta de tu archivo

        Graph<String, DefaultWeightedEdge> grafo = cargarGrafoDesdeArchivo(filePath);

       // verificarConexion(grafo);
       // encontrarCaminoDistancia(grafo);
        encontrarCaminoTiempo(grafo);
    }

    private static Graph<String, DefaultWeightedEdge> cargarGrafoDesdeArchivo(String filePath) {
        Graph<String, DefaultWeightedEdge> grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String ciudadA = partes[0].trim();
                String ciudadB = partes[1].trim();
                double distancia = Double.parseDouble(partes[2].trim());
                double tiempo = Double.parseDouble(partes[3].trim()); // Usa el valor de tiempo

                grafo.addVertex(ciudadA);
                grafo.addVertex(ciudadB);

                DefaultWeightedEdge edge = grafo.addEdge(ciudadA, ciudadB);
                grafo.setEdgeWeight(edge, distancia);  // Puedes cambiar a tiempo si lo prefieres
            }
        }
        catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo", e);
        }

        return grafo;
    }
    private static void verificarConexion(Graph<String, DefaultWeightedEdge> grafo) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Verificar si estan conectadas por una única carretera --- ");
        System.out.println("Ingresa el nombre de la ciudad A:");
        String ciudadA = scanner.nextLine().trim();

        System.out.println("Ingresa el nombre de la ciudad B:");
        String ciudadB = scanner.nextLine().trim();

        if (grafo.containsVertex(ciudadA) && grafo.containsVertex(ciudadB)) {
            boolean conexion = grafo.containsEdge(ciudadA, ciudadB) || grafo.containsEdge(ciudadB, ciudadA);
            if (conexion) {
                System.out.println("Las ciudades están conectadas por una única carretera.");
            } else {
                System.out.println("Las ciudades no están conectadas por una única carretera.");
            }
        } else {
            System.out.println("Al menos una de las ciudades no existe en el grafo.");
        }
    }

    private static void encontrarCaminoDistancia(Graph<String, DefaultWeightedEdge> grafo) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Verificar el camino mas corto en distancia ---");
        System.out.println("Ingresa el nombre de la ciudad A:");
        String ciudadA = scanner.nextLine().trim();

        System.out.println("Ingresa el nombre de la ciudad B:");
        String ciudadB = scanner.nextLine().trim();

        if (grafo.containsVertex(ciudadA) && grafo.containsVertex(ciudadB)) {
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
                    new DijkstraShortestPath<>(grafo);
            double distancia = dijkstra.getPathWeight(ciudadA, ciudadB);

            if (distancia < Double.POSITIVE_INFINITY) {
                System.out.println("El camino más corto en distancia es: " + distancia + " kilómetros.");
            } else {
                System.out.println("No hay camino entre las ciudades.");
            }
        } else {
            System.out.println("Al menos una de las ciudades no existe en el grafo.");
        }
    }

    private static void encontrarCaminoTiempo(Graph<String, DefaultWeightedEdge> grafo) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingresa el nombre de la ciudad A:");
        String ciudadA = scanner.nextLine().trim();

        System.out.println("Ingresa el nombre de la ciudad B:");
        String ciudadB = scanner.nextLine().trim();

        if (grafo.containsVertex(ciudadA) && grafo.containsVertex(ciudadB)) {
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
                    new DijkstraShortestPath<>(grafo);
            GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(ciudadA, ciudadB);

            if (path != null) {
                double tiempo = calcularTiempoTotal(path, grafo);
                System.out.println("El camino más corto en tiempo es el de : " + tiempo + " kilometros.");
            } else {
                System.out.println("No hay camino entre las ciudades.");
            }
        } else {
            System.out.println("Al menos una de las ciudades no existe en el grafo.");
        }
    }
    private static double calcularTiempoTotal(GraphPath<String, DefaultWeightedEdge> path, Graph<String, DefaultWeightedEdge> grafo) {
        double tiempoTotal = 0;
        List<DefaultWeightedEdge> edges = path.getEdgeList();

        for (DefaultWeightedEdge edge : edges) {
            tiempoTotal += grafo.getEdgeWeight(edge);
        }

        return tiempoTotal;

    }
}