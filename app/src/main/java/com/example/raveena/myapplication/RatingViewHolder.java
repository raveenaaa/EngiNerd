package com.example.raveena.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingViewHolder extends RecyclerView.ViewHolder {
    public TextView username, comments;
    public RatingBar orating;

    public RatingViewHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.ratingUsername);
        orating = (RatingBar)itemView.findViewById(R.id.ratingStars);
        comments = (TextView)itemView.findViewById(R.id.ratingComments);
    }

    public void setElements(Rating rating) {
        username.setText(rating.username);
        orating.setRating(rating.orating);
        orating.setIsIndicator(true);
        comments.setText(rating.comments);

    }
}
