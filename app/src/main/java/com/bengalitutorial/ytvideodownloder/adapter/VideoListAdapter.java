package com.bengalitutorial.ytvideodownloder.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bengalitutorial.ytvideodownloder.DownloadActivity;
import com.bengalitutorial.ytvideodownloder.R;
import com.bengalitutorial.ytvideodownloder.animutil.AnimatorUtil;
import com.bengalitutorial.ytvideodownloder.model.Item;
import com.bengalitutorial.ytvideodownloder.service.OnItemClickListener;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VH> {

    public static List<Item> itemList;
    private Context context;
    private OnItemClickListener clickListener;
    private int currentPos = 0;


    public VideoListAdapter(Context context, OnItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
        itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.video_list, viewGroup, false
        );

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        final Item item = itemList.get(i);

        if (item != null) {

            Picasso.get().load(item.getSnippet().getThumbnails().getMedium().getUrl())
                    .into(vh.thumb);


            vh.title.setText(item.getSnippet().getTitle());
            vh.channel.setText(item.getSnippet().getChannelTitle());
            vh.pubDate.setText(item.getSnippet().getPublishedAt());
            vh.dButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String youtubeLink = "http://youtube.com/watch?v=" + item.getId().getVideoId();
                    String title = item.getSnippet().getTitle();
                    Intent intent = new Intent(context.getApplicationContext(), DownloadActivity.class);
                    intent.putExtra("URL",youtubeLink);
                    intent.putExtra("TITLE",title);
                    context.startActivity(intent);
                }
            });
        }

        if (i>currentPos){
            AnimatorUtil.AnimatorU(vh,true);//scroll down
        }else {
            AnimatorUtil.AnimatorU(vh,false);//scroll up
        }
        currentPos=i;

    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void AddAllVideo(List<Item> items) {
           for (Item item : items) {
               add(item);
           }
    }

    private void add(Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size()-1);
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumb,dButton;
        TextView title, channel,pubDate;

        public VH(@NonNull View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumbnails);
            dButton = (ImageView) itemView.findViewById(R.id.downloadNow);
            title = (TextView) itemView.findViewById(R.id.title);
            channel = (TextView) itemView.findViewById(R.id.channelName);
            pubDate = (TextView) itemView.findViewById(R.id.publishAt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            clickListener.itemClick(v,getAdapterPosition());

        }
    }


}
