package engine;

import engine.buildings.Road;
import engine.buildings.Structure;
import engine.cards.DevelopmentCard;
import engine.enums.Color;
import engine.helper.Edge;
import engine.helper.Vertex;

import java.util.ArrayList;

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

        lengthOfLongestRoad = updateLongestRoad();
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        developmentCards.add(card);
    }

    public void useDevelopmentCard() {
        // TODO: need to implement
    }

    // TODO: complete this!!
    public int updateLongestRoad() {
        // TODO: need to account that other players can cut length of road with settlements and cities


        for (Road road: roads) {
            Edge edge = road.getEdge();
            Vertex[] adjacentVertices = edge.getAdjacentVertices();

            for (Vertex vertex: adjacentVertices) {
                // account for players cutting length of longest road
                if (!vertex.getStructure().getOwner().equals(this)) {
                    continue;
                }

            }
        }

        return -1;
    }



    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Road> getRoads() {
        return roads;
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
