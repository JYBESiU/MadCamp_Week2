package com.example.thekaist.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thekaist.R;
import com.example.thekaist.online_player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OnlineAdapter extends RecyclerView.Adapter<OnlineAdapter.ViewHolder> {

    private ArrayList<online_player> mList=null;
    private Context mcontext;
    private LayoutInflater inflater;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public OnlineAdapter(Context context, ArrayList<online_player> mList) {
        this.mcontext = context;
        this.mList = mList;
    }



    @NotNull
    @Override
    public ViewHolder onCreateViewHolder( @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.online_player,parent,false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OnlineAdapter.ViewHolder holder, int position) {

        TextView name, id;

        holder.view_name.setText(mList.get(position).getName());
        if(mList.get(position).getId().equals("true")){
            holder.view_id.setText("대기 중");
        }
        if(mList.get(position).getId().equals("playing")){
            holder.view_id.setText("게임 중");
        }





    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView view_name, view_id;


        public ViewHolder(@NonNull View itemView,  OnItemClickListener listener) {
            super(itemView);

            view_name = itemView.findViewById(R.id.item_name);
            view_id = itemView.findViewById(R.id.item_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });



        }
    }
}
