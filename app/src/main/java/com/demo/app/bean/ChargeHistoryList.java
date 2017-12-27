package com.demo.app.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/3/1.
 */
public class ChargeHistoryList extends Base {
    private int Count = 0;

    private List<ChargeHistory> chargeHistoryList = new ArrayList<ChargeHistory>();

   public int getCount(){return Count;}

    public List<ChargeHistory> getChargeHistoryList(){return chargeHistoryList;}

    public static ChargeHistoryList parse(JSONArray obj) throws JSONException {

        ChargeHistoryList chargeHistoryList= new ChargeHistoryList();

        if (null != obj) {
            chargeHistoryList.Count = obj.length();
            for (int i = 0; i < obj.length(); i++) {
                JSONObject newsJson = obj.getJSONObject(i);
               // News news = new News();
                ChargeHistory chargeHistory = new ChargeHistory();

                chargeHistory.setHistoryNo("编号: " + newsJson.getString("RId"));  //编号

                chargeHistory.setChargeVaule("充值金额: " + newsJson.getString("RPoint")+"元"); //充值金额

                chargeHistory.setChargeTime("充值时间: " + newsJson.getString("AddTime"));  //时间

                //newslist.newslist.add(news);
                chargeHistoryList.chargeHistoryList.add(chargeHistory);
            }
        }
        return chargeHistoryList;

    }


}
