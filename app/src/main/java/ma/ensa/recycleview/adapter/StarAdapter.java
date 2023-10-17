package ma.ensa.recycleview.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.recycleview.R;
import ma.ensa.recycleview.beans.Star;
import ma.ensa.recycleview.service.StarService;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> implements Filterable {
    private static final String TAG = "StarAdapter";
    private List<Star> stars;
    private List<Star> starsFilter;
    private Context context;
    private NewFilter mfilter;
    public StarAdapter(Context context, List<Star> stars) {
        this.stars = stars;
        this.context = context;
        starsFilter = new ArrayList<>();
        starsFilter.addAll(stars);
        mfilter = new NewFilter(this);
    }

    public void setStar(List<Star> stars){
        this.stars = stars;
    }

    public List<Star> getStars(){
        return this.stars;
    }
    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.star_item, viewGroup, false);
        final StarViewHolder holder = new StarViewHolder(v);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = LayoutInflater.from(context).inflate(R.layout.star_edit_item, null,
                        false);
                final ImageView img = popup.findViewById(R.id.img);
                final RatingBar bar = popup.findViewById(R.id.ratingBar);
                final TextView idss = popup.findViewById(R.id.idss);
                Bitmap bitmap =
                        ((BitmapDrawable)((ImageView)v.findViewById(R.id.img)).getDrawable()).getBitmap();
                img.setImageBitmap(bitmap);
                bar.setRating(((RatingBar)v.findViewById(R.id.stars)).getRating());
                idss.setText(((TextView)v.findViewById(R.id.ids)).getText().toString());
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Notez : ")
                        .setMessage("Donner une note entre 1 et 5 :")
                        .setView(popup)
                        .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                float s = bar.getRating();
                                int ids = Integer.parseInt(idss.getText().toString());
                                Star star = StarService.getInstance().findById(ids);
                                star.setStar(s);
                                StarService.getInstance().update(star);
                                notifyItemChanged(holder.getAdapterPosition());
                            }
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull StarViewHolder starViewHolder, int i) {
        Log.d(TAG, "onBindView call ! "+ i);
        Glide.with(context)
                .asBitmap()
                .load(starsFilter.get(i).getImg())
                .apply(new RequestOptions().override(100, 100))
                .into(starViewHolder.img);
        starViewHolder.name.setText(starsFilter.get(i).getName().toUpperCase());
        starViewHolder.stars.setRating(starsFilter.get(i).getStar());
        starViewHolder.idss.setText(starsFilter.get(i).getId()+"");
    }
    @Override
    public int getItemCount() {
        return starsFilter.size();
    }
    @Override
    public Filter getFilter() {
        return mfilter;
    }
    public class StarViewHolder extends RecyclerView.ViewHolder {
        TextView idss;
        ImageView img;
        TextView name;
        RatingBar stars;
        RelativeLayout parent;
        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            idss = itemView.findViewById(R.id.ids);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            stars = itemView.findViewById(R.id.stars);
            parent = itemView.findViewById(R.id.parent);
        }
    }
    public class NewFilter extends Filter {
        public RecyclerView.Adapter mAdapter;
        public NewFilter(RecyclerView.Adapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        public NewFilter(RecyclerView.Adapter mAdapter, List<Star> stars) {
            super();
            this.mAdapter = mAdapter;
            starsFilter = new ArrayList<>();
            starsFilter.addAll(stars);
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            starsFilter.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                starsFilter.addAll(stars);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Star p : stars) {
                    if (p.getName().toLowerCase().startsWith(filterPattern)) {
                        starsFilter.add(p);
                    }
                }
            }
            results.values = starsFilter;
            results.count = starsFilter.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            starsFilter = (List<Star>) filterResults.values;
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
