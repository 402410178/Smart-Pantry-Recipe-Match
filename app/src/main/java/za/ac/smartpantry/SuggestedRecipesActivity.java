package za.ac.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private PantryDatabase database; private RecipeAdapter adapter;
    @Override protected void onCreate(Bundle state) { super.onCreate(state); database = new PantryDatabase(this); }
    @Override protected void onResume() { super.onResume(); render(); }
    private void render() {
        LinearLayout root = Ui.page(this,"Suggested Recipes"); setContentView(root);
        root.addView(Ui.label(this,"Every item and required quantity must be in your pantry to appear here.",16));
        android.widget.TextView empty = Ui.label(this,"",16); root.addView(empty);
        RecyclerView list = new RecyclerView(this); list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(recipe -> { Intent i = new Intent(this,RecipeDetailActivity.class); i.putExtra("recipe_id",recipe.id); startActivity(i); }); list.setAdapter(adapter); root.addView(list,Ui.weighted());
        RecipeAdapter currentAdapter = adapter;
        DatabaseExecutor.execute(() -> {
            List<Ingredient> pantry = database.getPantry();
            List<Recipe> matches = new ArrayList<>();
            for (Recipe recipe : database.getRecipes()) {
                if (RecipeMatcher.canMake(database.getRecipeIngredients(recipe.id), pantry)) matches.add(recipe);
            }
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed() && adapter == currentAdapter) {
                    currentAdapter.setItems(matches);
                    empty.setText(matches.isEmpty()?"No recipes match your pantry yet. Add more ingredients to see what you can make.":matches.size()+" recipe(s) you can make right now");
                }
            });
        });
        Ui.navigation(this,root);
    }
}
