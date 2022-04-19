package engine.buildings;

import engine.Player;
import engine.helper.Location;

public class Road {

    private final Location location;
    private final Player owner;

    public Road(Location location, Player owner) {
        this.location = location;
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }

}
