package za.ac.smartpantry;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

final class Ui {
    static final int GREEN = Color.rgb(46, 125, 50);
    private Ui() { }
    static LinearLayout page(AppCompatActivity a, String title) {
        LinearLayout root = new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20, 18, 20, 18); root.setBackgroundColor(Color.rgb(248, 250, 247));
        TextView heading = new TextView(a); heading.setText(title); heading.setTextSize(26); heading.setTypeface(null, Typeface.BOLD); heading.setTextColor(Color.rgb(27, 94, 32)); heading.setPadding(0, 4, 0, 16); root.addView(heading);
        return root;
    }
    static Button button(Context c, String label) { Button b = new Button(c); b.setText(label); b.setAllCaps(false); return b; }
    static TextView label(Context c, String text, int size) { TextView t = new TextView(c); t.setText(text); t.setTextSize(size); t.setTextColor(Color.rgb(45, 55, 45)); t.setPadding(2, 8, 2, 8); return t; }
    static LinearLayout.LayoutParams weighted() { return new LinearLayout.LayoutParams(-1, 0, 1); }
    static void navigation(AppCompatActivity a, LinearLayout root) {
        LinearLayout nav = new LinearLayout(a); nav.setGravity(Gravity.CENTER); nav.setOrientation(LinearLayout.HORIZONTAL);
        Button pantry = button(a, "Pantry"), recipes = button(a, "Recipes"), settings = button(a, "Settings");
        pantry.setOnClickListener(v -> a.startActivity(new android.content.Intent(a, PantryActivity.class)));
        recipes.setOnClickListener(v -> a.startActivity(new android.content.Intent(a, SuggestedRecipesActivity.class)));
        settings.setOnClickListener(v -> a.startActivity(new android.content.Intent(a, SettingsActivity.class)));
        nav.addView(pantry, new LinearLayout.LayoutParams(0, -2, 1)); nav.addView(recipes, new LinearLayout.LayoutParams(0, -2, 1)); nav.addView(settings, new LinearLayout.LayoutParams(0, -2, 1)); root.addView(nav);
    }
}
