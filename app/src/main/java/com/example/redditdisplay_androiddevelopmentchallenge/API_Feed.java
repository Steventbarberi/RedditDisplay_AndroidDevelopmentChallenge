package com.example.redditdisplay_androiddevelopmentchallenge;

import model.Feed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_Feed {

//    String BaseUrl = "https://www.reddit.com/r/androiddev/";

//    @GET(".rss")
//    Call<Feed> getFeed();

    //feed name is the url which we need the rss
    @GET("{feed_name}/.rss")
    //get method
    Call<Feed> getFeed(@Path("feed_name") String feed_name);

}
