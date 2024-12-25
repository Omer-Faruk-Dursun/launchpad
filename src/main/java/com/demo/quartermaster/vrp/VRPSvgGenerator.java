package com.demo.quartermaster.vrp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VRPSvgGenerator {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int LOCATION_BASE_SIZE = 20;
    private final int PADDING = 50;

    private Point[] calculateLocations(double[][] distanceMatrix) {
        int n = distanceMatrix.length;
        Point[] points = new Point[n];

        // Use MDS-like algorithm for 2D embedding
        double[][] coordinates = new double[n][2];

        // Initialize random positions
        Random rand = new Random(42); // Fixed seed for consistency
        for (int i = 0; i < n; i++) {
            coordinates[i][0] = rand.nextDouble();
            coordinates[i][1] = rand.nextDouble();
        }

        // Iterative optimization
        double learningRate = 0.1;
        for (int iter = 0; iter < 1000; iter++) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    double currentDist = Math.sqrt(
                            Math.pow(coordinates[i][0] - coordinates[j][0], 2) +
                                    Math.pow(coordinates[i][1] - coordinates[j][1], 2)
                    );

                    double targetDist = distanceMatrix[i][j] / 100.0; // Scale down the distances
                    double diff = currentDist - targetDist;

                    // Adjust positions
                    if (currentDist > 0.0001) {
                        double dx = (coordinates[i][0] - coordinates[j][0]) * diff / currentDist;
                        double dy = (coordinates[i][1] - coordinates[j][1]) * diff / currentDist;

                        coordinates[i][0] -= learningRate * dx;
                        coordinates[i][1] -= learningRate * dy;
                        coordinates[j][0] += learningRate * dx;
                        coordinates[j][1] += learningRate * dy;
                    }
                }
            }
            learningRate *= 0.995;
        }

        // Normalize to fit in SVG dimensions
        double minX = Arrays.stream(coordinates).mapToDouble(p -> p[0]).min().getAsDouble();
        double maxX = Arrays.stream(coordinates).mapToDouble(p -> p[0]).max().getAsDouble();
        double minY = Arrays.stream(coordinates).mapToDouble(p -> p[1]).min().getAsDouble();
        double maxY = Arrays.stream(coordinates).mapToDouble(p -> p[1]).max().getAsDouble();

        for (int i = 0; i < n; i++) {
            double x = (coordinates[i][0] - minX) / (maxX - minX) * (WIDTH - 2 * PADDING) + PADDING;
            double y = (coordinates[i][1] - minY) / (maxY - minY) * (HEIGHT - 2 * PADDING) + PADDING;
            points[i] = new Point((int)x, (int)y);
        }

        return points;
    }

    public String generateSvg(double[][] distanceMatrix, int[] demands, Solution solution) {
        Point[] locations = calculateLocations(distanceMatrix);

        StringBuilder svg = new StringBuilder();
        svg.append(String.format("<svg viewBox=\"0 0 %d %d\" xmlns=\"http://www.w3.org/2000/svg\">\n", WIDTH, HEIGHT));

        // Background
        svg.append(String.format("<rect width=\"%d\" height=\"%d\" fill=\"#f0f0f0\"/>\n", WIDTH, HEIGHT));

        // Grid
        appendGrid(svg);

        // Routes if solution exists
        if (solution != null) {
            appendRoutes(svg, solution, locations);
        }

        // Locations
        appendLocations(svg, locations, demands);

        // Legend
        appendLegend(svg);

        svg.append("</svg>");
        return svg.toString();
    }

    private void appendGrid(StringBuilder svg) {
        svg.append("<g stroke=\"#ddd\" stroke-width=\"1\">\n");
        for (int i = 100; i < HEIGHT; i += 100) {
            svg.append(String.format("<path d=\"M 0 %d L %d %d\"/>\n", i, WIDTH, i));
        }
        for (int i = 100; i < WIDTH; i += 100) {
            svg.append(String.format("<path d=\"M %d 0 L %d %d\"/>\n", i, i, HEIGHT));
        }
        svg.append("</g>\n");
    }

    private void appendLocations(StringBuilder svg, Point[] locations, int[] demands) {
        svg.append("<g>\n");

        for (int i = 0; i < locations.length; i++) {
            Point loc = locations[i];
            int size = i == 0 ? LOCATION_BASE_SIZE : LOCATION_BASE_SIZE + demands[i];
            String color = i == 0 ? "red" : "blue";

            // Location circle
            svg.append(String.format(
                    "<circle cx=\"%d\" cy=\"%d\" r=\"%d\" fill=\"%s\" opacity=\"0.7\"/>\n",
                    loc.x, loc.y, size, color
            ));

            // Location label with index
            svg.append(String.format(
                    "<text x=\"%d\" y=\"%d\" text-anchor=\"middle\" dy=\".3em\" fill=\"white\" font-weight=\"bold\">%d</text>\n",
                    loc.x, loc.y, i
            ));
        }

        svg.append("</g>\n");
    }

    private void appendRoutes(StringBuilder svg, Solution solution, Point[] locations) {
        svg.append("<g stroke-width=\"2\" fill=\"none\">\n");

        String[] colors = {"#ff0000", "#00ff00", "#0000ff", "#ff00ff", "#ffff00",
                "#ff8000", "#00ff80", "#8000ff", "#ff0080", "#80ff00"};

        for (int v = 0; v < solution.routes.length; v++) {
            List<Integer> route = solution.routes[v];
            if (route.isEmpty()) continue;

            String color = colors[v % colors.length];
            StringBuilder path = new StringBuilder();

            // Start from depot
            Point depot = locations[0];
            path.append(String.format("M %d %d ", depot.x, depot.y));

            // Add each location
            for (int loc : route) {
                Point point = locations[loc];
                path.append(String.format("L %d %d ", point.x, point.y));
            }

            // Back to depot
            path.append(String.format("L %d %d", depot.x, depot.y));

            svg.append(String.format("<path d=\"%s\" stroke=\"%s\"/>\n", path.toString(), color));
        }

        svg.append("</g>\n");
    }

    private void appendLegend(StringBuilder svg) {
        svg.append("<g transform=\"translate(650, 50)\">\n");
        svg.append("<circle cx=\"0\" cy=\"0\" r=\"20\" fill=\"red\" opacity=\"0.7\"/>\n");
        svg.append("<text x=\"30\" y=\"0\" dy=\".3em\">Depot</text>\n");
        svg.append("<circle cx=\"0\" cy=\"40\" r=\"20\" fill=\"blue\" opacity=\"0.7\"/>\n");
        svg.append("<text x=\"30\" y=\"40\" dy=\".3em\">Location</text>\n");
        svg.append("<text x=\"-30\" y=\"80\" dy=\".3em\">Circle size = Demand</text>\n");
        svg.append("</g>\n");
    }

    private static class Point {
        final int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Solution {
        public List<Integer>[] routes;

        public Solution(List<Integer>[] routes) {
            this.routes = routes;
        }
    }
}