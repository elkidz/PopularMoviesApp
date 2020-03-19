package com.example.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmovies.data.Review;
import com.example.popularmovies.utils.MovieJsonUtils;

import org.json.JSONException;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private String[] mReviewData;

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        String reviewJson = mReviewData[position];

        try {
            Review review = MovieJsonUtils.getReviewFromJson(reviewJson);

            holder.mReviewAuthor.setText(review.getAuthor());
            holder.mReviewContent.setText(review.getContent());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (null == mReviewData) return 0;
        return mReviewData.length;
    }

    void setReviewData(String[] reviewData) {
        mReviewData = reviewData;
        notifyDataSetChanged();
    }

    static class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mReviewAuthor;
        final TextView mReviewContent;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.tv_review_author);
            mReviewContent = view.findViewById(R.id.tv_review_content);
        }
    }
}
