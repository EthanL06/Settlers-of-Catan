package engine;

import engine.buildings.Road;
import engine.buildings.Structure;
import engine.cards.DevelopmentCard;
import engine.enums.Color;

import java.util.ArrayList;

public class Player {
    private static int number = 1;

    private int id;
    private Color color;
    private Stockpile stockpile;

    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<Structure> structures;
    private ArrayList<Road> roads;

    private ArrayList<Tile> tiles;
    private ArrayList<Harbor> harbors;

    private int numOfKnights;
    private int lengthOfLongestRoad;
    private int victoryPoints;

    public Player(Color color) {
        this.id = number;
        this.color = color;
        stockpile = new Stockpile();

        developmentCards = new ArrayList<>();
        structures = new ArrayList<>();
        roads = new ArrayList<>();

        tiles = new ArrayList<>();
        harbors = new ArrayList<>();

        numOfKnights = 0;
        lengthOfLongestRoad = 0;
        victoryPoints = 0;

        number++;
    }

    public void addStructure(Structure structure) {
        structures.add(structure);
    }

    public void addRoad(Road road) {
        roads.add(road);
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public String toString() {
        return "Player " + id + ": " + color;
    }
}
