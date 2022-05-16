package com.example.redditdisplay_androiddevelopmentchallenge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.ArrayList;
import java.util.List;

import model.Feed;
import model.entry.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class viewComments extends AppCompatActivity {

    private static final String TAG ="ViewComments";

    //Incoming Intents
    private static String postURL;
    private static String postThumbnailURL;
    private static String postTitle;
    private static String postAuthor;

    //TextViews
    private TextView title,
                    author;
    //ImageView
    private ImageView thumbnail;

    //default image when post does not have a thumbnail
    private int defaultImage;

    //BASE URL
    private static final String BASE_URL = "https://www.reddit.com/r/";

    //The comment URL -- past /r/
    private String currentFeed;

    private ListView mListView;

    private ArrayList<ModelComments> mComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_comments_layout);

        setTitle("Comments");

        Log.d(TAG, "onCreate: Started\n");

        //load post thumbnail
        setupImageLoader();

        //Initiating Posts
        initiatePost();

        //Initiating Comments
        initiateComments();


    }

    private void initiateComments(){

        //Using retrofit to convert BaseURL to XML
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        //Declaring feedAPI object
        API_Feed feedAPI = retrofit.create(API_Feed.class);

        Call<Feed> call = feedAPI.getFeed(currentFeed);

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                List<Entry> entrys = response.body().getEntrys();
                for (int i = 0; i< entrys.size();i++){
                    Log.d(TAG, "onResponse: Entry: " + entrys.get(i).toString());
                    //Extracts the comment
                    ExtractXML extractComment = new ExtractXML(entrys.get(i).getContent(), "<div class=\"md\"><p>", "</p>");
                    //Adds comment to List
                    List<String> commentsInfo = extractComment.start();

                    try{
                        mComments.add(new ModelComments(
                                commentsInfo.get(0),
                                entrys.get(i).getAuthor().getName(),
                                entrys.get(i).getId()
                        ));
                    }//Comment is formatted in a way XML can't read correctly
                    catch (IndexOutOfBoundsException e){
                        mComments.add(new ModelComments(
                                "Error reading comment",
                                "None",
                                "None"
                        ));
                        Log.e(TAG, "onResponse: IndexOutOfBoundException: " + e.getMessage());
                    }//Comment does not have an author
                    catch (NullPointerException e){
                        mComments.add(new ModelComments(
                                commentsInfo.get(0),
                                "None",
                                entrys.get(i).getId()
                        ));
                        Log.e(TAG, "onResponse: NullPointerException: " + e.getMessage());
                    }

                }

                mListView = (ListView) findViewById(R.id.comments_listview);
                CommentsListAdapter commentsListAdapter = new CommentsListAdapter(viewComments.this,R.layout.all_comments_layout,mComments);
                mListView.setAdapter(commentsListAdapter);

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS" + t.getMessage());
                Toast.makeText(viewComments.this, "An Error with RSS", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initiatePost(){
        //grabbing all sent info from MainActivity
        Intent incomingIntent = getIntent();
        postURL = incomingIntent.getStringExtra("@string/post_url");
        postThumbnailURL = incomingIntent.getStringExtra("@string/post_comment_thumbnail");
        postTitle = incomingIntent.getStringExtra("@string/post_comment_title");
        postAuthor = incomingIntent.getStringExtra("@string/post_comment_author");

        //TextView assignment
        title = findViewById(R.id.post_comment_title);
        author = findViewById(R.id.post_comment_author);
        //ImageView assignment
        thumbnail = findViewById(R.id.post_comment_thumbnail);

        //Setting each TextView with post Title and post Author
        title.setText(postTitle);
        author.setText(postAuthor);

        //Setting ImageView with post Thumbnail
        displayImage(postThumbnailURL, thumbnail);

        System.out.println("POSTURL: " +postURL);

        //Throws exception in case of NSFW post
        try{
            String[] splitURL = postURL.split(BASE_URL);
            //splits URL (https://www.reddit.com/r/) and grabs everything after it
            currentFeed = splitURL[1];
            Log.d(TAG, "initiate Post: current feed: " + currentFeed);
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "initiate Post: ArrayIndexOutOfBoundsException: " + e.getMessage());
        }
    }


    private void displayImage(String imageURL, ImageView imageThumbnail){

        //create the image loader object
        ImageLoader imageLoader = ImageLoader.getInstance();

        defaultImage = viewComments.this.getResources().getIdentifier("@drawable/reddit_icon",null,viewComments.this.getPackageName());

        //create display options
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        //download and display image from url
        imageLoader.displayImage(imageURL, imageThumbnail, options , new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }

        });

    }

    private void setupImageLoader(){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                viewComments.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

        defaultImage = viewComments.this.getResources().getIdentifier("@drawable/reddit_icon",null,viewComments.this.getPackageName());
    }




}
