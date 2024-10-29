package base;

public abstract class Model {
    private int id;

    public Model(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Model[id=%s]", id);
    }

    public abstract Object[] getDatabaseValues();
}
