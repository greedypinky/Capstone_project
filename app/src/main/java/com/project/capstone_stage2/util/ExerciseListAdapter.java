package com.project.capstone_stage2.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

// https://developer.android.com/training/material/lists-cards.html
// RecyclerView provides these built-in layout managers:
//
// LinearLayoutManager shows items in a vertical or horizontal scrolling list.
//        GridLayoutManager shows items in a grid.
//        StaggeredGridLayoutManager shows items in a staggered grid.

public class ExerciseListAdapter extends RecyclerView.Adapter {
    // TODO: activity need to implement this listener 's call back and pass into the adapter
    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        public void onClickListener();
    }

    // Default Constructor
    public ExerciseListAdapter() {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * ExerciseViewHolder
     */
    public static class ExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ExerciseViewHolder(View itemView) {
            // initialize the item
            // itemView.findViewById()
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
