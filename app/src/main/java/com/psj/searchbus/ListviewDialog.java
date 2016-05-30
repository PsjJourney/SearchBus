package com.psj.searchbus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by psj on 2016/5/27.
 */
public class ListviewDialog extends Dialog {

    private Context mContext;
    private List<String> mData;
    private ListView mListview;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;

    public ListviewDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public ListviewDialog(Context context) {
        this(context, R.style.myDialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        //点击dialog外无法关闭dialog
        setCancelable(false);

//        ButterKnife.bind(this);
        this.getWindow().setWindowAnimations(R.style.dialog_anim);
        mListview = (ListView)findViewById(R.id.listview);
        LayoutInflater inflater = getLayoutInflater();
        initData();
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        ListviewAdapter listviewAdapter = new ListviewAdapter(inflater,getContext(),mData);
        mListview.setAdapter(listviewAdapter);
        mListview.setDividerHeight(10);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                next();
            }
        });


    }

    void initData(){
        mData = new ArrayList<String>();
        mData.add("aa");
        mData.add("bb");
        mData.add("bb");
    }

    //    @OnClick(R.id.next)
    void next() {
        this.dismiss();
//        TimeSelectorDialog timeSelectorDialog = new TimeSelectorDialog(getContext());
//        timeSelectorDialog.show();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
//                editText.setText("日期：" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }, year, monthOfYear, dayOfMonth);

        datePickerDialog.show();
    }
}