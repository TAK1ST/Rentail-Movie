package main.base;

public abstract class Model implements Comparable<Model> {
    private String id;
    
    public Model() {}
    
    public Model(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Model[id=%s]", id);
    }
    
    @Override
    public int compareTo(Model other) {
        return this.id.compareTo(other.id);
    }

}
