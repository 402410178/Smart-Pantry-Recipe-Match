package za.ac.smartpantry;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); PantryDatabase database = new PantryDatabase(this); LinearLayout root = Ui.page(this,"Settings"); setContentView(root);
        root.addView(Ui.label(this,"Preferences",20)); Switch alerts = new Switch(this); alerts.setText("Expiring-soon alerts (preference)"); alerts.setPadding(2,16,2,16); alerts.setEnabled(false);
        alerts.setOnCheckedChangeListener((CompoundButton b, boolean enabled) -> DatabaseExecutor.execute(() -> database.setExpiryAlerts(enabled))); root.addView(alerts);
        DatabaseExecutor.execute(() -> {
            boolean savedValue = database.getExpiryAlerts();
            runOnUiThread(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    alerts.setChecked(savedValue);
                    alerts.setEnabled(true);
                }
            });
        });
        root.addView(Ui.label(this,"This preference is saved on this device. Pantry and recipe data are stored locally in SQLite.",16)); Ui.navigation(this,root);
    }
}
