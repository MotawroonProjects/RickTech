package com.app.ricktech.share;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.app.ricktech.language.Language;
import com.app.ricktech.preferences.Preferences;

import io.paperdb.Paper;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"de"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                updateFont();
                if(Preferences.getInstance().getDarkMode(getApplicationContext()).equals("yes")){
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                }
                else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);}
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });

    }

    private void updateFont() {
        Paper.init(this);
        String lang = Paper.book().read("lang", "de");

        if (lang.equals("ar")){
            TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/font.ttf");
            TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/font.ttf");
            TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/font.ttf");
            TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/font.ttf");

        }else {
            TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/en_font.ttf");
            TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/en_font.ttf");
            TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/en_font.ttf");
            TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/en_font.ttf");

        }
    }
}

