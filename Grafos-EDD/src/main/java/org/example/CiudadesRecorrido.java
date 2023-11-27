package org.example;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class CiudadesRecorrido{

    public static void main(String[] args) {
        // Crear el grafo ponderado
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Cargar datos desde el archivo CSV
        loadGraphFromCSV(graph);

        // Menú de opciones
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nMenú de opciones:");
            System.out.println("1. Verificar si dos ciudades están conectadas");
            System.out.println("2. Camino más corto en distancia entre dos ciudades");
            System.out.println("3. Camino más corto en tiempo entre dos ciudades");
            System.out.println("4. Salir");
            System.out.print("Ingrese su elección: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    checkConnection(graph, scanner);
                    break;
                case 2:
                    findShortestPath(graph, scanner, "Distancia");
                    break;
                case 3:
                    findShortestPath(graph, scanner, "Tiempo");
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }

        } while (choice != 4);
    }

    private static void loadGraphFromCSV(Graph<String, DefaultWeightedEdge> graph) {
        String csvFile = "C:\\Users\\santi\\Desktop\\Grafos-ED\\Grafos-Unal\\Grafos-EDD\\src\\main\\java\\org\\example\\data.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String cityA = data[0];
                String cityB = data[1];
                double distance = Double.parseDouble(data[2]);
                double time = Double.parseDouble(data[3]);

                // Agregar ciudades como vértices al grafo
                graph.addVertex(cityA);
                graph.addVertex(cityB);

                // Agregar arista con peso (distancia)
                DefaultWeightedEdge edge = graph.addEdge(cityA, cityB);

                if (edge != null) {
                    // Verificar si la arista se agregó correctamente antes de establecer el peso
                    graph.setEdgeWeight(edge, distance); // Utilizamos el peso de distancia
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkConnection(Graph<String, DefaultWeightedEdge> graph, Scanner scanner) {
        System.out.print("Ingrese el nombre de la primera ciudad: ");
        String cityA = scanner.next();
        System.out.print("Ingrese el nombre de la segunda ciudad: ");
        String cityB = scanner.next();

        boolean connected = graph.containsEdge(cityA, cityB) || graph.containsEdge(cityB, cityA);

        if (connected) {
            System.out.println("Las ciudades están conectadas.");
        } else {
            System.out.println("Las ciudades no están conectadas.");
        }
    }

    private static void findShortestPath(Graph<String, DefaultWeightedEdge> graph, Scanner scanner, String weightType) {
        System.out.print("Ingrese el nombre de la ciudad de origen: ");
        String sourceCity = scanner.next();
        System.out.print("Ingrese el nombre de la ciudad de destino: ");
        String targetCity = scanner.next();

        // Verificar si las ciudades de origen y destino existen en el grafo
        if (graph.containsVertex(sourceCity) && graph.containsVertex(targetCity)) {
            // Utilizar el algoritmo de Dijkstra para encontrar el camino más corto en base a la distancia
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraDistance = new DijkstraShortestPath<>(graph);
            List<DefaultWeightedEdge> shortestPathByDistance = findShortestPathByDistance(dijkstraDistance, graph, sourceCity, targetCity);

            // Verificar si el camino es nulo
            if (shortestPathByDistance != null) {
                System.out.println("Camino más corto en distancia:");
                for (DefaultWeightedEdge edge : shortestPathByDistance) {
                    System.out.println(graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge));
                }
                System.out.println("Distancia total: " + dijkstraDistance.getPathWeight(sourceCity, targetCity));
            } else {
                System.out.println("No hay camino disponible en distancia entre las ciudades.");
            }

            // Utilizar el algoritmo de Dijkstra para encontrar el camino más corto en base al tiempo
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraTime = new DijkstraShortestPath<>(graph);
            List<DefaultWeightedEdge> shortestPathByTime = dijkstraTime.getPath(sourceCity, targetCity).getEdgeList();

            // Verificar si el camino es nulo
            if (shortestPathByTime != null) {
                System.out.println("\nCamino más corto en tiempo:");
                for (DefaultWeightedEdge edge : shortestPathByTime) {
                    System.out.println(graph.getEdgeSource(edge) + " -> " + graph.getEdgeTarget(edge));
                }
                System.out.println("Tiempo total: " + dijkstraTime.getPathWeight(sourceCity, targetCity));
            } else {
                System.out.println("No hay camino disponible en tiempo entre las ciudades.");
            }
        } else {
            System.out.println("Una de las ciudades no existe en el grafo.");
        }
    }

    private static List<DefaultWeightedEdge> findShortestPathByDistance(
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra,
            Graph<String, DefaultWeightedEdge> graph,
            String sourceCity,
            String targetCity) {
        return dijkstra.getPath(sourceCity, targetCity).getEdgeList();
    }
}
