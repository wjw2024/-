package com.example.the_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
//import android.support.annotation.FloatRange;

import android.os.Handler;

import android.widget.Toast;
import android.os.Message;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    //上证指数、上证涨幅、深圳指数、深圳涨幅、创业板指、创业涨幅
    private TextView txtshzs, txtshzf, txtszzs, txtszzf, txtcyzs, txtcyzf;
    //股票名称、股票代码、股票最新价格、涨幅、涨跌
    private TextView tvname, tvid, tvprice, tvzf, tvzd;
    private EditText edtstockid;//股票代码
    private Button btnadd;//添加
    private ListView lv;//显示添加股票的相关信息
    private String content;//返回网络响应结果字符串
    //用键-值对格式保存添加的股票信息
    List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
    String contnet = "";
    //ListView的自定义适配器
    private MySimpleAdapter adapter = null;
    SimpleAdapter simpleAdapter ;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 3000:
                    String[] from = new String[]{"name","id","price","zf","zd"};
                    int [] to = new int[]{R.id.txtname,R.id.txtid,R.id.txtnewprice,R.id.txtzf,R.id.txtzd};
                    //simpleAdapter = new SimpleAdapter(MainActivity.this,listitem,R.layout.show,from,to);
                    adapter = new MySimpleAdapter(MainActivity.this,listitem,R.layout.show,from,to);
                    lv.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        initView();
        //连接网络获取股票指数信息
        showInfo("sh000001", txtshzs, txtshzf);
        showInfo("sz399001", txtszzs, txtszzf);//(深圳成指、指数/当前报价、涨幅)
        showInfo("sz399006", txtcyzs, txtcyzf);//(创业板指数、指数/当前报价、涨幅)

        btnadd.setOnClickListener(new View.OnClickListener() {
            //点击事件
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        String stockid = edtstockid.getText().toString();
                        String oid = "sh";
                        if (stockid.trim().length() == 0) {
                            Toast.makeText(MainActivity.this, "对不起，你没有输入股票代码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String preid = stockid.trim().substring(0, 1);
                        switch (preid) {
                            case "6":
                                oid = "sh";
                                break;
                            case "0":
                            case "3":
                                oid = "sz";
                                break;
                        }
                        String path = "http://hq.sinajs.cn/list=" + oid + stockid;
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request
                                .Builder()
                                .url(path)
                                .addHeader("Referer", "https://finance.sina.com.cn")
                                .build();
                        Call call = client.newCall(request);
                        try {
                            Response response = call.execute();
                            content = response.body().string();
//                            Log.d("wjw", content);
                            //对获取的数据进行解析
                            String[] temp = content.split(",");
//                            for (String i: temp
//                                 ) {
//                                Log.d("wjw", i);
//                            }
                            //股票名称
                            String[] name = temp[0].split("\"");//即name[1]存放的时股票名称
                            //temp[3]最新报价
                            float zf = (Float.parseFloat(temp[3]) - Float.parseFloat(temp[2])) / Float.parseFloat(temp[2]) * 100;
                            float zd = Float.parseFloat(temp[3]) - Float.parseFloat(temp[2]);
                            Map<String, Object> showitem = new HashMap<>();
                            showitem.put("name", name[1]);
                            showitem.put("id", stockid);
                            showitem.put("price", temp[3]);
                            showitem.put("zf", zf+"");
                            showitem.put("zd", zd+"");

                            if (!listitem.contains(showitem))
                                listitem.add(showitem);
                            handler.sendEmptyMessage(3000);



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });


    }

    void initView() {
        txtcyzf = (TextView) this.findViewById(R.id.txtcyzf);//创业涨幅
        txtcyzs = (TextView) this.findViewById(R.id.txtcyzs);//创业指数
        txtshzf = (TextView) this.findViewById(R.id.txtshzf);//上海涨幅
        txtshzs = (TextView) this.findViewById(R.id.txtshzs);//上海指数
        txtszzs = (TextView) this.findViewById(R.id.txtszzs);//深圳指数
        txtszzf = (TextView) this.findViewById(R.id.txtszzf);//深圳涨幅
        tvid = (TextView) this.findViewById(R.id.txtid);
        tvname = (TextView) this.findViewById(R.id.txtname);
        tvprice = (TextView) this.findViewById(R.id.txtnewprice);
        tvzf = (TextView) this.findViewById(R.id.txtzf);
        tvzd = (TextView) this.findViewById(R.id.txtzd);
        edtstockid = (EditText) this.findViewById(R.id.edtstockId);//输入股票
        btnadd = (Button) this.findViewById(R.id.btnadd);//添加
        lv = (ListView) this.findViewById(R.id.listView);
    }

    void showInfo(String stockid, final TextView tv1, final TextView tv2) {
        String url = "http://hq.sinajs.cn/list=" + stockid;
        OkHttpClient client = new OkHttpClient();
//      Request request = new Request.Builder().url(url).build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Referer", "https://finance.sina.com.cn")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String stockinfo = response.body().string();
                            String[] temp = stockinfo.split(",");
                            float zf = (Float.parseFloat(temp[3]) - Float.parseFloat(temp[2])) / Float.parseFloat(temp[2]) * 100;
                            if (zf < 0) {
                                tv1.setTextColor(0xff00ff00);
                                tv2.setTextColor(0xff00ff00);
                            } else if (zf == 0) {
                                tv1.setTextColor(0xff000000);
                                tv2.setTextColor(0xff000000);
                            } else {
                                tv1.setTextColor(0xffff0000);
                                tv2.setTextColor(0xffff0000);
                            }
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            tv1.setText(temp[3]);
                            tv2.setText(decimalFormat.format(zf));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }
}