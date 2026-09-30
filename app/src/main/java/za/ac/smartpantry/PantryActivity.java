package za.ac.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PantryActivity extends AppCompatActivity {
    private PantryDatabase database; private IngredientAdapter adapter;
    @Override protected void onCreate(Bundle state) { super.onCreate(state); database = new PantryDatabase(this); }
    @Override protected void onResume() { super.onResume(); render(); }
    private void render() {
        LinearLayout root = Ui.page(this, "My Pantry"); setContentView(root);
        root.addView(Ui.label(this, "Track what you have at home and find recipes you can make now.", 16));
        LinearLayout actions = new LinearLayout(this); Button add = Ui.button(this, "+ Add ingredient"), recipes = Ui.button(this, "Find recipes"); actions.addView(add, new LinearLayout.LayoutParams(0,-2,1)); actions.addView(recipes, new LinearLayout.LayoutParams(0,-2,1)); root.addView(actions);
        add.setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class)));
        recipes.setOnClickListener(v -> startActivity(new Intent(this, SuggestedRecipesActivity.class)));
        RecyclerView list = new RecyclerView(this); list.setLayoutManager(new LinearLayoutManager(this)); adapter = new IngredientAdapter(new IngredientAdapter.Listener() {
            @Override public void onTap(Ingredient item) { Intent i = new Intent(PantryActivity.this, IngredientFormActivity.class); i.putExtra("id", item.id); i.putExtra("name", item.name); i.putExtra("quantity", item.quantity); i.putExtra("unit", item.unit); startActivity(i); }
            @Override public void onLongPress(Ingredient item) { new AlertDialog.Builder(PantryActivity.this).setTitle("Delete " + item.name + "?").setMessage("This removes the ingredient from your pantry.").setNegativeButton("Cancel", null).setPositiveButton("Delete", (d,w) -> { database.deleteIngredient(item.id); refresh(); Toast.makeText(PantryActivity.this,"Ingredient deleted",Toast.LENGTH_SHORT).show(); }).show(); }
        }); list.setAdapter(adapter); root.addView(list, Ui.weighted()); refresh(); Ui.navigation(this, root);
    }
    private void refresh() { if (adapter != null) adapter.setItems(database.getPantry()); }
}
