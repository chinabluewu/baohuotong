package com.demo.app;

/**
 * Created by wu on 2016/2/13.
 */
public class Data {
    private static String UserName=null;
    private static String UserPw=null;
    private static String sID=null;

    private static String RealName=null;
    private static String SNo=null;
    private static String Mobile=null;
    private static String Addr=null;
    private static String Point=null;




    private static int BillType = 4; //默认为4：鲜活险


    public static String getsID(){return sID;}
    public static void setsID(String sID){Data.sID = sID;}

    public static String getuserName(){return UserName;}
    public static void setUserName(String UserName){Data.UserName = UserName;}

    public static String getUserPw(){return UserPw;}
    public static void setUserPw(String UserPw){Data.UserPw = UserPw;}

    public static String getSNo(){return SNo;}
    public static void setSNo(String SNo){Data.SNo = SNo;}

    public static String getRealName(){return RealName;}
    public static void setRealName(String RealName){Data.RealName = RealName;}

    public static String getMobile(){return Mobile;}
    public static void setMobile(String Mobile){Data.Mobile = Mobile;}

    public static String getAddr(){return Addr;}
    public static void setAddr(String Addr){Data.Addr = Addr;}

    public static String getPoint(){return Point;}
    public static void setPoint(String Point){Data.Point = Point;}

    public static int getBillType(){return BillType;};
    public static void setBillType(int billType){Data.BillType = billType; }


}
