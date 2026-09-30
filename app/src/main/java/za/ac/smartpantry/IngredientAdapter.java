package za.ac.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.Holder> {
    public interface Listener { void onTap(Ingredient item); void onLongPress(Ingredient item); }
    private final List<Ingredient> items = new ArrayList<>(); private final Listener listener;
    public IngredientAdapter(Listener listener) { this.listener = listener; }
    public void setItems(List<Ingredient> updated) { items.clear(); items.addAll(updated); notifyDataSetChanged(); }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        TextView text = new TextView(parent.getContext()); text.setTextSize(18); text.setPadding(16, 15, 16, 15); text.setBackgroundColor(0xFFFFFFFF);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(-1, -2); lp.setMargins(0, 4, 0, 4); text.setLayoutParams(lp); return new Holder(text);
    }
    @Override public void onBindViewHolder(@NonNull Holder h, int position) {
        Ingredient item = items.get(position); h.text.setText(item.name + "\n" + format(item.quantity) + " " + item.unit);
        h.itemView.setOnClickListener(v -> listener.onTap(item)); h.itemView.setOnLongClickListener(v -> { listener.onLongPress(item); return true; });
    }
    private String format(double d) { return d == Math.rint(d) ? String.valueOf((long)d) : String.valueOf(d); }
    @Override public int getItemCount() { return items.size(); }
    static class Holder extends RecyclerView.ViewHolder { final TextView text; Holder(View v) { super(v); text = (TextView)v; } }
}
