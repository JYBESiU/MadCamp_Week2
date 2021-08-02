package com.example.thekaist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thekaist.ui.setting.SettingFragment;

import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class changeUserinfo extends AppCompatActivity {

    private TextView showname, showpwd, showimg;
    private EditText chgname, chgpwd, chgimg;
    private ImageView img1, img2, img3, img4;
    private Button save, cancel;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.BASE_URL;

    public String id,name,pwd,imgnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_userinfo);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        pwd = intent.getStringExtra("password");
        imgnum = intent.getStringExtra("imgnum");


        showname = findViewById(R.id.UserName);
        showpwd = findViewById(R.id.UserPwd);
        showimg = findViewById(R.id.UserPic);

        chgname = findViewById(R.id.changeName);
        chgname.setText(name);

        chgpwd = findViewById(R.id.changePwd);
        chgpwd.setText(pwd);

        chgimg = findViewById(R.id.changeImg);
        chgimg.setText(imgnum);


        img1 = findViewById(R.id.Img1);
        img2 = findViewById(R.id.Img2);
        img3 = findViewById(R.id.Img3);
        img4 = findViewById(R.id.Img4);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chgimg.setText("1");
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chgimg.setText("2");
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chgimg.setText("3");
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chgimg.setText("4");
            }
        });

        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void save() {
        HashMap<String, String> map = new HashMap<>();//key도 스트링, 값도 스트링

        map.put("id", id);
        map.put("name", chgname.getText().toString());
        map.put("password", chgpwd.getText().toString());
        map.put("imgnumber", chgimg.getText().toString());

        Call<Void> call = retrofitInterface.executeChange(map);//로그인리절트 클래스 부르는데저 map넣어서 함

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Log.d("look", "changed");
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }

        });

    }


}