package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";

    private AssetManager mAssetsManager;

    public BeatBox(Context context) {
        mAssetsManager = context.getAssets();
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetsManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        }
        catch (IOException ioe) {
            Log.e(TAG, "Couldn't list assets", ioe);
        }
    }
}
