package base;

public abstract class Model {
    private String id;
    
    public abstract Object[] getDatabaseValues();

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

}
