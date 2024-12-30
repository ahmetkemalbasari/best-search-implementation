package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int[][] goalState = {{0,1,2},{3,4,5},{6,7,8}};
    public static Set<List<List<Integer>>> seenStates = new HashSet<>();
    public static int maxFringeSize = 1;

    private static void swap(int[][] pieces, int x1, int y1, int x2, int y2) {
        int temp = pieces[x1][y1];
        pieces[x1][y1] = pieces[x2][y2];
        pieces[x2][y2] = temp;
    }

    public static boolean isUnique(int[][] state) {
        List<List<Integer>> stateAsList = Arrays.stream(state)
                .map(row -> Arrays.stream(row).boxed().collect(Collectors.toList()))
                .collect(Collectors.toList());

        return seenStates.add(stateAsList);
    }

    public static int hFunction(int[][] state){
        int distance = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int value = state[i][j];
                if(value == 0)
                    continue;
                int goalX = value/3;
                int goalY = value%3;
                distance += Math.abs(goalX-i) + Math.abs(goalY-j);
            }
        }
        return distance;
    }

    private static int[][] deepCopy(int[][] pieces) {
        return Arrays.stream(pieces).map(int[]::clone).toArray(int[][]::new);
    }

    public static boolean isValidMove(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    private static boolean isGoalState(int[][] state) {
        return Arrays.deepEquals(state, goalState);
    }

    public static void greedySearch(int[][] initialState){
        PriorityQueue<State> stateQueue = new PriorityQueue<State>();
        int zeroPosX = 0, zeroPosY = 0;
        int initialHeuristic = hFunction(initialState);

        for (int i = 0; i < initialState.length; i++) {
            for (int j = 0; j < initialState[i].length; j++) {
                if(initialState[i][j] == 0){
                    zeroPosX = i;
                    zeroPosY = j;
                    break;
                }
            }
        }
        stateQueue.add(new State(initialState, initialHeuristic, zeroPosX, zeroPosY));
        int count = 0;
        while(!stateQueue.isEmpty()){
            if(count++ % 100000 == 0)
                System.out.println(count);
            State currentState = stateQueue.poll();
            if(isGoalState(currentState.pieces)){
                System.out.print("I've found a SOLUTION!");
                System.out.println("Max fringe: " + maxFringeSize);
                return;
            }

            for (int[] move : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int newX = currentState.zeroPosX + move[0];
                int newY = currentState.zeroPosY + move[1];
                if (isValidMove(newX, newY)) {

                    int[][] newPieces = deepCopy(currentState.pieces);
                    swap(newPieces, currentState.zeroPosX, currentState.zeroPosY, newX, newY);
                    if(!isUnique(newPieces))
                        continue;
                    int heuristic = hFunction(newPieces);
                    stateQueue.add(new State(newPieces, heuristic, newX, newY));
                }
            }
            maxFringeSize = Math.max(maxFringeSize, stateQueue.size());
        }

    }

    public static void aStarSearch(int[][] initialState){
        PriorityQueue<State> stateQueue = new PriorityQueue<State>();

        int zeroPosX = 0, zeroPosY = 0;
        int initialHeuristic = hFunction(initialState);
        for (int i = 0; i < initialState.length; i++) {
            for (int j = 0; j < initialState[i].length; j++) {
                if(initialState[i][j] == 0){
                    zeroPosX = i;
                    zeroPosY = j;
                    break;
                }
            }
        }
        stateQueue.add(new State(initialState, 0, initialHeuristic, zeroPosX, zeroPosY));
        int count = 0;
        while(!stateQueue.isEmpty()){
            if(count++ % 100000 == 0)
                System.out.println(count);
            State currentState = stateQueue.poll();
            if(isGoalState(currentState.pieces)){
                System.out.print("I've found a SOLUTION!");
                System.out.println("Max fringe: " + maxFringeSize);
                return;
            }

            for (int[] move : new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}) {
                int newX = currentState.zeroPosX + move[0];
                int newY = currentState.zeroPosY + move[1];
                if (isValidMove(newX, newY)) {

                    int[][] newPieces = deepCopy(currentState.pieces);
                    swap(newPieces, currentState.zeroPosX, currentState.zeroPosY, newX, newY);
                    if(!isUnique(newPieces))
                        continue;
                    maxFringeSize++;
                    int heuristic = hFunction(newPieces);
                    stateQueue.add(new State(newPieces, heuristic, currentState.g + 1, newX, newY));
                }
            }
            maxFringeSize = Math.max(maxFringeSize, stateQueue.size());
        }
    }

    public static void main(String[] args) {
        greedySearch(new int[][] {{6,7,4},{3,0,8},{2,5,1}});
        maxFringeSize = 1;
        seenStates = new HashSet<>();
        aStarSearch(new int[][] {{6,7,4},{3,0,8},{2,5,1}});
    }
}