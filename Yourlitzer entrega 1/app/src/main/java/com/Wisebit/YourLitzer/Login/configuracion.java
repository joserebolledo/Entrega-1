package com.Wisebit.YourLitzer.Login;

/**
 * Created by Lleby on 30-09-2015.
 */
import android.content.Context;
import android.content.SharedPreferences;


public class configuracion {
    private static final String SHARED_PREFS_FILE = "HMPrefs";
    private static final String KEY_NICK = "nick";
    private static final String KEY_INTRO = "intro";

    private static Context mContext;

    public configuracion(Context context){
        mContext = context;
    }

    private static SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getUserNick(){
        return getSettings().getString(KEY_NICK, null);
    }

    public void setUserNick(String nick){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_NICK, nick );
        editor.commit();
    }

    public String getUserIntro(){
        return getSettings().getString(KEY_INTRO, null);
    }

    public void setUserIntro(String intro){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_INTRO, intro );
        editor.commit();
    }

    public static void borrar(){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(KEY_NICK,null);
        editor.commit();

    }

}