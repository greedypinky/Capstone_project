package com.project.capstone_stage2.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.capstone_stage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private static int CATEGORY_COUNT = 3;
    private static HashMap<String,String> category = new HashMap<String,String>();
    private static ArrayList<ExerciseCategory> catArrayList = new ArrayList<ExerciseCategory>();
    private static CardViewOnClickListener cardViewOnClickListener;
    private Context mContext;

    // Constructor that require the listener implements the onClick callback method
    public CategoryListAdapter(CardViewOnClickListener listener,Context context) {
        cardViewOnClickListener = listener;
        initData(context);
    }

    public interface CardViewOnClickListener {
        public void onClickCategory(ExerciseCategory category);
    }

    private int getImageResource(String name) {

        int drawable = -1;
        switch(name) {

            case "Squat": {
                drawable =  R.drawable.nao_squat01;
                //drawable =  R.drawable.exercise_default;
                break;
            }

            case "Pull": {
                drawable = R.drawable.nao_pull01;
                //drawable =  R.drawable.exercise_default2;
                break;
            }

            case "Push": {
                drawable = R.drawable.nao_push01;
                //drawable =  R.drawable.exercise_default3;
                break;
            }

        };

        return drawable;
    }

    private void initData(Context context) {

        String[] category_array = context.getResources().getStringArray(R.array.category_array);
        String[] category_desc_array = context.getResources().getStringArray(R.array.category_desc_array);


        int i = 0;
        for(String cat:category_array) {
            // init default Data
            catArrayList.add(new ExerciseCategory(category_array[i], category_desc_array[i], getImageResource(category_array[i])));
            i++;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CategoryListAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext(); // get the Context reference from parent view
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card_view,parent,false);
        CategoryViewHolder vh = new CategoryViewHolder(cardView);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.CategoryViewHolder holder, int position) {
        // TODO: bind adapter's data to the view holder's view based on the position
        holder.mTitle.setText(catArrayList.get(position).getCategoryName());
        holder.mDesc.setText(catArrayList.get(position).getCategoryDesc());
       // holder.image.setImageResource(catArrayList.get(position).getImage());
        //  if(!selectedRecipe.getImage().isEmpty()) {
        // Picasso will handle loading the images on a background thread, image decompression and caching the images.
       Picasso.with(mContext).load(catArrayList.get(position).getImage()).into(holder.mImage);
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
        if(catArrayList != null && catArrayList.size() > 0) {
          return catArrayList.size();
        } else {
            return 0;
        }
    }

    /**
     * RecyclerViewHolder
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView mCardView;
        private TextView mTitle;
        private TextView mDesc;
        private ImageView mImage;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView)itemView.findViewById(R.id.category_card_view);
            mCardView.setOnClickListener(this); // must set the OnClickListener,otherwise it will not react to the click
            mTitle = (TextView) itemView.findViewById(R.id.execise_category_name);
            mDesc = (TextView) itemView.findViewById(R.id.execise_category_desc);
            mImage = (ImageView) itemView.findViewById(R.id.execise_category_image);

        }

        @Override
        public void onClick(View view) {
            // TODO : add back the call back to launch the Activity
            int cardIndex = getAdapterPosition();
            if (cardViewOnClickListener != null) {

                ExerciseCategory category = catArrayList.get(cardIndex);
                cardViewOnClickListener.onClickCategory(category); // when click,pass the category data back to the activity
            } else {
                throw new RuntimeException("No onClick handler for ViewHolder! Please add it back!");
            }
        }
    }

    // inner class for Exercise Category
    public class ExerciseCategory {

        private String categoryName = null;
        private String categoryDesc = null;
        private int categoryImage = -1;

        public ExerciseCategory(String name, String desc, int drawableImage) {
            categoryName = name;
            categoryDesc = desc;
            categoryImage = drawableImage;
        }

        public String getCategoryDesc() {
            return categoryDesc;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public int getImage() {
            return categoryImage;
        }


    }


}
