package ru.geekbrains.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.SpaceShip;
import ru.geekbrains.sprites.Star;

public class GameScreen extends BaseScreen {
    private static final int COUNT_STARS = 256;
    private Texture background;
    private Background backgroundSprite;
    private SpaceShip spaceShipSprite;
    private TextureAtlas mainAtlas;
    private Star[] stars;

    @Override
    public void show() {
        super.show();

        background = new Texture("StarsSky_v1.jpg");
        backgroundSprite = new Background(background);

        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));

        stars = new Star[COUNT_STARS];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(mainAtlas);
        }
        TextureRegion spaceShipTextureRegion = mainAtlas.findRegion("main_ship").split(
                mainAtlas.findRegion("main_ship").originalWidth / 2,
                mainAtlas.findRegion("main_ship").originalHeight)[0][1];

        spaceShipSprite = new SpaceShip(spaceShipTextureRegion);
        spaceShipSprite.pos.set(0, 0);
    }

    @Override
    public void resize(Rect worldBounds) {
        backgroundSprite.resize(worldBounds);
        spaceShipSprite.resize(worldBounds);
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
        spaceShipSprite.update(delta);
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
        spaceShipSprite.draw(batch);
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
        spaceShipSprite.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        spaceShipSprite.touchDragged(touch, pointer);
        return false;
    }
}
