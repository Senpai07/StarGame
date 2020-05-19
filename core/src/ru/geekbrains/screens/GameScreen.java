package ru.geekbrains.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.AlienShipPool;
import ru.geekbrains.pools.BulletPool;
import ru.geekbrains.sprites.AlienShip;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;

public class GameScreen extends BaseScreen {
    private static final int COUNT_STARS = 64;
    private Texture background;
    private Background backgroundSprite;
    private Star[] stars;
    private MainShip mainShipSprite;
    private TextureAtlas mainAtlas;
    private BulletPool bulletPool;
    private AlienShipPool alienShipPool;
    private Sound shootSound;
    private Music music;
    private TextureRegion alienShipRegion;
    private float animateTimer;

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
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        mainShipSprite = new MainShip(mainAtlas, bulletPool, shootSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        alienShipPool = new AlienShipPool();
        alienShipRegion = mainAtlas.findRegion("enemy2");

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
        animateTimer += delta;
        if (animateTimer >= 7f) {
            AlienShip alienShip = alienShipPool.obtain();
            alienShip.set(alienShipRegion, new Vector2(0f, getWorldBounds().getTop()), new Vector2(0f, -0.2f), 0.15f, getWorldBounds(), 10);
            animateTimer = 0f;
        }
        bulletPool.updateActiveSprites(delta);
        mainShipSprite.update(delta);
        alienShipPool.updateActiveSprites(delta);
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        alienShipPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        backgroundSprite.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        alienShipPool.drawActiveSprites(batch);
        mainShipSprite.draw(batch);
        batch.end();
    }


    @Override
    public void dispose() {
        background.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        alienShipPool.dispose();
        shootSound.dispose();
        music.dispose();
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
