package za.ac.smartpantry;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class RecipeMatcherTest {
    private Ingredient item(String name, double quantity, String unit) { return new Ingredient(0, name, quantity, unit); }

    @Test public void exactFullRecipeMatches() {
        assertTrue(RecipeMatcher.canMake(Arrays.asList(item("tomato", 2, "count"), item("pasta", 200, "g")),
                Arrays.asList(item("tomatoes", 2, "counts"), item("pasta", 200, "g"), item("salt", 1, "tsp"))));
    }
    @Test public void missingIngredientExcludesRecipe() {
        assertFalse(RecipeMatcher.canMake(Arrays.asList(item("tomato", 1, "count"), item("pasta", 100, "g")),
                Collections.singletonList(item("tomato", 1, "count"))));
    }
    @Test public void insufficientQuantityExcludesRecipe() {
        assertFalse(RecipeMatcher.canMake(Collections.singletonList(item("pasta", 200, "g")),
                Collections.singletonList(item("pasta", 150, "g"))));
    }
    @Test public void metricUnitsAndPluralNamesNormalize() {
        assertTrue(RecipeMatcher.canMake(Arrays.asList(item("berries", 100, "g"), item("milk", 500, "ml")),
                Arrays.asList(item("berry", 0.1, "kg"), item("milk", 0.5, "l"))));
    }
    @Test public void incompatibleUnitsDoNotMatch() {
        assertFalse(RecipeMatcher.canMake(Collections.singletonList(item("milk", 250, "ml")),
                Collections.singletonList(item("milk", 250, "g"))));
    }
}
