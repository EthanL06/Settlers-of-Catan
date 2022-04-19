package engine.helper;

import engine.buildings.Road;

public class Edge {

    public static final int NORTHWEST = 0, NORTH = 1, NORTHEAST = 2, SOUTHEAST = 3, SOUTH = 4, SOUTHWEST = 5;
    private static int count = 0;

    private int id;
    private Road road;

    public Edge() {
        this(null);
    }

    public Edge(Road road) {
        this.road = road;
        this.id = count++;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Road getRoad() {
        return road;
    }

    public String toString() {
        return "" + id + (road != null ? " Road owned by " + road.getOwner() : "");
    }
}
