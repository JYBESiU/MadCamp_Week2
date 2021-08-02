package com.example.thekaist.ui.setting;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thekaist.AccessHistory;
import com.example.thekaist.FrontActivity;
import com.example.thekaist.GameActivity;
import com.example.thekaist.LoginResult;
import com.example.thekaist.MainActivity;
import com.example.thekaist.R;
import com.example.thekaist.RetrofitInterface;
import com.example.thekaist.UserInfo;
import com.example.thekaist.changeUserinfo;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private TextView profile_name, profile_id, profile_change, profile_history, develop, logout;
    private ImageView img;

    public String id = FrontActivity.id;
    public String imgnum, pwd;
    public static String name;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        profile_name = root.findViewById(R.id.user_name);
        profile_id = root.findViewById(R.id.user_id);
        profile_change = root.findViewById(R.id.change);
        profile_history = root.findViewById(R.id.history);
        logout = root.findViewById(R.id.logout);
        img = root.findViewById(R.id.User_pic);

        init();

        profile_id.setText("ID  "+id);
        profile_name.setText(name);

        
        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        
        profile_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {

                        HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

                        map.put("id", id);

                        Call<Void> call = retrofitInterface.executeLogout(map);//로그인리절트 클래스 부르는데저 map넣어서 함

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.code() == 200){
                                    Log.d("look", "changed");
                                    getActivity().finish();

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
                });
            }
        });
        
        return root;
    }

    private void history() {
        Intent intent = new Intent(getContext(), AccessHistory.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void change() {
        Intent intent = new Intent(getContext(), changeUserinfo.class);
        intent.putExtra("name", name);
        intent.putExtra("password", pwd);
        intent.putExtra("imgnum", imgnum);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    private void init(){
        HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

        map.put("id", id);

        Call<UserInfo> call = retrofitInterface.executeUserinfo(map);//로그인리절트 클래스 부르는데저 map넣어서 함

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.code() == 200){
                    UserInfo result = response.body();//응답의 내용. 이와같은 디비구조인게 loginresult.

                    name = result.getName();
                    profile_name.setText(name);

                    pwd = result.getPassword();
                    imgnum = result.getImgnumber();

                    switch(imgnum){
                        case "1":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character1));
                            break;


                        case "2":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character2));
                            break;

                        case "3":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character3));

                            break;

                        case "4":
                            img.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.character4));

                            break;
                        default:
                            Glide.with(getActivity()).load(imgnum).into(img);
                    }

                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }
}