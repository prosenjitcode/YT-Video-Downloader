package com.bengalitutorial.ytvideodownloder.service;

import com.bengalitutorial.ytvideodownloder.model.VideoRespons;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceInterface {

    @GET("search")
    Call<VideoRespons> getVideoList(@Query("part") String part,
                                    @Query("maxResults") String maxResults,
                                    @Query("pageToken") String pageToken,
                                    @Query("q") String q,
                                    @Query("type") String type,
                                    @Query("key") String key
    );

    @GET("search")
    Call<VideoRespons> getRelatedVideoList(@Query("part") String part,
                                    @Query("relatedToVideoId") String relatedToVideoId,
                                    @Query("maxResults") String maxResults,
                                    @Query("pageToken") String pageToken,
                                    @Query("type") String type,
                                    @Query("key") String key
    );



}
