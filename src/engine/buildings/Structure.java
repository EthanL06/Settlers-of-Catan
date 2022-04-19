package engine.buildings;

import engine.Player;
import engine.enums.StructureType;
import engine.helper.Location;

public class Structure {

    private Location location;
    private Player owner;
    private StructureType type;

    public Structure(Location location, Player owner) {
        this.location = location;
        this.owner = owner;
        this.type = StructureType.SETTLEMENT;
    }

    public Location getLocation() {
        return location;
    }

    public Player getOwner() {
        return owner;
    }

    public StructureType getType() {
        return type;
    }
}
