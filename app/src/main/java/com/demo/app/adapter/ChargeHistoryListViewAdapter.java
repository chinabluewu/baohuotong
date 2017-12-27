package com.demo.app.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.app.R;
import com.demo.app.bean.ChargeHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/3/1.
 */
public class ChargeHistoryListViewAdapter extends BaseAdapter {

    LayoutInflater inflater;
    private ViewHolder holder;
    List<ChargeHistory> list = new ArrayList<ChargeHistory>();

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView HistoryNo; //充值编号
        TextView ChargeVaule; //充值金额
        TextView ChargeTime; //充值时间


    }

    public ChargeHistoryListViewAdapter(Activity activity, List<ChargeHistory> listViewList) {
        inflater = activity.getLayoutInflater();
        list = listViewList;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.charge_listview_item, null);


            holder.HistoryNo = (TextView) convertView
                    .findViewById(R.id.tv_chargeListView_historyNo);

            holder.ChargeVaule = (TextView) convertView
                    .findViewById(R.id.tv_chargeListView_chargeVaule);  //add by wu

            holder.ChargeTime = (TextView) convertView
                    .findViewById(R.id.tv_chargeListView_chargeTime);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.HistoryNo.setText(list.get(position).getHistoryNo());
        holder.ChargeVaule.setText(list.get(position).getChargeVaule()); //wu add

        holder.ChargeTime.setText(list.get(position).getChargeTime());


        return convertView;
    }



    @Override
    public int getCount() {
        return list.size();
    }
}
