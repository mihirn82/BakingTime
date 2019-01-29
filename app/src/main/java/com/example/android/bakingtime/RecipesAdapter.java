package com.example.android.bakingtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mihirnewalkar on 9/16/17.
 */

//public class RecipesAdapter extends ArrayAdapter<Recipes> {
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private static final String LOG_TAG = "RecipesAdapter";

    private List<Recipes> mRecipesList = new ArrayList<>();

    /**
     * Since you have just one view type in the recycler view, set this in the constructor and pass it along to
     * every adapter
     */
    private RecipeCardClickListener listener;

    void swapData(List<Recipes> recipes) {
        mRecipesList.clear();
        mRecipesList.addAll(recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapter.ViewHolder holder, int position) {
        holder.bind(mRecipesList.get(position));
    }


    @Override
    public int getItemCount() {
        return mRecipesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView servings;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.recipe_name_tv);
            servings = itemView.findViewById(R.id.servings_tv);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onRecipeCardClicked(view, getAdapterPosition());
            }
        }

        void bind(Recipes recipes) {
            name.setText(recipes.getName());
            String servingsText = servings.getContext().getString(R.string.servings, Integer.valueOf(recipes.getId()));
            servings.setText(servingsText);
        }
    }

    @Nullable
    public Recipes getItem(int position) {
        if (position > mRecipesList.size() || position < 0) {
            return null;
        } else {
            return mRecipesList.get(position);
        }
    }

    public interface RecipeCardClickListener{

        /**
         * Method for when recipe is clicked
         *
         * @param view View of ViewHolder
         * @param position Position of item clicked in recycler view
         */
        void onRecipeCardClicked(View view, int position);
    }

    /**
     * Custom click listener
     */
    public void setOnItemClickListener(RecipeCardClickListener listener) {
        this.listener = listener;
    }
}
