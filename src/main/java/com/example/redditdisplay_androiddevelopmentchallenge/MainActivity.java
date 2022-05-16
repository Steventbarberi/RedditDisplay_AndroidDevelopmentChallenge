package com.example.redditdisplay_androiddevelopmentchallenge;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import model.Feed;
import model.entry.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //URL for the post to be pulled from
    private static final String BASE_URL = "https://www.reddit.com/r/all/";

    //Displaying the Top from r/all
    private String currentFeed = "top";
    private ListView viewPosts_listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting title of appbar
        setTitle("Posts");
        //initiating posts
        initiateMain();
    }



    private void initiateMain(){

        //XML Converter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        API_Feed feedApi = retrofit.create(API_Feed.class);

        //getting the feed which in this case is "top"
        Call<Feed> call = feedApi.getFeed(currentFeed);

        //Used to add to Stack of posts
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                //grab entries
                List<Entry> entrys = response.body().getEntrys();

                //ArrayList that holds all the posts
                ArrayList<ModelPost> posts = new ArrayList<ModelPost>();

                //For loop iterates through the list of entrys
                for(int i = 0; i < entrys.size(); i++){
                    //extracting the url of the post Author for all entrys
                    ExtractXML extractXML_1 = new ExtractXML(entrys.get(i).getContent(), "<a href=");
                    //Adding the url of the author to a list postContent
                    List<String> postContent = extractXML_1.start();

                    //extracting the url of the thumbnail for entrys
                    ExtractXML extractXML_2 = new ExtractXML(entrys.get(i).getContent(), "<img src=");

                    //not all post have images -- Need try and catch
                    try{
                        //decoding the image url -- or else receive ERROR 403
                        postContent.add(Html.fromHtml(extractXML_2.start().get(0)).toString());
                    }//if image does not exist
                    catch (NullPointerException e){
                        postContent.add(null);
                        Log.e(TAG, "onReponse: NullPointerException(Thumbnail):" + e.getMessage());
                    }//if the tag does not exits
                    catch (IndexOutOfBoundsException e){
                        postContent.add(null);
                        Log.e(TAG, "onReponse: IndexOutOfBoundsException(Thumbnail):" + e.getMessage());
                    }

                    int lastPosition = postContent.size() - 1;

                    //Some posts may not have an author because user deleted the post
                    try{
                        posts.add(new ModelPost(
                                entrys.get(i).getTitle(),
                                entrys.get(i).getAuthor().getName(),
                                //Post URL
                                postContent.get(0),

                                postContent.get(lastPosition)
                        ));
                        //If comment does not have an author
                    }catch (NullPointerException e){
                        posts.add(new ModelPost(
                                entrys.get(i).getTitle(),
                               "None",
                                postContent.get(0),
                                postContent.get(lastPosition)
                        ));
                        Log.e(TAG,"onResponse: NullPointerException: " + e.getMessage());

                    }


                }
                //Initializing list view
                viewPosts_listView = (ListView) findViewById(R.id.ViewAll_Posts);
                //Initializing the customListAdapter for the posts to display within the list view
                CustomListAdapter customListAdapter = new CustomListAdapter(MainActivity.this, R.layout.all_posts_layout, posts);
                //Setting the list view adapter to the custom list adapter
                viewPosts_listView.setAdapter(customListAdapter);


                //On click of an individual post
                viewPosts_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.d(TAG, "onItemClicked: " + posts.get(i).toString());
                        //Intent to send user to comments activity
                        Intent commentsIntent = new Intent(MainActivity.this, viewComments.class);
                        //Sending info to the new activity
                        commentsIntent.putExtra("@string/post_url", posts.get(i).getPostURL());
                        commentsIntent.putExtra("@string/post_comment_thumbnail", posts.get(i).getThumbnailURL());
                        commentsIntent.putExtra("@string/post_comment_title", posts.get(i).getTitle());
                        commentsIntent.putExtra("@string/post_comment_author", posts.get(i).getAuthor());
                        //Starting intent/sending user to comments activity
                        startActivity(commentsIntent);
                    }
                });

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Unable to retrieve RSS" + t.getMessage());
                Toast.makeText(MainActivity.this, "An Error with RSS", Toast.LENGTH_SHORT).show();
            }
        });


    }



}