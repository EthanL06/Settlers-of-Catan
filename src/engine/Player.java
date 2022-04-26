package engine;

import engine.buildings.Road;
import engine.buildings.Structure;
import engine.cards.DevelopmentCard;
import engine.enums.Color;
import engine.helper.Edge;
import engine.helper.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    private static int number = 1;

    private int id;
    private Color color;
    private Stockpile stockpile;

    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<Structure> structures;
    private ArrayList<Road> roads;

    private ArrayList<Harbor> harbors;

    private int numOfKnights;
    private int lengthOfLongestRoad;
    private int victoryPoints;

    public Player(Color color) {
        this.id = number++;
        this.color = color;
        stockpile = new Stockpile();

        developmentCards = new ArrayList<>();
        structures = new ArrayList<>();
        roads = new ArrayList<>();

        harbors = new ArrayList<>();

        numOfKnights = 0;
        lengthOfLongestRoad = 0;
        victoryPoints = 0;
    }

    // TODO: remember to add victory points
    public void addStructure(Structure structure) {
        structures.add(structure);

        // Update harbors player can trade with
        outer:
        for (Harbor harbor : Board.getHarbors()) {

            for (Vertex harborVertex : harbor.getVertices()) {
                if (harborVertex.equals(structure.getVertex())) {
                    System.out.println("HERE!!!");
                    harbors.add(harbor);
                    break outer;
                }
            }
        }
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        developmentCards.add(card);
    }

    public void useDevelopmentCard() {
        // TODO: need to implement
    }

    // TODO: complete this!!
    public void updateLongestRoad() {
        // TODO: need to account that other players can cut length of road with settlements and cities
        int longestRoad = 0;

        for (Road road: roads) {
            longestRoad = Math.max(longestRoad, longestRoad(road, new HashSet<>()));
        }

        lengthOfLongestRoad = longestRoad;
    }

/*    private int longestRoad(Road road, HashSet<Road> roadsVisited) {
        if (roadsVisited.contains(road)) {
            return 0;
        }

        roadsVisited.add(road);

        Edge edge = road.getEdge();
        Vertex[] adjacentVertices = edge.getAdjacentVertices();
        ArrayList<Vertex> availableVertices = new ArrayList<>();

        for (Vertex adjacentVertex : adjacentVertices) {
            // if adjacent vertex doesn't contain a structure or it contains a structure owned by this player
            if (adjacentVertex.getStructure() == null || adjacentVertex.getStructure().getOwner().equals(this)) {
                availableVertices.add(adjacentVertex);
            }
        }

        int longestRoad = 0;
        for (Vertex vertex: availableVertices) {
            for (Edge adjacentEdge: vertex.getAdjacentEdges()) {
                // check if the adjacent edge is not the same as the  given edge AND there is a road in the adjacent edge AND the road is owned by this player
                if (adjacentEdge != null && !roadsVisited.contains(adjacentEdge.getRoad()) && !adjacentEdge.equals(edge) && adjacentEdge.getRoad() != null && adjacentEdge.getRoad().getOwner().equals(this)) {
                    longestRoad = Math.max(longestRoad, longestRoad(adjacentEdge.getRoad(), roadsVisited));
                    System.out.println("LONGEST SO FAR: " + longestRoad);
                }
            }
        }

        return 1 + longestRoad;
    }*/

    private int longestRoad(Road road, HashSet<Road> roadsVisited) {
        if (roadsVisited.contains(road)) {
            return 0;
        }

        int longestRoad = 0;

        roadsVisited.add(road);

        Edge edge = road.getEdge();
        Vertex[] adjacentVertices = edge.getAdjacentVertices();

        for (Vertex vertex: adjacentVertices) {
            if (vertex.getStructure() == null || vertex.getStructure().getOwner().equals(this)) {
                Edge[] adjacentEdges = vertex.getAdjacentEdges();
                ArrayList<Edge> availableEdges = new ArrayList<>();

                for (Edge adjacentEdge : adjacentEdges) {
                    if (adjacentEdge != null && adjacentEdge.getRoad() != null && adjacentEdge.getRoad().getOwner().equals(this)) {
                        availableEdges.add(adjacentEdge);
                    }
                }

                for (Edge availableEdge : availableEdges) {
                    longestRoad = Math.max(longestRoad, longestRoad(availableEdge.getRoad(), roadsVisited));
                }
            }
        }

        return 1 + longestRoad;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public int getLengthOfLongestRoad() {
        return lengthOfLongestRoad;
    }

    public ArrayList<Harbor> getHarbors() {
        return harbors;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public Stockpile getStockpile() {
        return stockpile;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "Player " + id + ": " + color;
    }
}
