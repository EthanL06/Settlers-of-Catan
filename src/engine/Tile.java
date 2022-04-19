package engine;

import engine.enums.ResourceType;
import engine.helper.Edge;
import engine.helper.Location;
import engine.helper.Vertex;

public class Tile {
    public static final int NORTHWEST = 0, NORTH = 1, NORTHEAST = 2, SOUTHEAST = 3, SOUTH = 4, SOUTHWEST = 5;

    private final ResourceType type;
    private int number;

    private Tile[] adjacentTiles;
    private Edge[] edges;
    private Vertex[] vertices;

    private Location location;

    public Tile(ResourceType type){
        this.type = type;
        number = -1;

        adjacentTiles = new Tile[6];
        edges = new Edge[6];
        vertices = new Vertex[6];

        location = null;
    }

    public Tile getAdjacentTile(int orientation) {
        return adjacentTiles[orientation];
    }

    public Tile[] getAdjacentTiles() {
        return adjacentTiles;
    }

    public void setAdjacentTiles() {
        Tile[][] board = Board.getBoard();
        int row = location.getRow();
        int col = location.getCol();

        switch (location.getRow()) {
            case 0, 1 -> {
                // NW
                if (Board.isValidCoordinate(row - 1, col - 1)) {
                    adjacentTiles[NORTHWEST] = board[row - 1][col - 1];
                }

                // N
                if (Board.isValidCoordinate(row - 1, col)) {
                    adjacentTiles[NORTH] = board[row - 1][col];
                }

                // NE
                if (Board.isValidCoordinate(row, col + 1)) {
                    adjacentTiles[NORTHEAST] = board[row][col + 1];
                }

                // SE
                if (Board.isValidCoordinate(row + 1, col + 1)) {
                    adjacentTiles[SOUTHEAST] = board[row + 1][col + 1];
                }

                // S
                if (Board.isValidCoordinate(row + 1, col)) {
                    adjacentTiles[SOUTH] = board[row + 1][col];
                }

                // SW
                if (Board.isValidCoordinate(row, col - 1)) {
                    adjacentTiles[SOUTHWEST] = board[row][col - 1];
                }
            }
            case 2 -> {
                // NW
                if (Board.isValidCoordinate(row - 1, col - 1)) {
                    adjacentTiles[NORTHWEST] = board[row - 1][col - 1];
                }

                // N
                if (Board.isValidCoordinate(row - 1, col)) {
                    adjacentTiles[NORTH] = board[row - 1][col];
                }

                // NE
                if (Board.isValidCoordinate(row, col + 1)) {
                    adjacentTiles[NORTHEAST] = board[row][col + 1];
                }

                // SE
                if (Board.isValidCoordinate(row + 1, col)) {
                    adjacentTiles[SOUTHEAST] = board[row + 1][col];
                }

                // S
                if (Board.isValidCoordinate(row + 1, col - 1)) {
                    adjacentTiles[SOUTH] = board[row + 1][col - 1];
                }

                // SW
                if (Board.isValidCoordinate(row, col - 1)) {
                    adjacentTiles[SOUTHWEST] = board[row][col - 1];
                }
            }
            case 3, 4 -> {
                // NW
                if (Board.isValidCoordinate(row - 1, col)) {
                    adjacentTiles[NORTHWEST] = board[row - 1][col];
                }

                // N
                if (Board.isValidCoordinate(row - 1, col + 1)) {
                    adjacentTiles[NORTH] = board[row - 1][col + 1];
                }

                // NE
                if (Board.isValidCoordinate(row, col + 1)) {
                    adjacentTiles[NORTHEAST] = board[row][col + 1];
                }

                // SE
                if (Board.isValidCoordinate(row + 1, col)) {
                    adjacentTiles[SOUTHEAST] = board[row + 1][col];
                }

                // S
                if (Board.isValidCoordinate(row + 1, col - 1)) {
                    adjacentTiles[SOUTH] = board[row + 1][col - 1];
                }

                // SW
                if (Board.isValidCoordinate(row, col - 1)) {
                    adjacentTiles[SOUTHWEST] = board[row][col - 1];
                }
            }
        }
    }

    public void setAdjacentEdges() {
        setAdjacentEdge(Edge.NORTHWEST);
        setAdjacentEdge(Edge.NORTH);
        setAdjacentEdge(Edge.NORTHEAST);
        setAdjacentEdge(Edge.SOUTHEAST);
        setAdjacentEdge(Edge.SOUTH);
        setAdjacentEdge(Edge.SOUTHWEST);
    }

