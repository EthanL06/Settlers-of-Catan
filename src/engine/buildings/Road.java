package engine.buildings;

import engine.Player;
import engine.helper.Edge;
import engine.helper.Location;

public class Road {

    private final Location location;
    private final Player owner;
    private Edge edge;

    public Road(Location location, Player owner) {
        this.location = location;
        this.owner = owner;
        this.edge = null;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Location getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }

}
