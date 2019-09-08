package com.example.socketmessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListModelViewHolder>{

    private ArrayList<ListModel> dataList;
    private Context context;
    private String IMG_SERVER = "http://192.168.100.6/project/myproject/socketimg/uploadedimages/";

    public ListAdapter(ArrayList<ListModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public ListModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listview, parent, false);
        return new ListModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListModelViewHolder holder, int position) {
        String imgName = dataList.get(position).getImg();

        if(imgName != "null"){
            Glide.with(context).load(IMG_SERVER + imgName)
                    .thumbnail(Glide.with(context).load(R.drawable.loadingpink))
                    .fitCenter()
                    .into(holder.img);
        } else holder.img.setVisibility(View.GONE);

        holder.msg.setText(dataList.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ListModelViewHolder extends RecyclerView.ViewHolder{
        private TextView msg;
        private ImageView img;

        public ListModelViewHolder(final View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.textView);
            img = itemView.findViewById(R.id.imageView);
        }
    }
}

