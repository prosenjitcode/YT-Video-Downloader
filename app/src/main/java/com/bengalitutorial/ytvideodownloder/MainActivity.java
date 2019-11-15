package com.bengalitutorial.ytvideodownloder;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.bengalitutorial.ytvideodownloder.adapter.VideoListAdapter;
import com.bengalitutorial.ytvideodownloder.api.RetrofitClient;
import com.bengalitutorial.ytvideodownloder.constans.Constans;
import com.bengalitutorial.ytvideodownloder.model.VideoRespons;
import com.bengalitutorial.ytvideodownloder.service.OnItemClickListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.credentials.CredentialRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bengalitutorial.ytvideodownloder.adapter.VideoListAdapter.itemList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private VideoListAdapter adapter;

    private ShimmerFrameLayout shimmerFrameLayout;

    private static final int FIRST_PAGE = 1;
    private int TOTAL_PAGE = 10;
    private boolean isLoaded = false;
    private boolean isLastPage = false;
    private int CURRENT_PAGE = FIRST_PAGE;
    private String nextPageToken = "";

    private ProgressBar progressBar;
    private String query;
    private CredentialRequest mCredentialRequest;
    private int RC_READ = 901;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_container);
        shimmerFrameLayout.startShimmer();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.vlist);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new VideoListAdapter(this, this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            videoApi(query);

        }

        videoApi(query);


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
                        loadMoreItem(query);
                    }
                }
            }
        });

    }




    private void loadMoreItem(String query) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getService().getVideoList("snippet",
                "10", nextPageToken, query, "video", Constans.API_KEY)
                .enqueue(new Callback<VideoRespons>() {
                    @Override
                    public void onResponse(Call<VideoRespons> call, Response<VideoRespons> response) {
                        if (response.body() != null) {
                            if (response.isSuccessful()) {
                                isLoaded = false;
                                adapter.AddAllVideo(response.body().getItems());
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                nextPageToken = response.body().getNextPageToken();
                                if (CURRENT_PAGE == TOTAL_PAGE)
                                    isLastPage = true;

                            }
                        }
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<VideoRespons> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void videoApi(String query) {
        RetrofitClient.getInstance().getService().getVideoList("snippet",
                "10", nextPageToken, query, "video", Constans.API_KEY)
                .enqueue(new Callback<VideoRespons>() {
                    @Override
                    public void onResponse(Call<VideoRespons> call, Response<VideoRespons> response) {
                        if (response.body() != null) {
                            if (response.isSuccessful()) {

                                adapter.AddAllVideo(response.body().getItems());
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                nextPageToken = response.body().getNextPageToken();

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<VideoRespons> call, Throwable t) {
                        //Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void itemClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, YTPlayer.class);
        intent.putExtra("VIDEOID", itemList.get(position).getId().getVideoId());
        intent.putExtra("TITLE", itemList.get(position).getSnippet().getTitle());
        intent.putExtra("CHANNEL", itemList.get(position).getSnippet().getChannelTitle());
        startActivity(intent);
    }


}
