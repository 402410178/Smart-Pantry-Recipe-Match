package za.ac.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/** Local persistent store for pantry items and the bundled recipe catalogue. */
public class PantryDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public PantryDatabase(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), unit TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, description TEXT NOT NULL, steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (recipe_id INTEGER NOT NULL REFERENCES recipes(id) ON DELETE CASCADE, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, PRIMARY KEY(recipe_id,name))");
        db.execSQL("CREATE TABLE app_settings (setting_key TEXT PRIMARY KEY, setting_value TEXT NOT NULL)");
        db.execSQL("PRAGMA foreign_keys=ON");
        seedRecipes(db);
        ContentValues settings = new ContentValues(); settings.put("setting_key", "expiry_alerts"); settings.put("setting_value", "1"); db.insert("app_settings", null, settings);
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] recipes = {
            {"Tomato Pasta", "A simple tomato and garlic pasta.", "Boil pasta until tender. Warm tomatoes with garlic and oil. Toss together and season.", "pasta|200|g;tomato|3|count;garlic|2|clove;olive oil|1|tbsp"},
            {"Cheese Omelette", "A quick, filling cheese omelette.", "Beat eggs. Cook gently in a pan, add cheese, fold and serve.", "egg|2|count;cheese|40|g;butter|1|tsp"},
            {"Banana Pancakes", "Soft pancakes using ripe bananas.", "Mash banana, whisk with egg and flour. Cook spoonfuls in a lightly oiled pan.", "banana|2|count;egg|1|count;flour|100|g;milk|100|ml"},
            {"Vegetable Fried Rice", "A flexible rice dish using leftover vegetables.", "Stir-fry vegetables, add cooked rice and soy sauce, then heat through.", "rice|200|g;carrot|1|count;peas|80|g;soy sauce|1|tbsp"},
            {"Garlic Butter Potatoes", "Crisp potatoes with garlic butter.", "Boil potato cubes until tender. Toss with melted butter and garlic.", "potato|400|g;butter|30|g;garlic|2|clove"},
            {"Chickpea Salad", "A fresh chickpea salad with lemon dressing.", "Combine chickpeas, cucumber and tomato. Dress with lemon juice and olive oil.", "chickpea|240|g;cucumber|1|count;tomato|2|count;lemon|1|count;olive oil|1|tbsp"},
            {"Apple Oat Porridge", "Warm oats topped with apple.", "Simmer oats in milk until creamy. Top with chopped apple and cinnamon.", "oats|60|g;milk|250|ml;apple|1|count;cinnamon|1|tsp"},
            {"Tuna Sandwich", "A fast tuna and mayonnaise sandwich.", "Mix drained tuna with mayonnaise. Spread on bread and add lettuce.", "tuna|120|g;mayonnaise|1|tbsp;bread|2|slice;lettuce|2|leaf"},
            {"Spinach Feta Pasta", "Pasta with spinach and feta.", "Boil pasta. Wilt spinach, fold in feta and pasta with a little pasta water.", "pasta|200|g;spinach|100|g;feta|50|g;garlic|1|clove"},
            {"Carrot Soup", "A smooth, comforting carrot soup.", "Soften onion, add carrots and stock. Simmer until soft and blend.", "carrot|400|g;onion|1|count;vegetable stock|500|ml;olive oil|1|tbsp"},
            {"Peanut Butter Toast", "Toast topped with peanut butter and banana.", "Toast bread, spread peanut butter and top with sliced banana.", "bread|2|slice;peanut butter|2|tbsp;banana|1|count"},
            {"Tomato Grilled Cheese", "A toasted cheese sandwich with tomato.", "Layer cheese and tomato between bread. Toast in butter until golden.", "bread|2|slice;cheese|50|g;tomato|1|count;butter|10|g"},
            {"Lentil Curry", "A pantry-friendly lentil curry.", "Cook onion with curry powder, add lentils, tomatoes and water. Simmer until tender.", "lentil|200|g;onion|1|count;tomato|2|count;curry powder|1|tbsp"},
            {"Yogurt Berry Bowl", "Yogurt with berries and crunchy oats.", "Spoon yogurt into a bowl and top with berries and oats.", "yogurt|150|g;berries|80|g;oats|30|g"},
            {"Lemon Garlic Chicken", "Pan-cooked chicken with lemon and garlic.", "Season and cook chicken through. Add garlic and lemon juice to the pan.", "chicken|250|g;lemon|1|count;garlic|2|clove;olive oil|1|tbsp"},
            {"Simple Tomato Soup", "A basic tomato soup served with bread.", "Simmer tomatoes with onion and stock. Blend smooth and serve with bread.", "tomato|400|g;onion|1|count;vegetable stock|300|ml;bread|2|slice"},
            {"Egg Fried Rice", "Rice stir-fried with egg and peas.", "Scramble eggs, add rice and peas, then season with soy sauce.", "rice|200|g;egg|2|count;peas|80|g;soy sauce|1|tbsp"},
            {"Baked Apple Oats", "A baked oat and apple breakfast.", "Mix oats, milk, chopped apple and cinnamon. Bake until set.", "oats|100|g;milk|150|ml;apple|1|count;cinnamon|1|tsp"}
        };
        for (String[] item : recipes) {
            ContentValues recipe = new ContentValues(); recipe.put("name", item[0]); recipe.put("description", item[1]); recipe.put("steps", item[2]);
            long id = db.insert("recipes", null, recipe);
            for (String part : item[3].split(";")) {
                String[] fields = part.split("\\|"); ContentValues ingredient = new ContentValues();
                ingredient.put("recipe_id", id); ingredient.put("name", fields[0]); ingredient.put("quantity", Double.parseDouble(fields[1])); ingredient.put("unit", fields[2]);
                db.insert("recipe_ingredients", null, ingredient);
            }
        }
    }

    public List<Ingredient> getPantry() {
        List<Ingredient> items = new ArrayList<>(); Cursor c = getReadableDatabase().query("pantry", null, null, null, null, null, "name COLLATE NOCASE");
        try { while (c.moveToNext()) items.add(new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3))); } finally { c.close(); }
        return items;
    }
    public long addIngredient(String name, double quantity, String unit) { ContentValues v = new ContentValues(); v.put("name", name); v.put("quantity", quantity); v.put("unit", unit); return getWritableDatabase().insert("pantry", null, v); }
    public int updateIngredient(long id, String name, double quantity, String unit) { ContentValues v = new ContentValues(); v.put("name", name); v.put("quantity", quantity); v.put("unit", unit); return getWritableDatabase().update("pantry", v, "id=?", new String[]{String.valueOf(id)}); }
    public int deleteIngredient(long id) { return getWritableDatabase().delete("pantry", "id=?", new String[]{String.valueOf(id)}); }
    public List<Recipe> getRecipes() {
        List<Recipe> items = new ArrayList<>(); Cursor c = getReadableDatabase().query("recipes", null, null, null, null, null, "name COLLATE NOCASE");
        try { while (c.moveToNext()) items.add(new Recipe(c.getLong(0), c.getString(1), c.getString(2), c.getString(3))); } finally { c.close(); }
        return items;
    }
    public List<Ingredient> getRecipeIngredients(long recipeId) {
        List<Ingredient> items = new ArrayList<>(); Cursor c = getReadableDatabase().query("recipe_ingredients", null, "recipe_id=?", new String[]{String.valueOf(recipeId)}, null, null, "name COLLATE NOCASE");
        try { while (c.moveToNext()) items.add(new Ingredient(0, c.getString(1), c.getDouble(2), c.getString(3))); } finally { c.close(); }
        return items;
    }
    public boolean getExpiryAlerts() {
        Cursor c = getReadableDatabase().query("app_settings", new String[]{"setting_value"}, "setting_key='expiry_alerts'", null, null, null, null);
        try { return c.moveToFirst() && "1".equals(c.getString(0)); } finally { c.close(); }
    }
    public void setExpiryAlerts(boolean enabled) { ContentValues v = new ContentValues(); v.put("setting_key", "expiry_alerts"); v.put("setting_value", enabled ? "1" : "0"); getWritableDatabase().insertWithOnConflict("app_settings", null, v, SQLiteDatabase.CONFLICT_REPLACE); }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
