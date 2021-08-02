package com.example.thekaist.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekaist.Adapter.OnlineAdapter;
import com.example.thekaist.MainActivity;
import com.example.thekaist.R;
import com.example.thekaist.RetrofitInterface;
import com.example.thekaist.UserInfo;
import com.example.thekaist.online_player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends Fragment {
    RecyclerView recyclerView;
    DashboardAdapter dashboardAdapter;
    private SwipeRefreshLayout swipe;
    private TextView rank;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;
    private ArrayList<UserInfo> ranklist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = root.findViewById(R.id.show_rank);
        swipe = root.findViewById(R.id.swipe_rank);
        rank = root.findViewById(R.id.ranking);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


        getRanking();


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ranklist.clear();
                getRanking();

                dashboardAdapter = new DashboardAdapter(getContext(), ranklist);

                recyclerView.setAdapter(dashboardAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

                RecyclerView.LayoutManager layoutManager = linearLayoutManager;
                recyclerView.setLayoutManager(layoutManager);

                swipe.setRefreshing(false);
            }
        });
        return root;
    }

    private void getRanking() {
        Call<List<UserInfo>> call = retrofitInterface.executeRank();//로그인리절트 클래스 부르는데저 map넣어서 함

        ranklist = new ArrayList<>();
        call.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if (response.code() == 200) {

                    List<UserInfo> resultList = response.body();
                    Log.d("look", ""+resultList.get(0).getName()+" and "+resultList.size());

                    for(int i=0;i<resultList.size();i++) {
                        UserInfo result = resultList.get(i);
                        ranklist.add(result);

                        Log.d("look", ""+ranklist.size()+" and "+result.getId());

                    }

                    Collections.sort(ranklist, Collections.reverseOrder());
                    dashboardAdapter = new DashboardAdapter(getActivity(), ranklist);

                    Log.d("look", ""+ranklist.size());
                    recyclerView.setAdapter(dashboardAdapter);

                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Wrong Credential in ranks",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

        });
    }
}