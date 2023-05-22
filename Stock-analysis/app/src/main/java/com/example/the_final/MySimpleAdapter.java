package com.example.the_final;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by angular on 2018/6/24.
 */

public class MySimpleAdapter extends SimpleAdapter {
    List<? extends Map<String, ?>> mdata;
    Context context;
    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mdata =data;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.show, null);
        }
        //此处的TextView是R.layout.show_layout里面的，也就是需要重新设置的字体颜色的
        TextView textView1 = (TextView) convertView.findViewById(R.id.txtname);
        TextView textView2 = (TextView) convertView.findViewById(R.id.txtid);
        TextView textView3 = (TextView) convertView.findViewById(R.id.txtnewprice);
        TextView textView4 = (TextView) convertView.findViewById(R.id.txtzf);
        TextView textView5 = (TextView) convertView.findViewById(R.id.txtzd);
        String ss=(String)mdata.get(position).get("zd");//取出涨跌值
        if(Float.parseFloat(ss)<0){//涨跌值<0，则绿色
            textView1.setTextColor(Color.rgb(00, 255, 00));
            textView2.setTextColor(Color.rgb(00, 255, 00));
            textView3.setTextColor(Color.rgb(00, 255, 00));
            textView4.setTextColor(Color.rgb(00, 255, 00));
            textView5.setTextColor(Color.rgb(00, 255, 00));
            // textView2、textView3、textView4 、textView5设置一样，此处略
        }else if(Float.parseFloat(ss)==0) {//黑色
            textView1.setTextColor(Color.rgb(0, 0, 0));
            textView2.setTextColor(Color.rgb(0, 0, 0));
            textView3.setTextColor(Color.rgb(0, 0, 0));
            textView4.setTextColor(Color.rgb(0, 0, 0));
            textView5.setTextColor(Color.rgb(0, 0, 0));
            //textView2、textView3、textView4 、textView5设置一样，此处略
        }else  {//红色
            textView1.setTextColor(Color.rgb(255, 0, 0));
            textView2.setTextColor(Color.rgb(255, 0, 0));
            textView3.setTextColor(Color.rgb(255, 0, 0));
            textView4.setTextColor(Color.rgb(255, 0, 0));
            textView5.setTextColor(Color.rgb(255, 0, 0));
            //extView2、textView3、textView4 、textView5设置一样，此处略
        }
        return super.getView(position, convertView, parent);
    }

}
