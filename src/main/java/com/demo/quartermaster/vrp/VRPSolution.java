package com.demo.quartermaster.vrp;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import java.util.*;

public class VRPSolution {
    private final Distances distances;
    private final VehicleRepository vehicleRepo;
    private RoutingIndexManager manager;
    private RoutingModel routing;

    public VRPSolution(Distances distances, VehicleRepository vehicleRepo) {
        this.distances = distances;
        this.vehicleRepo = vehicleRepo;

        // Initialize OR-Tools
        Loader.loadNativeLibraries();

        // Create Routing Index Manager
        int vehicleCount = vehicleRepo.getVehicleCount();
        int locationCount = distances.distanceMatrix.length;
        manager = new RoutingIndexManager(locationCount, vehicleCount, 0);

        // Create Routing Model
        routing = new RoutingModel(manager);

        // Define cost of each arc
        int transitCallbackIndex = routing.registerTransitCallback(
                (long fromIndex, long toIndex) -> {
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return (long) (distances.distanceMatrix[fromNode][toNode] * 100);
                });

        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        // Add Capacity constraint
        int demandCallbackIndex = routing.registerUnaryTransitCallback(
                (long fromIndex) -> {
                    int node = manager.indexToNode(fromIndex);
                    return distances.demands[node];
                });

        routing.addDimensionWithVehicleCapacity(
                demandCallbackIndex,
                0,  // null capacity slack
                createCapacityArray(vehicleRepo.getVehicleCount(), vehicleRepo.getVehicleCapacity()),
                true,  // start cumul to zero
                "Capacity");
    }

    private long[] createCapacityArray(int count, int capacity) {
        long[] capacities = new long[count];
        Arrays.fill(capacities, capacity);
        return capacities;
    }

    public void solve() {
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(com.google.protobuf.Duration.newBuilder().setSeconds(120).build())
                        .setSolutionLimit(500)
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);

        if (solution != null) {
            printSolution(solution);
        } else {
            System.out.println("No solution found.");
        }
    }

    private void printSolution(Assignment solution) {
        long totalDistance = 0;
        for (int vehicle = 0; vehicle < vehicleRepo.getVehicleCount(); vehicle++) {
            long index = routing.start(vehicle);
            System.out.printf("Route for vehicle %d:\n", vehicle);
            long routeDistance = 0;
            while (!routing.isEnd(index)) {
                System.out.printf(" %d ->", manager.indexToNode(index));
                long previousIndex = index;
                index = solution.value(routing.nextVar(index));
                routeDistance += routing.getArcCostForVehicle(previousIndex, index, vehicle);
            }
            System.out.printf(" %d\n", manager.indexToNode(index));
            System.out.printf("Distance of route: %s\n", routeDistance / 100.0);
            totalDistance += routeDistance;
        }
        System.out.printf("Total distance of all routes: %s\n", totalDistance / 100.0);
    }

    public static void main(String[] args) {
        Distances distances = new Distances();
        VehicleRepository vehicleRepo = new VehicleRepository();
        VRPSolution solution = new VRPSolution(distances, vehicleRepo);
        solution.solve();
    }
}