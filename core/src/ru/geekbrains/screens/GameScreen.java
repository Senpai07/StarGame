package ru.geekbrains.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.AlienShipPool;
import ru.geekbrains.pools.BulletPool;
import ru.geekbrains.pools.ExplosionPool;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.utils.AlienEmitter;

public class GameScreen extends BaseScreen {
    private static final int COUNT_STARS = 64;
    private Texture background;
    private Background backgroundSprite;
    private Star[] stars;
    private MainShip mainShipSprite;
    private TextureAtlas mainAtlas;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private AlienShipPool alienShipPool;
    private Music music;
    private AlienEmitter alienEmitter;

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
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(mainAtlas);
        alienShipPool = new AlienShipPool(bulletPool, explosionPool, worldBounds);
        mainShipSprite = new MainShip(mainAtlas, bulletPool,explosionPool);
        alienEmitter = new AlienEmitter(mainAtlas, alienShipPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void resize(Rect worldBounds) {
        backgroundSprite.resize(worldBounds);
        mainShipSprite.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        alienEmitter.resize(worldBounds);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        free();
        draw();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        explosionPool.updateActiveSprites(delta);
        alienShipPool.updateActiveSprites(delta);
        mainShipSprite.update(delta);
        alienEmitter.generate(delta);
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        alienShipPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        backgroundSprite.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        mainShipSprite.draw(batch);
        alienShipPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }


    @Override
    public void dispose() {
        background.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        alienShipPool.dispose();
        music.dispose();
        mainShipSprite.dispose();
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
        mainShipSprite.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShipSprite.keyUp(keycode);
        return false;
    }
}
