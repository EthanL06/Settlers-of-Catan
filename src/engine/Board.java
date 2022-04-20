package engine;

import com.sun.source.tree.Tree;
import engine.buildings.Road;
import engine.buildings.Structure;
import engine.cards.DevelopmentCard;
import engine.enums.DevelopmentCardType;
import engine.enums.ResourceType;
import engine.helper.Edge;
import engine.helper.Location;
import engine.helper.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Board {

    private static Tile[][] board;
    private static HashMap<Integer, Vertex> vertices = new HashMap<>(); // key: vertex id, value: vertex
    private static HashMap<Integer, Edge> edges = new HashMap<>(); // key: edge id, value: edge

    private HashMap<Location, Tile> tileLocations;
    private Location robberLocation;

    private final Stockpile stockpile;
    private Stack<DevelopmentCard> developmentCards;

    public Board() {
        initializeBoard();
        initializeDevelopmentCards();

        stockpile = new Stockpile(19, 19, 19, 19, 19);

        System.out.println(this);
    }

    // region Structure/Road Placement
    public void placeSettlement(Location location) {
        // TODO: complete

        Tile tile = getTile(location);
        if (tile == null) {
            System.out.println("Invalid location");
            return;
        }

        if (tile.getVertex(location.getOrientation()).getStructure() == null) {
            System.out.println("Player placed a settlement");
            Structure structure = new Structure(location, GameState.getCurrentPlayer());
            GameState.getCurrentPlayer().addStructure(structure);
            tile.getVertex(location.getOrientation()).setStructure(structure);
            structure.setVertex(tile.getVertex(location.getOrientation()));

//            System.out.println("Current tile: " + Arrays.toString(tile.getVertices()));
//            Tile[] adjacentTiles = tile.getAdjacentTiles();
//
//            for (Tile t: adjacentTiles) {
//                if (t != null) {
//                    System.out.println(t + ": " + Arrays.toString(t.getVertices()));
//                } else {
//                    System.out.println("null");
//                }
//            }
            System.out.println("Current tile: " + Arrays.toString(tile.getEdges()));
            Tile[] adjacentTiles = tile.getAdjacentTiles();

            for (Tile t: adjacentTiles) {
                if (t != null) {
                    System.out.println(t + ": " + Arrays.toString(t.getEdges()));
                } else {
                    System.out.println("null");
                }
            }
        } else {
            System.out.println("Vertex already has a structure");
        }
    }

    public boolean placeRoad(Location location) {
        // TODO: complete

        Tile tile = getTile(location);

        if (tile == null) {
            System.out.println("Invalid location");
            return false;
        }

        Set<Edge> availableRoadPlacements = availableRoadPlacements();
        Edge selectedEdge = tile.getEdge(location.getOrientation());

        if (availableRoadPlacements.contains(selectedEdge)) {
            System.out.println("Player placed a road");

            Road road = new Road(location, GameState.getCurrentPlayer());
            GameState.getCurrentPlayer().addRoad(road);
            tile.getEdge(location.getOrientation()).setRoad(road);
            road.setEdge(tile.getEdge(location.getOrientation()));

            System.out.println("Current tile: " + Arrays.toString(tile.getEdges()));
            Tile[] adjacentTiles = tile.getAdjacentTiles();

            for (Tile t : adjacentTiles) {
                if (t != null) {
                    System.out.println(t + ": " + Arrays.toString(t.getEdges()));
                } else {
                    System.out.println("null");
                }
            }

            return true;
        } else {
            System.out.println("Invalid location");
            return false;
        }
    }

    public Set<Edge> availableRoadPlacements() {
        Player currentPlayer = GameState.getCurrentPlayer();
        ArrayList<Structure> playerStructures = currentPlayer.getStructures();
        Set<Edge> availableEdges = new HashSet<>(); // available edges to place a road on

        // must connect to a structure
        // can lead of an existing road
        // can not be placed on top of another road
        // can not be placed on top of a structure
        // can not go through other players' structures

        for (Structure s: playerStructures) {
            Vertex vertex = s.getVertex();
            Edge[] adjacentEdges = vertex.getAdjacentEdges();

            for (Edge e: adjacentEdges) {
                if (e == null) continue;
                // if an adjacent edge to a structure doesn't have a road on it
                if (e.getRoad() == null) {
                    availableEdges.add(e);
                }
            }
        }

        for (Road r: currentPlayer.getRoads()) {
            Vertex[] adjacentVertices = r.getEdge().getAdjacentVertices();

            for (Vertex v: adjacentVertices) {
                // cannot build road through another player's structure
                if (v.getStructure() != null && !v.getStructure().getOwner().equals(currentPlayer)) continue;

                // either the vertex is occupied by the current player's structure or the adjacent edges have no road
                Edge[] adjacentEdges = v.getAdjacentEdges();

                for (Edge e: adjacentEdges) {
                    if (e == null) continue;

                    if (e.getRoad() == null) {
                        availableEdges.add(e);
                    }
                }
            }
        }

        return availableEdges;
    }
    // endregion

    // region Initialization
    private void initializeBoard() {
        board = new Tile[5][];

        board[0] = new Tile[3];
        board[1] = new Tile[4];
        board[2] = new Tile[5];
        board[3] = new Tile[4];
        board[4] = new Tile[3];

//        for (int i = 0; i < board.length; i++) {
//            switch (i) {
//                case 0, 4 -> {
//                    board[i] = new Tile[3];
//                }
//                case 1, 3 -> {
//                    board[i] = new Tile[4];
//                }
//                case 2 -> {
//                    board[i] = new Tile[5];
//                }
//            }
//        }

        setTiles();
        setNumberTokens();
    }

    private void initializeDevelopmentCards() {
        developmentCards = new Stack<>();

        final int KNIGHT_COUNT = 14;
        final int VICTORY_POINT_COUNT = 5;
        final int PROGRESS_COUNT = 6;

        String[] victoryPointNames = {"Chapel", "Great Hall", "Library", "Market", "University"};
        String[] progressNames = {"Monopoly", "Road Building", "Year of Plenty"};

        for (int i = 0; i < KNIGHT_COUNT; i++) {
            developmentCards.push(new DevelopmentCard("Knight", DevelopmentCardType.KNIGHT));
        }

        for (int i = 0; i < VICTORY_POINT_COUNT; i++) {
            developmentCards.push(new DevelopmentCard(victoryPointNames[i], DevelopmentCardType.VICTORY_POINT));
        }

        for (int i = 0; i < PROGRESS_COUNT/2; i++) {
            developmentCards.push(new DevelopmentCard(progressNames[i], DevelopmentCardType.PROGRESS));
        }

        Collections.shuffle(developmentCards, Dice.getRef());
    }

    private void setTiles() {
        tileLocations = new HashMap<>();
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            tiles.add(new Tile(ResourceType.WHEAT));
            tiles.add(new Tile(ResourceType.WOOD));
            tiles.add(new Tile(ResourceType.WOOL));
        }

        for (int i = 0; i < 3; i++) {
            tiles.add(new Tile(ResourceType.BRICK));
            tiles.add(new Tile(ResourceType.ORE));
        }

        tiles.add(new Tile(ResourceType.DESERT));

        Collections.shuffle(tiles, Dice.getRef());

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Location location = new Location(row, col);

                board[row][col] = tiles.remove(0);
                board[row][col].setLocation(location);

                if (board[row][col].getType() == ResourceType.DESERT) {
                    robberLocation = location;
                }

                tileLocations.put(location, board[row][col]);
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].setAdjacentTiles();
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col].setAdjacentVertices();
                board[row][col].setAdjacentEdges();
                board[row][col].setAdjacentEdgesToVertices();
                board[row][col].setAdjacentVerticesToEdges();
            }
        }
    }

    private void setNumberTokens() {
        ArrayList<Integer> tokens = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File("src/engine/board.txt"));
            while (sc.hasNextLine()) {
                tokens.add(sc.nextInt());
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // left column
        for (int row = 0; row < board.length; row++) {
            if (board[row][0].getType() != ResourceType.DESERT && board[row][0].getNumber() == -1) {
                board[row][0].setNumber(tokens.remove(0));
            }
        }

        // bottom row
        for (int col = 0; col < board[board.length-1].length; col++) {
            if (board[4][col].getType() != ResourceType.DESERT && board[4][col].getNumber() == -1) {
                board[4][col].setNumber(tokens.remove(0));
            }
        }

        // right column
        for (int row = board.length-1; row >= 0; row--) {
            if (board[row][board[row].length-1].getType() != ResourceType.DESERT && board[row][board[row].length-1].getNumber() == -1) {
                board[row][board[row].length-1].setNumber(tokens.remove(0));
            }
        }

        // top row
        if (board[0][1].getType() != ResourceType.DESERT && board[0][1].getNumber() == -1) {
            board[0][1].setNumber(tokens.remove(0));
        }

        // middle row
        for (int row = 1; row < board.length-1; row++) {
            if (board[row][1].getType() != ResourceType.DESERT && board[row][1].getNumber() == -1) {
                board[row][1].setNumber(tokens.remove(0));
            }
        }

        // middle right column
        for (int row = 3; row >= 0; row--) {
            if (board[row][board[row].length-2].getType() != ResourceType.DESERT && board[row][board[row].length-2].getNumber() == -1) {
                board[row][board[row].length-2].setNumber(tokens.remove(0));
            }
        }

        // middle
        if (board[2][2].getType() != ResourceType.DESERT && board[2][2].getNumber() == -1) {
            board[2][2].setNumber(tokens.remove(0));
        }
    }
    // endregion

    // region Static methods
    public static void addVertex(Vertex v) {
        vertices.put(v.getId(), v);
    }

    public static void addEdge(Edge e) {
        edges.put(e.getId(), e);
    }

    public static boolean isValidCoordinate(Location location) {
        try {
            Tile locationTest = board[location.getRow()][location.getCol()];
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean isValidCoordinate(int row, int col) {
        return isValidCoordinate(new Location(row, col));
    }

    public static Tile[][] getBoard() {
        return board;
    }

    public static Tile getTile(int row, int col) {
        return getTile(new Location(row, col));
    }

    public static Tile getTile(Location l) {
        if (!isValidCoordinate(l.getRow(), l.getCol())) {
            return null;
        }

        return board[l.getRow()][l.getCol()];
    }
    // endregion

    public String toString() {
        return Arrays.deepToString(board).replace("], ", "]\n").replace("[[", "[").replace("]]", "]");
    }
}
