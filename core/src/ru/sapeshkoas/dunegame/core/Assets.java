package ru.sapeshkoas.dunegame.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    private static final Assets ourInstance = new Assets();
    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

    private Assets() {
       assetManager = new AssetManager();
    }

    public static Assets getOurInstance() {
        return ourInstance;
    }

    public void loadAssets() {
        assetManager.load("game.pack", TextureAtlas.class);
        assetManager.finishLoading();
        textureAtlas = assetManager.get("game.pack");
    }
}
