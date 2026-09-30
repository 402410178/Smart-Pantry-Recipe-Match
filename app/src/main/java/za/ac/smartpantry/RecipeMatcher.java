package za.ac.smartpantry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Returns only recipes whose complete ingredient and quantity requirements are in stock. */
public final class RecipeMatcher {
    private RecipeMatcher() { }
    public static boolean canMake(List<Ingredient> required, List<Ingredient> pantry) {
        Map<String, Double> available = new HashMap<>();
        Map<String, String> pantryUnits = new HashMap<>();
        for (Ingredient item : pantry) {
            String key = normalizeName(item.name), unit = normalizeUnit(item.unit);
            double base = toBase(item.quantity, unit);
            available.put(key, available.getOrDefault(key, 0d) + base);
            pantryUnits.put(key, unit);
        }
        for (Ingredient need : required) {
            String key = normalizeName(need.name), unit = normalizeUnit(need.unit);
            if (!available.containsKey(key) || !sameDimension(unit, pantryUnits.get(key))) return false;
            double requiredBase = toBase(need.quantity, unit);
            if (available.get(key) + 0.000001 < requiredBase) return false;
        }
        return true;
    }
    static String normalizeName(String value) {
        String word = value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        if (word.endsWith("ies") && word.length() > 3) return word.substring(0, word.length()-3) + "y";
        if (word.endsWith("oes") && word.length() > 3) return word.substring(0, word.length()-2);
        if (word.endsWith("s") && !word.endsWith("ss") && word.length() > 2) return word.substring(0, word.length()-1);
        return word;
    }
    static String normalizeUnit(String value) {
        String unit = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        if (unit.endsWith("s")) unit = unit.substring(0, unit.length()-1);
        if (unit.equals("gms") || unit.equals("gram")) return "g";
        if (unit.equals("kilogram")) return "kg";
        if (unit.equals("litre") || unit.equals("liter")) return "l";
        if (unit.equals("millilitre") || unit.equals("milliliter")) return "ml";
        if (unit.equals("pieces") || unit.equals("piece")) return "count";
        return unit;
    }
    private static double toBase(double amount, String unit) {
        if (unit.equals("kg")) return amount * 1000;
        if (unit.equals("l")) return amount * 1000;
        if (unit.equals("tbsp")) return amount * 3;
        return amount;
    }
    private static boolean sameDimension(String a, String b) {
        return dimension(a).equals(dimension(b));
    }
    private static String dimension(String u) {
        if (u.equals("g") || u.equals("kg")) return "mass";
        if (u.equals("ml") || u.equals("l")) return "volume";
        if (u.equals("count") || u.equals("piece") || u.equals("clove") || u.equals("slice") || u.equals("leaf")) return "count";
        if (u.equals("tbsp") || u.equals("tsp")) return "spoon";
        return "other:" + u;
    }
}
