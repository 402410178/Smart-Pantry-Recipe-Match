package za.ac.smartpantry;

public class Recipe {
    public final long id;
    public final String name;
    public final String description;
    public final String steps;

    public Recipe(long id, String name, String description, String steps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.steps = steps;
    }
}
