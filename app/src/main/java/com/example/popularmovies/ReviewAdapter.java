package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter> {

    private String[] mReviewData;

    @NonNull
    @Override
    public ReviewAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        V
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
