package com.example.thekaist.ui.dashboard;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thekaist.R;
import com.example.thekaist.UserInfo;
import com.example.thekaist.online_player;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    static Context context;
    private ArrayList<UserInfo> mList=null;


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView user_img;
        TextView user_name, user_record;

        public ViewHolder(View itemView) {
            super(itemView);

            user_img = itemView.findViewById(R.id.user_img);
            user_name = itemView.findViewById(R.id.user_name);
            user_record = itemView.findViewById(R.id.user_record);
        }
    }

    public DashboardAdapter(Context context, ArrayList<UserInfo> mlist) {

        this.context = context;
        this.mList = mlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.dashboard_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch(mList.get(position).getImgnumber()){
            case "1":
                holder.user_img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.character1));
                break;
            case "2":
                holder.user_img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.character2));
                break;
            case "3":
                holder.user_img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.character3));
                break;
            case "4":
                holder.user_img.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.character4));
                break;
            default:
                Glide.with(context).load(mList.get(position).getImgnumber()).into(holder.user_img);

        }
        holder.user_name.setText(mList.get(position).getName());
        holder.user_record.setText(mList.get(position).getWin()+"승 "+mList.get(position).getLose()+"패");

    }

    @Override
    public int getItemCount() {

        return mList.size();    }
}
