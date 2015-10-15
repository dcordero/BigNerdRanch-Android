package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetsManager;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context) {
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
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
            return;
        }

        for (String soundName : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + soundName;
                Sound sound = new Sound(assetPath);
                loadSound(sound);
                mSounds.add(sound);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSound(Sound sound) throws IOException {
        AssetFileDescriptor fileDescriptor = mAssetsManager.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(fileDescriptor, 1);
        sound.setSoundId(soundId);
    }

    public void playSound(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) {
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
