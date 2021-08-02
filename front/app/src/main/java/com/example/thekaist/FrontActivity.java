package com.example.thekaist;

import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekaist.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kakao.auth.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_LONG;

public class FrontActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;

    public static Socket mSocket;

    private Context activity = this;
    int flag = 0;

    public static String id;

    public String ask, accept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        setContentView(R.layout.activity_front);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String imgnumber = intent.getStringExtra("imgnumber");


        if(!imgnumber.equals("")){
            HashMap<String, String> map = new HashMap<>();

            map.put("name", name);
            map.put("id", id);
            map.put("imgnumber", imgnumber);

            Call<Void> call = retrofitInterface.executeKakaosignup(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 200) {
                        Log.d("kakao", "200");

                    } else if (response.code() == 400) {

                        Log.d("kakao", "400");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(FrontActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        init();
    }

    private void init() {
        try {
            mSocket = IO.socket(BASE_URL);
            Log.d("SOCKET info", "Connection success : " + mSocket.toString());
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT, waitBattle);
        mSocket.on("challengeCome", challengeCome);
        mSocket.on("startGame", startGame);
        mSocket.on("yourRejected", yourRejected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    public static Emitter.Listener waitBattle = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mSocket.emit("waitBattle", id);
        }
    };

    public  Emitter.Listener yourRejected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Toast.makeText(getApplicationContext(), "거절당했습니다...", Toast.LENGTH_SHORT).show();
        }
    };

    public Emitter.Listener challengeCome = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(() -> {
                Log.d("TTTTTTA", "challengeCome");
                ask = args[0].toString();
                accept = args[1].toString();

                View view = getLayoutInflater().inflate(R.layout.pass_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                AlertDialog ad = builder.setView(view).setCancelable(false).create();

                Button yes_button = view.findViewById(R.id.yes);
                Button no_button = view.findViewById(R.id.no);

                TextView msg = view.findViewById(R.id.dialog_msg);

                msg.setText(ask + "에게서 대결 요청이 왔습니다. 수락하시겠습니까?");

                yes_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSocket.emit("acceptGame", ask, accept);
                        ad.dismiss();
                    }
                });

                no_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSocket.emit("challengeReject", ask, accept);
                        ad.dismiss();
                    }
                });

                ad.show();
            });
        }
    };

    public Emitter.Listener startGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("ask", args[0].toString());
            intent.putExtra("accept", args[1].toString());
            intent.putExtra("roomid", (int)args[4]);

            HashMap<String, String> map = new HashMap<>();

            map.put("id", id);
            Call<Void> call = retrofitInterface.executeChangePlay(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code()==200){
                        Log.d("playing", "succeed");
                    }
                    else if(response.code()==404){
                        
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

            JSONArray jsonArray1 = (JSONArray) args[2];
            ArrayList<Integer> list1 = new ArrayList<Integer>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                try {
                    list1.add(jsonArray1.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            JSONArray jsonArray2 = (JSONArray) args[3];
            ArrayList<Integer> list2 = new ArrayList<Integer>();
            for (int i = 0; i < jsonArray2.length(); i++) {
                try {
                    list2.add(jsonArray1.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            intent.putExtra("cards_order", list1);
            intent.putExtra("nums_order", list2);

            startActivity(intent);
        }
    };

    private long time= 0;

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

        else if(System.currentTimeMillis() - time < 2000 ){
            HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

            map.put("id", id);

            Call<Void> call = retrofitInterface.executeLogout(map);//로그인리절트 클래스 부르는데저 map넣어서 함

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() == 200){
                        Log.d("look", "changed");
                        flag = 1;
                        finish();
                    }
                    else if(response.code()==404){
                        Log.d("look", "not changed");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });

        }
    }

}