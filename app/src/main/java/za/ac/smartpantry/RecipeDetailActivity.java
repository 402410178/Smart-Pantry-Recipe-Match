package za.ac.smartpantry;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); PantryDatabase database = new PantryDatabase(this); long id = getIntent().getLongExtra("recipe_id", -1);
        Recipe recipe = null; for (Recipe item : database.getRecipes()) if (item.id == id) { recipe = item; break; }
        LinearLayout root = Ui.page(this,recipe == null ? "Recipe" : recipe.name); setContentView(root);
        if (recipe == null) { root.addView(Ui.label(this,"This recipe could not be found.",16)); return; }
        root.addView(Ui.label(this,recipe.description,17)); root.addView(Ui.label(this,"Ingredients",20));
        for (Ingredient item : database.getRecipeIngredients(id)) root.addView(Ui.label(this,"•  "+item.name+" — "+item.quantity+" "+item.unit,16));
        root.addView(Ui.label(this,"Method",20)); root.addView(Ui.label(this,recipe.steps,17));
        root.addView(Ui.button(this,"Back to suggestions")); ((android.widget.Button)root.getChildAt(root.getChildCount()-1)).setOnClickListener(v->finish());
    }
}
