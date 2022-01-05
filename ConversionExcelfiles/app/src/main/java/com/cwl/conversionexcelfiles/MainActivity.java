package com.cwl.conversionexcelfiles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.conversionexcelfiles.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button button;
    //文件保存操作路径
    private String SDCardPath = "";
    //python环境初始化使用，APP启动搭建，加快后续调用python的速度
    //获取数据日期
    private Date dateStart=null;
    //设置日期格式
    public SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat formatter_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat formatter_yyyy_M_d = new SimpleDateFormat("yyyy-M-d");

    private Python py;
    private PyObject pyobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPython();
        this.py = Python.getInstance();
        this.pyobj = py.getModule("ConversionXls");

        button=findViewById(R.id.button);
        button.setOnClickListener(getClickEvent());

        SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Cwl/";

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1); //向前走一天的日期
        long milliTime = ca.getTimeInMillis();
        dateStart = ca.getTime();

    }

    private View.OnClickListener getClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button) {
                    Date curDate = dateStart;//ca.getTime();
                    callPythonCode(SDCardPath, curDate);//使用Python脚本读取XLSX文件处理写入到CSV中
                }
            }
        };
    }

    private void callPythonCode(String sdCardPath, Date curDate) {
        String src_Date=formatter_yyyyMMdd.format(curDate);
        String src_File = sdCardPath + src_Date+".xlsx";
        String tag_File = sdCardPath + src_Date+".csv";

        System.out.println("Process "+src_Date+" file to game.csv Start...");
        this.pyobj.callAttr("xlsx_to_csv_pd",src_File,tag_File);
        System.out.println("Process all data to csv complete.");

    }

    private void initPython() {
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
    }
}