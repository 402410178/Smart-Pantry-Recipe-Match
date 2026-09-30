package za.ac.smartpantry;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.Holder> {
    public interface Listener { void onTap(Recipe recipe); }
    private final List<Recipe> items = new ArrayList<>(); private final Listener listener;
    public RecipeAdapter(Listener l) { listener = l; }
    public void setItems(List<Recipe> values) { items.clear(); items.addAll(values); notifyDataSetChanged(); }
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int type) { TextView v = new TextView(parent.getContext()); v.setTextSize(19); v.setPadding(16,17,16,17); v.setBackgroundColor(0xffffffff); RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(-1,-2); lp.setMargins(0,4,0,4); v.setLayoutParams(lp); return new Holder(v); }
    @Override public void onBindViewHolder(@NonNull Holder h, int p) { Recipe r=items.get(p); h.text.setText(r.name+"\n"+r.description); h.itemView.setOnClickListener(v->listener.onTap(r)); }
    @Override public int getItemCount() { return items.size(); }
    static class Holder extends RecyclerView.ViewHolder { final TextView text; Holder(View v){super(v);text=(TextView)v;} }
}
