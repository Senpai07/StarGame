package ru.geekbrains.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;

public class GameScreen extends BaseScreen {
    private static final int COUNT_STARS = 64;
    private Texture background;
    private Background backgroundSprite;
    private MainShip mainShipSprite;
    private TextureAtlas mainAtlas;
    private Star[] stars;

    @Override
    public void show() {
        super.show();

        background = new Texture("StarsSky_v1.jpg");
        backgroundSprite = new Background(background);

        mainAtlas = new TextureAtlas("textures/mainAtlas.tpack");

        stars = new Star[COUNT_STARS];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(mainAtlas);
        }

        mainShipSprite = new MainShip(mainAtlas);
    }

    @Override
    public void resize(Rect worldBounds) {
        backgroundSprite.resize(worldBounds);
        mainShipSprite.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        mainShipSprite.update(delta);
        for (Star star : stars) {
            star.update(delta);
        }
    }

    private void draw() {
        batch.begin();
        backgroundSprite.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        mainShipSprite.draw(batch);
        batch.end();
    }


    @Override
    public void dispose() {
        background.dispose();
        mainAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShipSprite.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        mainShipSprite.touchUp(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        mainShipSprite.touchDragged(touch, pointer);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
}
