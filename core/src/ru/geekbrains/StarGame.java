package ru.geekbrains;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Sprite backgroundSprite;

    @Override
    public void create() {
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            Gdx.graphics.setWindowedMode(1080, 1920);
            background = new Texture("background_v.jpg");
        } else {
            Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            background = new Texture("background.jpg");
        }
        batch = new SpriteBatch();
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
