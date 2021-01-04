package com.nucarf.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.amap.api.maps.model.LatLng;
import com.nucarf.base.ui.WebActivity;

/**
 * Created by wang on 2017/4/5.
 */

public class MapRoutUtil {
    public static MapRoutUtil instance;

    public static MapRoutUtil getInstance() {
        if (null == instance) {
            instance = new MapRoutUtil();
        }

        return instance;
    }
     //腾讯地图 只有web
    public void selectTencent(Context pContext, LatLng pMyLoaction, String pFrom, LatLng pDestLoaction, String pDestTo) {
        if (AppUtil.getInstance().isInstallByread(pContext,"com.tencent.map")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse("qqmap://map/routeplan?type=drive&from=我的位置&fromcoord="+ pMyLoaction.latitude + "," + pMyLoaction.longitude+"&to="+pDestTo+"&tocoord=" + pDestLoaction.latitude + "," + pDestLoaction.longitude+"&refer=com.nucarf,member"));
            pContext.startActivity(intent);
        }else {
            //http://lbs.qq.com/uri_v1/guide-route.html
            String url = "http://apis.map.qq.com/uri/v1/routeplan?type=drive&from=" + pFrom + "&fromcoord="
                    + pMyLoaction.latitude + "," + pMyLoaction.longitude
                    + "&to=" + pDestTo + "&tocoord=" + pDestLoaction.latitude + "," + pDestLoaction.longitude + "&policy=0&referer=" + "member";
            WebActivity.lauch(pContext, "网页版地图导航", url);

        }
    }


    public void selectGaode(Context pContext, LatLng pMyLoaction, String pFrom, LatLng pDestLoaction, String pDestTo) {
        //http://lbs.amap.com/api/amap-mobile/guide/android/route
        if (AppUtil.getInstance().isInstallByread(pContext,"com.autonavi.minimap")) {
            try {

                Intent intentOther = new Intent("android.intent.action.VIEW",
                        Uri.parse("androidamap://route?sourceApplication=starshow&slat="+pMyLoaction.latitude+"&slon="+pMyLoaction.longitude+"&sname="+pFrom+"&dlat="+pDestLoaction.latitude+"&dlon="+pDestLoaction.longitude+"&dname="+pDestTo+"&dev=0&t=0"));
                intentOther.setPackage("com.autonavi.minimap");
                intentOther.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pContext.startActivity(intentOther);
            } catch (Exception e) {
                String url = "http://m.amap.com/?from="
                        + pMyLoaction.latitude + "," + pMyLoaction.longitude
                        + "(from)&to=" + pDestLoaction.latitude + "," + pDestLoaction.longitude + "(to)&type=0&opt=1&dev=0";
                WebActivity.lauch(pContext,  "网页版地图导航",url);
            }

        } else {
            String url = "http://m.amap.com/?from="
                    + pMyLoaction.latitude + "," + pMyLoaction.longitude
                    + "(from)&to=" + pDestLoaction.latitude + "," + pDestLoaction.longitude + "(to)&type=0&opt=1&dev=0";
            WebActivity.lauch(pContext,"网页版地图导航", url);

        }

    }


    public void selectBaidu(Context pContext, LatLng pMyLoaction, String pFrom, LatLng pDestLoaction, String pDestTo) {
        try {
            //调起App
            double[] txNowLatLng = gaoDeToBaidu(pMyLoaction.latitude, pMyLoaction.longitude);
            double[] txDesLatLng = gaoDeToBaidu(pDestLoaction.latitude, pDestLoaction.longitude);

            if (AppUtil.getInstance().isInstallByread(pContext,"com.baidu.BaiduMap")) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/direction?origin=name:"+pFrom+"|latlng:"+txNowLatLng[0]+","+txNowLatLng[1]+"&destination=name:"+pDestTo+"|latlng:"+txDesLatLng[0]+","+txDesLatLng[1]+"&mode=driving&sy=3&index=0&target=1"));
                pContext.startActivity(intent);
            } else {
//                Toast.makeText(mActivity, "如果您没有安装百度地图APP，" +
//                        "可能无法正常使用导航，建议选择其他地图", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }
}
