package org.example;

public class State implements Comparable<State> {
    public int[][] pieces;
    public int heuristic, g = 0, zeroPosX, zeroPosY;

    public State(int[][] pieces, int heuristic, int zeroPosX, int zeroPosY) {
        this.pieces = pieces;
        this.heuristic = heuristic;
        this.zeroPosX = zeroPosX;
        this.zeroPosY = zeroPosY;
    }

    public State(int[][] pieces, int heuristic, int g, int zeroPosX, int zeroPosY) {
        this(pieces, heuristic, zeroPosX, zeroPosY);
        this.g = g;
    }

    @Override
    public int compareTo(State state) {
        return Integer.compare(this.heuristic + this.g, state.heuristic + state.g);
    }
}
