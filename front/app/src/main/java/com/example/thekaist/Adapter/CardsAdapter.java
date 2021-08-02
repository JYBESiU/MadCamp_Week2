package com.example.thekaist.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.thekaist.FrontActivity;
import com.example.thekaist.GameActivity;
import com.example.thekaist.R;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private static Context context;

    static ArrayList<Drawable> image_list = new ArrayList<Drawable>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView card_img;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            card_img = itemView.findViewById(R.id.card_img);
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

    public CardsAdapter(Context context, ArrayList<Drawable> image_list) {
        this.context = context;
        this.image_list = image_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cards_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.card_img.setImageDrawable(image_list.get(position));
    }

    @Override
    public int getItemCount() {
        return 16;
    }



}
