package com.psj.searchbus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by psj on 2016/5/27.
 */
public class ListviewAdapter extends BaseAdapter {
    Context context = null;
    LayoutInflater mInflater;
    List<String> mData;

    public ListviewAdapter(LayoutInflater inflater, Context mContext) {
        mInflater = inflater;
        context = mContext;
    }

    public void setData(List<String> data) {
        mData = data;
    }

    @Override
    public int getCount() {
//        return mData.size();
        return (mData == null) ? 0 : (mData.size());
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = mInflater.inflate(R.layout.listview_item_layout, null);
        TextView textView = (TextView) view1.findViewById(R.id.text_content_item);
        textView.setText(mData.get(i));
        return view1;
    }
}
