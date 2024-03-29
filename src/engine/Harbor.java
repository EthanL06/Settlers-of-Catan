package engine;

import engine.enums.ResourceType;
import engine.helper.Vertex;

public class Harbor {

    private static int numHarbors = 0;

    private Vertex[] vertices;
    private final ResourceType resourceType;
    private final int ratio;
    private final int id;

    public Harbor(ResourceType type) {
        vertices = new Vertex[2];

        this.resourceType = type;
        this.id = numHarbors++;

        if (type == ResourceType.MISC) {
            ratio = 3;
        } else {
            ratio = 2;
        }
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getRatio() {
        return ratio;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Harbor " + id + ": " + resourceType;
    }
}
