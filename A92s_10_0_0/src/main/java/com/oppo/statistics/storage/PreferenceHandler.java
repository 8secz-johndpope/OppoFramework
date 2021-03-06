package com.oppo.statistics.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.oppo.statistics.util.AccountUtil;
import com.oppo.statistics.util.ApkInfoUtil;

public class PreferenceHandler {
    public static final String ACTIVITY_END_TIME = "activity.end.time";
    public static final String ACTIVITY_START_TIME = "activity.start.time";
    public static final String CURRENT_ACTIVITY = "current.activity";
    public static final String EVENT_START = "event.start";
    public static final String KVEVENT_START = "kv.start";
    public static final String PAGEVISIT_DURATION = "pagevisit.duration";
    public static final String PAGEVISIT_ROUTES = "pagevisit.routes";
    public static final String PAGEVISIT_START_TIME = "pagevisit.start.time";
    public static final String SESSION_TIMEOUT = "session.timeout";
    public static final int SESSION_TIMEOUT_DEFAULT = 30;
    public static final String SSOID = "ssoid";

    public static SharedPreferences getSettingPref(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences("nearme_setting_" + ApkInfoUtil.getPackageName(context), 0);
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences preferences;
        if (context != null && !TextUtils.isEmpty(key) && (preferences = getSettingPref(context)) != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(key, value);
            edit.apply();
        }
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences preferences;
        if (context == null || TextUtils.isEmpty(key) || (preferences = getSettingPref(context)) == null) {
            return defaultValue;
        }
        return preferences.getLong(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences;
        if (context != null && !TextUtils.isEmpty(key) && (preferences = getSettingPref(context)) != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(key, value);
            edit.apply();
        }
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences;
        if (context == null || TextUtils.isEmpty(key) || (preferences = getSettingPref(context)) == null) {
            return defaultValue;
        }
        return preferences.getString(key, defaultValue);
    }

    public static SharedPreferences getFunctionPref(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences("nearme_func_" + ApkInfoUtil.getPackageName(context), 0);
    }

    public static String getPageVisitRoutes(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getString(PAGEVISIT_ROUTES, "");
        }
        return "";
    }

    public static void setPageVisitRoutes(Context context, String routes) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(PAGEVISIT_ROUTES, routes);
            edit.commit();
        }
    }

    public static long getActivityStartTime(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getLong(ACTIVITY_START_TIME, -1);
        }
        return -1;
    }

    public static void setActivityStartTime(Context context, long time) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(ACTIVITY_START_TIME, time);
            edit.commit();
        }
    }

    public static long getActivityEndTime(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getLong(ACTIVITY_END_TIME, -1);
        }
        return -1;
    }

    public static void setActivityEndTime(Context context, long time) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(ACTIVITY_END_TIME, time);
            edit.commit();
        }
    }

    public static String getCurrentActivity(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getString(CURRENT_ACTIVITY, "");
        }
        return "";
    }

    public static void setCurrentActivity(Context context, String activity) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(CURRENT_ACTIVITY, activity);
            edit.commit();
        }
    }

    public static long getEventStart(Context context, String eventID, String eventTag) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences == null) {
            return 0;
        }
        return preferences.getLong(EVENT_START + eventID + "_" + eventTag, 0);
    }

    public static void setEventStart(Context context, String eventID, String eventTag, long eventTime) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putLong(EVENT_START + eventID + "_" + eventTag, eventTime);
            edit.commit();
        }
    }

    public static String getKVEventStart(Context context, String eventID, String flag) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences == null) {
            return "";
        }
        return preferences.getString(KVEVENT_START + eventID + "_" + flag, "");
    }

    public static void setKVEventStart(Context context, String eventID, String eventInfo, String flag) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(KVEVENT_START + eventID + "_" + flag, eventInfo);
            edit.commit();
        }
    }

    public static int getPageVisitDuration(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getInt(PAGEVISIT_DURATION, 0);
        }
        return -1;
    }

    public static void setPageVisitDuration(Context context, int duration) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(PAGEVISIT_DURATION, duration);
            edit.commit();
        }
    }

    public static void setPageVisitStartTime(Context context, long millis) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(PAGEVISIT_START_TIME, millis);
            editor.commit();
        }
    }

    public static long getPageVisitStartTime(Context context) {
        SharedPreferences preferences = getFunctionPref(context);
        if (preferences != null) {
            return preferences.getLong(PAGEVISIT_START_TIME, 0);
        }
        return 0;
    }

    public static String getSsoID(Context context) {
        SharedPreferences preferences = getSettingPref(context);
        if (preferences != null) {
            return preferences.getString(SSOID, AccountUtil.SSOID_DEFAULT);
        }
        return AccountUtil.SSOID_DEFAULT;
    }

    public static void setSsoID(Context context, String ssoID) {
        SharedPreferences preferences = getSettingPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(SSOID, ssoID);
            edit.commit();
        }
    }

    public static void setSsoID(Context context) {
        SharedPreferences preferences = getSettingPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(SSOID, AccountUtil.SSOID_DEFAULT);
            edit.commit();
        }
    }

    public static void setSessionTimeout(Context context, int timeout) {
        SharedPreferences preferences = getSettingPref(context);
        if (preferences != null) {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putInt(SESSION_TIMEOUT, timeout);
            edit.commit();
        }
    }

    public static int getSessionTimeout(Context context) {
        SharedPreferences preferences = getSettingPref(context);
        if (preferences != null) {
            return preferences.getInt(SESSION_TIMEOUT, 30);
        }
        return 30;
    }
}