    private void setAdjacentEdge(int edgeOrientation) {
        if (edges[edgeOrientation] != null) {
            return;
        }

        Tile adjacentTile = adjacentTiles[edgeOrientation];

        // Tile is on the edge of board
        if (adjacentTile == null) {
            edges[edgeOrientation] = new Edge();
            return;
        }

        int tileEdgeOrientation = -1;

        switch (edgeOrientation) {
            case Edge.NORTHWEST -> tileEdgeOrientation = Edge.SOUTHEAST;
            case Edge.NORTH -> tileEdgeOrientation = Edge.SOUTH;
            case Edge.NORTHEAST -> tileEdgeOrientation = Edge.SOUTHWEST;
            case Edge.SOUTHEAST -> tileEdgeOrientation = Edge.NORTHWEST;
            case Edge.SOUTH -> tileEdgeOrientation = Edge.NORTH;
            case Edge.SOUTHWEST -> tileEdgeOrientation = Edge.NORTHEAST;
        }

        Edge temp = adjacentTile.getEdge(tileEdgeOrientation);

        // Adjacent tile already has an edge in the correct orientation
        if (temp != null) {
            edges[edgeOrientation] = temp;
        } else {
            Edge e = new Edge();

            edges[edgeOrientation] = e;
            adjacentTile.setEdge(e, tileEdgeOrientation);
        }

    }

    public void setAdjacentVertices() {
        setAdjacentVertex(Vertex.NORTHWEST);
        setAdjacentVertex(Vertex.NORTHEAST);
        setAdjacentVertex(Vertex.EAST);
        setAdjacentVertex(Vertex.SOUTHEAST);
        setAdjacentVertex(Vertex.SOUTHWEST);
        setAdjacentVertex(Vertex.WEST);
    }

    private void setAdjacentVertex(int vertexOrientation) {
        if (vertices[vertexOrientation] != null) {
            return;
        }

        Tile adjacentTileOne = null;
        Tile adjacentTileTwo = null;

        switch (vertexOrientation) {
            case Vertex.NORTHWEST -> {
                adjacentTileOne = adjacentTiles[NORTHWEST];
                adjacentTileTwo = adjacentTiles[NORTH];
            }
            case Vertex.NORTHEAST -> {
                adjacentTileOne = adjacentTiles[NORTH];
                adjacentTileTwo = adjacentTiles[NORTHEAST];
            }
            case Vertex.EAST -> {
                adjacentTileOne = adjacentTiles[NORTHEAST];
                adjacentTileTwo = adjacentTiles[SOUTHEAST];
            }
            case Vertex.SOUTHEAST -> {
                adjacentTileOne = adjacentTiles[SOUTHEAST];
                adjacentTileTwo = adjacentTiles[SOUTH];
            }
            case Vertex.SOUTHWEST -> {
                adjacentTileOne = adjacentTiles[SOUTH];
                adjacentTileTwo = adjacentTiles[SOUTHWEST];
            }
            case Vertex.WEST -> {
                adjacentTileOne = adjacentTiles[SOUTHWEST];
                adjacentTileTwo = adjacentTiles[NORTHWEST];
            }
        }

        boolean tileOneExists = adjacentTileOne != null;
        boolean tileTwoExists = adjacentTileTwo != null;

        boolean vertexExists = false;

        int tileOneVertexOrientation = -1;
        int tileTwoVertexOrientation = -1;

        switch (vertexOrientation) {
            case Vertex.NORTHWEST -> {
                tileOneVertexOrientation = Vertex.EAST;
                tileTwoVertexOrientation = Vertex.SOUTHWEST;
            }
            case Vertex.NORTHEAST -> {
                tileOneVertexOrientation = Vertex.SOUTHEAST;
                tileTwoVertexOrientation = Vertex.WEST;
            }
            case Vertex.EAST -> {
                tileOneVertexOrientation = Vertex.SOUTHWEST;
                tileTwoVertexOrientation = Vertex.NORTHWEST;
            }
            case Vertex.SOUTHEAST -> {
                tileOneVertexOrientation = Vertex.WEST;
                tileTwoVertexOrientation = Vertex.NORTHEAST;
            }
            case Vertex.SOUTHWEST -> {
                tileOneVertexOrientation = Vertex.NORTHWEST;
                tileTwoVertexOrientation = Vertex.EAST;
            }
            case Vertex.WEST -> {
                tileOneVertexOrientation = Vertex.NORTHEAST;
                tileTwoVertexOrientation = Vertex.SOUTHEAST;
            }
        }

        // If NW tile exists
        if (tileOneExists) {
//            Vertex temp = adjacentTileOne.getVertex(Vertex.EAST);
            Vertex temp = adjacentTileOne.getVertex(tileOneVertexOrientation);

            // If NW tile has a vertex at position EAST
            if (temp != null) {
                vertices[vertexOrientation] = temp;

                // Set pointer to same vertex in N tile at position SW
                if (tileTwoExists) {
                    adjacentTileTwo.setVertex(temp, tileTwoVertexOrientation);
                }

                vertexExists = true;
            }
        }

        // If N exists
        if (tileTwoExists && !vertexExists) {
            Vertex temp = adjacentTileTwo.getVertex(tileTwoVertexOrientation);

            if (temp != null) {
                vertices[vertexOrientation] = temp;

                // Set pointer to same vertex in NW tile at position E
                if (tileOneExists) {
                    adjacentTileOne.setVertex(temp, tileOneVertexOrientation);
                }

                vertexExists = true;
            }

        }

        if (!vertexExists) {
            Vertex v = new Vertex();
            vertices[vertexOrientation] = v;

            if (tileOneExists) {
                adjacentTileOne.setVertex(v, tileOneVertexOrientation);
            }

            if (tileTwoExists) {
                adjacentTileTwo.setVertex(v, tileTwoVertexOrientation);
            }
        }

    }

