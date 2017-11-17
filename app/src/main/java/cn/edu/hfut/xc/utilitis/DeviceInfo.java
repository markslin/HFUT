package cn.edu.hfut.xc.utilitis;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by sunmingyin on 2017/11/17.
 */

public class DeviceInfo {
    private static final String DEVICE_INFO="device_info";
    private static final String DEVICE_ID="device_id";
    public synchronized static String getDeviceId(Context context){
        SharedPreferences sp=context.getSharedPreferences(DEVICE_INFO,Context.MODE_PRIVATE);
        String deviceId=sp.getString(DEVICE_ID,null);
        if (TextUtils.isEmpty(deviceId)){
            SharedPreferences.Editor editor=sp.edit();
            deviceId=UUID.randomUUID().toString();
            editor.putString(DEVICE_ID,deviceId);
            editor.commit();
        }
        return deviceId;
    }
}
