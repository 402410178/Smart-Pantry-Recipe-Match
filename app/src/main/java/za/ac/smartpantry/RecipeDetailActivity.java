package za.ac.smartpantry;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); PantryDatabase database = new PantryDatabase(this); long id = getIntent().getLongExtra("recipe_id", -1);
        LinearLayout root = Ui.page(this,"Recipe"); setContentView(root);
        root.addView(Ui.label(this,"Loading recipe…",16));
        DatabaseExecutor.execute(() -> {
            Recipe selected = null;
            for (Recipe item : database.getRecipes()) if (item.id == id) { selected = item; break; }
            java.util.List<Ingredient> ingredients = selected == null ? java.util.Collections.emptyList() : database.getRecipeIngredients(id);
            Recipe loadedRecipe = selected;
            runOnUiThread(() -> {
                if (isFinishing() || isDestroyed()) return;
                root.removeViewAt(1);
                if (loadedRecipe == null) {
                    root.addView(Ui.label(this,"This recipe could not be found.",16));
                    return;
                }
                ((android.widget.TextView)root.getChildAt(0)).setText(loadedRecipe.name);
                root.addView(Ui.label(this,loadedRecipe.description,17));
                root.addView(Ui.label(this,"Ingredients",20));
                for (Ingredient item : ingredients) root.addView(Ui.label(this,"•  "+item.name+" — "+item.quantity+" "+item.unit,16));
                root.addView(Ui.label(this,"Method",20)); root.addView(Ui.label(this,loadedRecipe.steps,17));
                root.addView(Ui.button(this,"Back to suggestions"));
                ((android.widget.Button)root.getChildAt(root.getChildCount()-1)).setOnClickListener(v->finish());
            });
        });
    }
}
