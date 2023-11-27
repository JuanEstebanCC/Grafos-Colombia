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
        // Crear grafos ponderados para distancia y tiempo
        Graph<String, DefaultWeightedEdge> graphDistance = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        Graph<String, DefaultWeightedEdge> graphTime = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Cargar datos desde el archivo CSV
        loadGraphFromCSV(graphDistance, graphTime);

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
                    checkConnection(graphDistance, scanner);
                    break;
                case 2:
                    findAndPrintShortestPath(graphDistance, scanner, true);
                    break;
                case 3:
                    findAndPrintShortestPath(graphTime, scanner, false);
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }

        } while (choice != 4);
    }
    private static void loadGraphFromCSV(Graph<String, DefaultWeightedEdge> graphDistance, Graph<String, DefaultWeightedEdge> graphTime) {
        // Ruta del archivo CSV
        String csvFile = "C:\\Users\\santi\\Desktop\\Grafos-ED\\Grafos-Unal\\Grafos-EDD\\src\\main\\java\\org\\example\\data.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Separar la línea en datos: ciudadA, ciudadB, distancia, tiempo
                String[] data = line.split(",");
                String cityA = data[0].trim();
                String cityB = data[1].trim();
                double distance = Double.parseDouble(data[2]);
                double time = Double.parseDouble(data[3]);

                // Grafo de distancia
                graphDistance.addVertex(cityA);
                graphDistance.addVertex(cityB);
                DefaultWeightedEdge distanceEdge = graphDistance.addEdge(cityA, cityB);
                if (distanceEdge != null) {
                    graphDistance.setEdgeWeight(distanceEdge, distance);
                }

                // Grafo de tiempo
                graphTime.addVertex(cityA);
                graphTime.addVertex(cityB);
                DefaultWeightedEdge timeEdge = graphTime.addEdge(cityA, cityB);
                if (timeEdge != null) {
                    graphTime.setEdgeWeight(timeEdge, time);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void checkConnection(Graph<String, DefaultWeightedEdge> graph, Scanner scanner) {
        // Solicitar el nombre de la primera ciudad al usuario
        System.out.print("Ingrese el nombre de la primera ciudad: ");
        String cityA = scanner.next();

        // Solicitar el nombre de la segunda ciudad al usuario
        System.out.print("Ingrese el nombre de la segunda ciudad: ");
        String cityB = scanner.next();

        // Verificar si existe una conexión directa entre las dos ciudades en el grafo
        boolean connected = graph.containsEdge(cityA, cityB) || graph.containsEdge(cityB, cityA);

        // Informar al usuario sobre la conexión entre las ciudades
        if (connected) {
            System.out.println("Las ciudades están conectadas.");
        } else {
            System.out.println("Las ciudades no están conectadas.");
        }
    }

    private static void findAndPrintShortestPath(Graph<String, DefaultWeightedEdge> graph, Scanner scanner, boolean isDistance) {
        // Solicitar la ciudad de origen al usuario
        System.out.print("Ingrese la ciudad de origen: ");
        String sourceCity = scanner.next();

        // Solicitar la ciudad de destino al usuario
        System.out.print("Ingrese la ciudad de destino: ");
        String targetCity = scanner.next();

        // Verificar si ambas ciudades existen en el grafo
        if (!graph.containsVertex(sourceCity) || !graph.containsVertex(targetCity)) {
            System.out.println("Al menos una de las ciudades no existe en el grafo.");
            return;
        }

        // Inicializar el algoritmo de Dijkstra con el grafo proporcionado
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);

        // Obtener el camino más corto según la métrica (distancia o tiempo)
        List<DefaultWeightedEdge> shortestPath = (isDistance)
                ? dijkstra.getPath(sourceCity, targetCity).getEdgeList()
                : findShortestPathByTime(dijkstra, graph, sourceCity, targetCity);

        // Obtener el peso total del camino más corto
        double totalWeight = dijkstra.getPathWeight(sourceCity, targetCity);

        // Determinar la métrica actual (distancia o tiempo)
        String metric = (isDistance) ? "Distancia" : "Tiempo";

        // Imprimir el resultado del camino más corto
        if (shortestPath != null) {
            System.out.println("Camino más corto en " + metric + ":");
            for (DefaultWeightedEdge edge : shortestPath) {
                // Obtener la ciudad de origen, ciudad de destino y peso de la arista
                String edgeSource = graph.getEdgeSource(edge);
                String edgeTarget = graph.getEdgeTarget(edge);
                double edgeWeight = graph.getEdgeWeight(edge);

                // Imprimir la arista y su peso
                System.out.println(edgeSource + " -> " + edgeTarget + " (" + metric + ": " + edgeWeight + ")");
            }
            // Imprimir el peso total del camino más corto
            System.out.println(metric + " total: " + totalWeight);
        } else {
            // Informar si no hay camino disponible entre las ciudades
            System.out.println("No hay camino disponible en " + metric + " entre las ciudades.");
        }
    }

    // Parámetros para realizar la búsqueda del camino más corto en tiempo utilizando el algoritmo de Dijkstra. La instancia de DijkstraShortestPath ya contiene información sobre el grafo y el
    // cálculo del camino más corto se realiza desde sourceCity hacia targetCity. La función devuelve la lista de aristas que forman el camino más corto en términos de tiempo
    private static List<DefaultWeightedEdge> findShortestPathByTime(
            DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra,
            Graph<String, DefaultWeightedEdge> graph,
            String sourceCity,
            String targetCity) {
        // Utiliza el algoritmo de Dijkstra para encontrar el camino más corto en tiempo
        return dijkstra.getPath(sourceCity, targetCity).getEdgeList();
    }
}