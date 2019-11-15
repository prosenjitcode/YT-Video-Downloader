package com.bengalitutorial.ytvideodownloder;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bengalitutorial.ytvideodownloder.adapter.RelatedListAdapter;
import com.bengalitutorial.ytvideodownloder.api.RetrofitClient;
import com.bengalitutorial.ytvideodownloder.constans.Constans;
import com.bengalitutorial.ytvideodownloder.model.VideoRespons;
import com.bengalitutorial.ytvideodownloder.service.OnItemClickListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YTPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        OnItemClickListener {

    private static final int RECOVERY_ID = 1;
    private YouTubePlayerView playerView;
    private YouTubePlayer youTubePlayer;
    private String videoId, title, channel;

    private TextView textViewTitle, textViewChannel;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RelatedListAdapter adapter;

    private static final int FIRST_PAGE = 1;
    private int TOTAL_PAGE = 10;
    private boolean isLoaded = false;
    private boolean isLastPage = false;
    private int CURRENT_PAGE = FIRST_PAGE;
    private String nextPageToken = "";

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytplayer);

        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("VIDEOID");
        title = bundle.getString("TITLE");
        channel = bundle.getString("CHANNEL");

        progressBar =(ProgressBar)findViewById(R.id.progressBar2);
        playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        playerView.initialize(Constans.API_KEY, this);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewChannel = (TextView) findViewById(R.id.textViewChannel);

        recyclerView = (RecyclerView) findViewById(R.id.rRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RelatedListAdapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int viewItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoaded && !isLastPage) {
                    if (viewItemCount + firstVisibleItemPosition == totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        isLoaded = true;
                        CURRENT_PAGE += 1;
                        loadMoreItem();
                    }
                }
            }
        });
        if (savedInstanceState != null) {

            videoId = savedInstanceState.getString("videoId");
            title = savedInstanceState.getString("title");
            channel = savedInstanceState.getString("channel");
            textViewTitle.setText(title);
            textViewChannel.setText(channel);
        }


        RetrofitClient.getInstance().getService().getRelatedVideoList("snippet",
                videoId, "10", nextPageToken, "video", Constans.API_KEY).enqueue(
                new Callback<VideoRespons>() {
                    @Override
                    public void onResponse(Call<VideoRespons> call, Response<VideoRespons> response) {
                        if (response.body() != null) {
                            if (response.isSuccessful()) {

                                adapter.AddAllVideo(response.body().getItems());
                                nextPageToken = response.body().getNextPageToken();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoRespons> call, Throwable t) {

                    }
                }
        );

    }

    private void loadMoreItem() {
progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getService().getRelatedVideoList("snippet",
                videoId, "10", nextPageToken, "video", Constans.API_KEY).enqueue(
                new Callback<VideoRespons>() {
                    @Override
                    public void onResponse(Call<VideoRespons> call, Response<VideoRespons> response) {
                        if (response.body() != null) {
                            if (response.isSuccessful()) {
                                isLoaded = false;
                                adapter.AddAllVideo(response.body().getItems());

                                nextPageToken = response.body().getNextPageToken();
                                if (CURRENT_PAGE == TOTAL_PAGE)
                                    isLastPage = true;
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }


                    @Override
                    public void onFailure(Call<VideoRespons> call, Throwable t) {

                    }
                }
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putString("title", title);
        bundle.putString("channel", channel);
        bundle.putString("videoId", videoId);
    }

    @Override
    protected void onDestroy() {

        if (youTubePlayer != null) {
            youTubePlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        this.youTubePlayer = youTubePlayer;
        if (!b && youTubePlayer != null) {

            youTubePlayer.cueVideo(videoId);
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            textViewTitle.setText(title);
            textViewChannel.setText(channel);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_ID).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECOVERY_ID) {
            getYoutubePlayerProvider().initialize(Constans.API_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYoutubePlayerProvider() {
        return playerView;
    }

    @Override
    public void itemClick(View view, int position) {

        String videoIdL = RelatedListAdapter.itemList.get(position).getId().getVideoId();

        if (videoIdL != null && youTubePlayer != null) {

            youTubePlayer.cueVideo(videoIdL);
            title = RelatedListAdapter.itemList.get(position).getSnippet().getTitle();
            channel = RelatedListAdapter.itemList.get(position).getSnippet().getChannelTitle();

            textViewTitle.setText(title);
            textViewChannel.setText(channel);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
