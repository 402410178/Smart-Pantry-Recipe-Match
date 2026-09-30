package za.ac.smartpantry;

public class Ingredient {
    public final long id;
    public final String name;
    public final double quantity;
    public final String unit;

    public Ingredient(long id, String name, double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}
