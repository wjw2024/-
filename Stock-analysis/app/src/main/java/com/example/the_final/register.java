package com.example.the_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
public class register extends AppCompatActivity {

    EditText name;  //创建账号
    EditText passwd;  //创建密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);   //获取输入的账号
        passwd = findViewById(R.id.passwd);
    }

    public void Login(View v) {
        //这是能够登录的账号密码
        String Usename = "admin";
        String Upwd = "12345";

        //创建两个String类，储存从输入文本框获取到的内容
        String user = name.getText().toString().trim();
        String pwd = passwd.getText().toString().trim();

        //进行判断，如果两个内容都相等，就显现第一条语句，反之，第二条。
        if (user.equals(Usename) & pwd.equals(Upwd)) {
//            Toast.makeText(this, "欢迎你，新世界管理者", Toast.LENGTH_SHORT).show();
            startreister();
        } else {
            Toast.makeText(this, "身份验证错误，禁止访问", Toast.LENGTH_SHORT).show();
        }
    }

    public  void startreister()
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
