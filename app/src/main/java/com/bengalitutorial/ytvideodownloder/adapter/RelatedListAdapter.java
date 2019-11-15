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
import com.bengalitutorial.ytvideodownloder.model.Item;
import com.bengalitutorial.ytvideodownloder.service.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RelatedListAdapter extends RecyclerView.Adapter<RelatedListAdapter.RVH> {
    public static List<Item> itemList;
    private OnItemClickListener clickListener;
    private Context context;

    public RelatedListAdapter(OnItemClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        this.context = context;
        itemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RVH onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_video_list,
                parent,false);
        return new RVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVH holder, int position) {

        final Item itemP = itemList.get(position);

        if (itemP!=null){
            Picasso.get().load(itemP.getSnippet().getThumbnails().getMedium().getUrl())
                    .into(holder.imageView);
            holder.ti.setText(itemP.getSnippet().getTitle());
            holder.ch.setText(itemP.getSnippet().getChannelTitle());

            holder.buttonD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String youtubeLink = "http://youtube.com/watch?v=" + itemP.getId().getVideoId();
                    Intent intent = new Intent(context.getApplicationContext(), DownloadActivity.class);
                    intent.putExtra("URL",youtubeLink);
                    context.startActivity(intent);
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return itemList!=null?itemList.size():0;
    }

    public void AddAllVideo(List<Item> items) {
        if (itemList!=null){
            for (Item item : items) {
                add(item);
            }
        }else {
            this.itemList = items;
            notifyDataSetChanged();
        }
    }

    private void add(Item item) {
        itemList.add(item);
        notifyItemInserted(itemList.size() - 1);
    }

    public class RVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView,buttonD;
        private TextView ti,ch;

        public RVH(@NonNull View view) {
            super(view);

            imageView =(ImageView)view.findViewById(R.id.thumb);
            buttonD =(ImageView)view.findViewById(R.id.downloadB);
            ti =(TextView) view.findViewById(R.id.vTitle);
            ch =(TextView)view.findViewById(R.id.vChannel);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.itemClick(v,getAdapterPosition());

        }
    }
}
