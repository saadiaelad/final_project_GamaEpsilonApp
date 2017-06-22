package com.example.elad.gamaepsilonapp;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by elad on 20/06/2017.
 */

public class UserListView extends FirebaseRecyclerAdapter<UserListView.ViewHolder, User> {


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView firstNameView;
        TextView lastNameView;
        TextView phoneNumberView;
        TextView jobTitleView;
        TextView emailView;

        public ViewHolder(View view) {
            super(view);
            firstNameView = (TextView)view.findViewById(R.id.firstNameText);
            lastNameView = (TextView)view.findViewById(R.id.lastNameText);
            phoneNumberView = (TextView)view.findViewById(R.id.phoneNumberText);
            jobTitleView = (TextView)view.findViewById(R.id.jobTitleText);
            emailView = (TextView)view.findViewById(R.id.emailText);
        }


    }

    public UserListView(Query query, Class<User> userClass, @Nullable ArrayList<User> user,
                        @Nullable ArrayList<String> keys) {
        super(query, user, keys);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = getItem(position);
        holder.firstNameView.setText(user.getFirsName());
        holder.lastNameView.setText(user.getLastName());
        holder.phoneNumberView.setText(user.getPhoneNumber());
        holder.emailView.setText(user.getEmail());
        holder.jobTitleView.setText(user.getPermission());
    }

    protected void itemAdded(User item, String key, int position) {
        Log.d("UserListView", "Added a new item to the adapter.");
    }

    protected void itemChanged(User oldItem, User newItem, String key, int position) {
        Log.d("UserListView", "Changed an item.");
    }

    protected void itemRemoved(User item, String key, int position) {
        Log.d("UserListView", "Removed an item from the adapter.");
    }

    protected void itemMoved(User item, String key, int oldPosition, int newPosition) {
        Log.d("UserListView", "Moved an item.");
    }
}
