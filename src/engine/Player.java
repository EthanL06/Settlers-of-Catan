package engine;

import engine.buildings.Road;
import engine.buildings.Structure;
import engine.cards.DevelopmentCard;
import engine.enums.Color;
import engine.helper.Edge;
import engine.helper.Vertex;

import java.util.ArrayList;
import java.util.Collections;
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

    // look at bug on figma
    public int longestRoad() {
        ArrayList<Edge> endpointEdges = getEndpointEdges();

        int longestRoad = 0;

        for (Edge edge: endpointEdges) {
            Vertex[] adjacentVertices = edge.getAdjacentVertices();

            for (Vertex vertex: adjacentVertices) {
                Edge[] adjacentEdges = vertex.getAdjacentEdges();

                for (Edge adjacentEdge: adjacentEdges) {
                    longestRoad = Math.max(longestRoad, longestRoad(vertex, adjacentEdge, new HashSet<>()));
                }
            }
        }

        return longestRoad;
    }

    private int longestRoad(Vertex startingVertex, Edge edge, HashSet<Road> roadsVisited) {
        if (edge == null || edge.getRoad() == null || roadsVisited.contains(edge.getRoad()) || !edge.getRoad().getOwner().equals(this)) return 0;

        roadsVisited.add(edge.getRoad());

        Vertex otherVertex = edge.getAdjacentVertices()[0].equals(startingVertex) ? edge.getAdjacentVertices()[1] : edge.getAdjacentVertices()[0];

        HashSet<Road> roadsVisitedCopy = new HashSet<>(roadsVisited);
        HashSet<Road> roadsVisitedCopy2 = new HashSet<>(roadsVisited);

        return 1 + Math.max(longestRoad(otherVertex, otherVertex.getAdjacentEdges()[0], roadsVisitedCopy), longestRoad(otherVertex, otherVertex.getAdjacentEdges()[1], roadsVisitedCopy2));
    }

    private ArrayList<Edge> getEndpointEdges() {
        ArrayList<Edge> endpointEdges = new ArrayList<>();

        for (Road road: roads) {
            Edge edge = road.getEdge();
            Vertex[] adjacentVertices = edge.getAdjacentVertices();

            for (Vertex vertex: adjacentVertices) {
                Edge[] vertexEdges = vertex.getAdjacentEdges();
                ArrayList<Edge> otherEdges = new ArrayList<>();

                for (Edge vertexEdge: vertexEdges) {
                    if (vertexEdge != null && vertexEdge.equals(edge)) continue;
                    otherEdges.add(vertexEdge);
                }

                boolean flag = true;
                for (Edge otherEdge: otherEdges) {
                    if (otherEdge != null && otherEdge.getRoad() != null && otherEdge.getRoad().getOwner().equals(this)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) endpointEdges.add(road.getEdge());
            }
        }

        return endpointEdges;
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