    public void setAdjacentEdgesToVertices() {
        // TODO: complete this!!
        for (int i = 0; i < vertices.length; i++) {
            Vertex v = vertices[i];
            Edge[] adjacentEdges = new Edge[3];

            switch (i) {
                case Vertex.NORTHWEST -> {
                    adjacentEdges[0] = edges[Edge.NORTHWEST];
                    adjacentEdges[1] = edges[Edge.NORTH];

                    // if northern tile exists
                    if (adjacentTiles[Tile.NORTH] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTH].getEdge(Edge.SOUTHWEST);
                    } else if (adjacentTiles[Tile.NORTHEAST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTHEAST].getEdge(Edge.NORTHEAST);
                    }
                }
                case Vertex.NORTHEAST -> {
                    adjacentEdges[0] = edges[Edge.NORTH];
                    adjacentEdges[1] = edges[Edge.NORTHEAST];

                    if (adjacentTiles[Tile.NORTH] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTH].getEdge(Edge.SOUTHEAST);
                    } else if (adjacentTiles[Tile.NORTHWEST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTHWEST].getEdge(Edge.NORTHWEST);
                    }
                }
                case Vertex.EAST -> {
                    adjacentEdges[0] = edges[Edge.NORTHEAST];
                    adjacentEdges[1] = edges[Edge.SOUTHEAST];

                    if (adjacentTiles[Tile.NORTHEAST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTHEAST].getEdge(Edge.SOUTH);
                    } else if (adjacentTiles[Tile.SOUTHEAST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTHEAST].getEdge(Tile.NORTH);
                    }
                }
                case Vertex.SOUTHEAST -> {
                    adjacentEdges[0] = edges[Edge.SOUTHEAST];
                    adjacentEdges[1] = edges[Edge.SOUTH];

                    if (adjacentTiles[Tile.SOUTHEAST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTHEAST].getEdge(Edge.SOUTHWEST);
                    } else if (adjacentTiles[Tile.SOUTH] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTH].getEdge(Edge.NORTHEAST);
                    }
                }
                case Vertex.SOUTHWEST -> {
                    adjacentEdges[0] = edges[Edge.SOUTH];
                    adjacentEdges[1] = edges[Edge.SOUTHWEST];

                    if (adjacentTiles[Tile.SOUTH] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTH].getEdge(Edge.NORTHWEST);
                    } else if (adjacentTiles[Tile.SOUTHWEST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTHWEST].getEdge(Edge.SOUTHEAST);
                    }
                }
                case Vertex.WEST -> {
                    adjacentEdges[0] = edges[Edge.SOUTHWEST];
                    adjacentEdges[1] = edges[Edge.NORTHWEST];

                    if (adjacentTiles[Tile.SOUTHWEST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.SOUTHWEST].getEdge(Edge.NORTH);
                    } else if (adjacentTiles[Tile.NORTHWEST] != null) {
                        adjacentEdges[2] = adjacentTiles[Tile.NORTHWEST].getEdge(Edge.SOUTH);
                    }
                }
            }

            v.setAdjacentEdges(adjacentEdges);
        }
    }

    public Vertex getVertex(int orientation) {
        return vertices[orientation];
    }

    public Edge getEdge(int orientation) {
        return edges[orientation];
    }

    public Edge[] getEdges() {
        return edges;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void setVertex(Vertex vertex, int orientation) {
        vertices[orientation] = vertex;
    }

    public void setEdge(Edge edge, int orientation) {
        edges[orientation] = edge;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public ResourceType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String toString(){
        return type.toString() +  " " + number;
    }

    public String printAdjacentTiles() {
        String s = "";

        for(int i = 0; i < 6; i++) {
            if (adjacentTiles[i] != null) {
                s += " " + adjacentTiles[i].getLocation().toString();
            } else {
                s += " null";
            }
        }

        return s;
    }
}
