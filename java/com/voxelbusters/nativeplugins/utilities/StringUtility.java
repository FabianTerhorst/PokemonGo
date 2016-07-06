package com.voxelbusters.nativeplugins.utilities;

import android.util.Base64;
import com.voxelbusters.nativeplugins.defines.CommonDefines;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import spacemadness.com.lunarconsole.BuildConfig;

public class StringUtility {
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.equals(BuildConfig.FLAVOR) || str.equals("null")) {
            return true;
        }
        return false;
    }

    public static String[] convertJsonStringToStringArray(String jsonString) {
        String[] stringArray = null;
        if (isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            int size = jsonArray.length();
            stringArray = new String[size];
            for (int i = 0; i < size; i++) {
                stringArray[i] = new String(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Debug.error(CommonDefines.STRING_UTILS_TAG, "Error in parsing jsonString " + jsonString);
        }
        return stringArray;
    }

    public static boolean contains(String source, String[] list) {
        for (String element : list) {
            if (source.contains(element)) {
                return true;
            }
        }
        return false;
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    }

    public static String getBase64DecodedString(String base64String) {
        byte[] dataBytes = Base64.decode(base64String, 0);
        String text = BuildConfig.FLAVOR;
        try {
            return new String(dataBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
    }

    public static String getCurrencySymbolFromCode(String currencyCode) {
        String symbol = BuildConfig.FLAVOR;
        try {
            symbol = Currency.getInstance(currencyCode).getSymbol();
        } catch (Exception e) {
            Debug.log(CommonDefines.STRING_UTILS_TAG, "Error in converting currency code : " + currencyCode);
        }
        return symbol;
    }

    public static String getFileNameWithoutExtension(String fileWithExt) {
        String fileWithoutExt = fileWithExt;
        int dotIndex = fileWithExt.lastIndexOf(46);
        if (dotIndex >= 0) {
            return fileWithExt.substring(0, dotIndex);
        }
        return fileWithoutExt;
    }
}
