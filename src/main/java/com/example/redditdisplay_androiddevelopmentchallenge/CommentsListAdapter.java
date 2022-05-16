package com.example.redditdisplay_androiddevelopmentchallenge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;


public class CommentsListAdapter extends ArrayAdapter<ModelComments> {

    private static final String TAG = "CommentsListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    //Holder for view
    private static class ViewHolder {
        TextView commentTextView;
        TextView authorTextView;
    }

    /*
    Default constructor with Parameters:
    Context, Resource, and Arraylist that holds the Comments
     */
    public CommentsListAdapter(Context context, int resource, ArrayList<ModelComments> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String comment = getItem(position).getComment();
        String author = getItem(position).getAuthor();

        try{
            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            final ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new ViewHolder();
                holder.commentTextView = (TextView) convertView.findViewById(R.id.comment);
                holder.authorTextView = (TextView) convertView.findViewById(R.id.comment_author);

                result = convertView;
                convertView.setTag(holder);
            }
            else{
                holder = (CommentsListAdapter.ViewHolder) convertView.getTag();
                result = convertView;
            }

            lastPosition = position;

            //Setting text to be displayed withing comment view and author view
            holder.commentTextView.setText(comment);
            holder.authorTextView.setText(author);

            return convertView;

        }catch (IllegalArgumentException e){
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }


}
