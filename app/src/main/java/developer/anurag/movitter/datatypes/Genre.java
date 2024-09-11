package developer.anurag.movitter.datatypes;

public class Genre {
    private long id;
    private String name;

    public Genre(String name,int id) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {return name;}

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
