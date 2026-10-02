package za.ac.smartpantry;

import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class IngredientFormActivity extends AppCompatActivity {
    private PantryDatabase database; private EditText name, quantity, unit; private Button saveButton; private long id = -1;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); database = new PantryDatabase(this); id = getIntent().getLongExtra("id", -1);
        LinearLayout root = Ui.page(this, id == -1 ? "Add Ingredient" : "Edit Ingredient"); setContentView(root);
        name = field("Ingredient name", InputType.TYPE_CLASS_TEXT); quantity = field("Quantity (must be greater than 0)", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); unit = field("Unit (g, kg, ml, l, count, tbsp...)", InputType.TYPE_CLASS_TEXT);
        if (id != -1) { name.setText(getIntent().getStringExtra("name")); quantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity", 1))); unit.setText(getIntent().getStringExtra("unit")); }
        saveButton = Ui.button(this, id == -1 ? "Save ingredient" : "Save changes");
        root.addView(saveButton);
        saveButton.setOnClickListener(v -> save());
        root.addView(Ui.button(this, "Cancel")); ((android.widget.Button)root.getChildAt(root.getChildCount()-1)).setOnClickListener(v -> finish());
    }
    private EditText field(String hint, int type) { EditText e = new EditText(this); e.setSingleLine(true); e.setInputType(type); e.setHint(hint); getRoot().addView(e); return e; }
    private LinearLayout getRoot() { return (LinearLayout)((android.view.ViewGroup)findViewById(android.R.id.content)).getChildAt(0); }
    private void save() {
        String n = name.getText().toString().trim(), u = unit.getText().toString().trim(), q = quantity.getText().toString().trim();
        if (n.isEmpty()) { name.setError("Enter an ingredient name"); name.requestFocus(); return; }
        if (u.isEmpty()) { unit.setError("Enter a unit"); unit.requestFocus(); return; }
        double amount; try { amount = Double.parseDouble(q); } catch (NumberFormatException ex) { quantity.setError("Enter a valid quantity"); quantity.requestFocus(); return; }
        if (!(amount > 0) || Double.isInfinite(amount)) { quantity.setError("Quantity must be greater than zero"); quantity.requestFocus(); return; }
        saveButton.setEnabled(false);
        DatabaseExecutor.execute(() -> {
            try {
                if (id == -1) database.addIngredient(n, amount, u); else database.updateIngredient(id, n, amount, u);
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (RuntimeException error) {
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        saveButton.setEnabled(true);
                        Toast.makeText(this, "Could not save ingredient. Please try again.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
