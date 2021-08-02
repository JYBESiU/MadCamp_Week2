package com.example.thekaist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.thekaist.Adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccessHistory extends AppCompatActivity {

    private TextView title;
    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;
    private String id;
    private ArrayList<history_item> histlist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_history);


        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        title = findViewById(R.id.History);
        recyclerView = findViewById(R.id.show_history);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        historyAdapter = new HistoryAdapter(getApplicationContext(), histlist);


        //배틀에서 가져와야함 레트로핏 생성예정
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);

        Call<List<battleinfo>> call = retrofitInterface.executeHistory(map);

        call.enqueue(new Callback<List<battleinfo>>() {
            @Override
            public void onResponse(Call<List<battleinfo>> call, Response<List<battleinfo>> response) {
                if(response.code() == 200){
                    List<battleinfo> resultList = response.body();

                    for(battleinfo result: resultList){
                        Log.d("history", ""+result.getAsk());
                        history_item item = new history_item();

                        if(result.getAccept().equals(id)){
                            //accept 칸이 본인일때
                            if(result.getWinner().equals(id)){
                                //이겼을때
                                item = new history_item("WIN", result.getAccept_scr().toString(), result.getAsk_scr().toString(), result.getAsk());
                            }
                            else if(result.getLoser().equals(id)){
                                item = new history_item("LOSE", result.getAccept_scr().toString(), result.getAsk_scr().toString(), result.getAsk());
                            }
                        }
                        else if(result.getAsk().equals(id)){
                            //ask가 본인일때
                            if(result.getWinner().equals(id)){
                                //이겼을때
                                item = new history_item("WIN", result.getAsk_scr().toString(), result.getAccept_scr().toString(), result.getAccept());
                            }
                            else if(result.getLoser().equals(id)){
                                item = new history_item("LOSE", result.getAsk_scr().toString(), result.getAccept_scr().toString(), result.getAccept());
                            }
                        }

                        histlist.add(item);
                    }
                    historyAdapter = new HistoryAdapter(getApplicationContext(), histlist);
                    recyclerView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<battleinfo>> call, Throwable t) {

            }
        });





    }
}