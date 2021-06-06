package com.app.ricktech.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.app.ricktech.language.Language;

import io.paperdb.Paper;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        String lang = Paper.book().read("lang","ar");
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

