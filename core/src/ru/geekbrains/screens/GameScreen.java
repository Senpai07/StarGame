package ru.geekbrains.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.AlienShipPool;
import ru.geekbrains.pools.BulletPool;
import ru.geekbrains.pools.ExplosionPool;
import ru.geekbrains.sprites.AlienShip;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Bullet;
import ru.geekbrains.sprites.ButtonNewGame;
import ru.geekbrains.sprites.GameOver;
import ru.geekbrains.sprites.MainShip;
import ru.geekbrains.sprites.Star;
import ru.geekbrains.utils.AlienEmitter;

public class GameScreen extends BaseScreen {

    private static final float FONT_SIZE = 0.02f;
    private static final float TEXT_MARGIN = 0.01f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, GAME_OVER}

    private static final int COUNT_STARS = 64;
    private Texture background;
    private Background backgroundSprite;
    private Star[] stars;
    private MainShip mainShip;
    private TextureAtlas mainAtlas;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private AlienShipPool alienShipPool;
    private Music music;
    private AlienEmitter alienEmitter;
    private State state;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;
    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

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
        mainShip = new MainShip(mainAtlas, bulletPool, explosionPool);
        alienEmitter = new AlienEmitter(mainAtlas, alienShipPool);
        gameOver = new GameOver(mainAtlas);
        buttonNewGame = new ButtonNewGame(mainAtlas, this);
        font = new Font("font/font.fnt", "font/font.png");
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        state = State.PLAYING;
    }

    @Override
    public void resize(Rect worldBounds) {
        backgroundSprite.resize(worldBounds);
        mainShip.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        alienEmitter.resize(worldBounds);
        gameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
        font.setSize(FONT_SIZE);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollision();
        free();
        draw();
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            alienShipPool.updateActiveSprites(delta);
            alienEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.update(delta);
        }

    }

    private void checkCollision() {
        if (state != State.PLAYING) {
            return;
        }
        List<AlienShip> alienList = alienShipPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (AlienShip alienShip : alienList) {
            float minDist = alienShip.getHalfWidth() + mainShip.getHalfWidth();
            if (mainShip.pos.dst(alienShip.pos) < minDist) {
                alienShip.destroy();
                mainShip.damage(alienShip.getBulletDamage());
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue;
                }
                if (alienShip.isBulletCollision(bullet)) {
                    alienShip.damage(bullet.getDamage());
                    bullet.destroy();
                    if (alienShip.isDestroyed()) {
                        frags += 1;
                    }
                }
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == mainShip || bullet.isDestroyed()) {
                continue;
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
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
        if (state == State.PLAYING) {
            bulletPool.drawActiveSprites(batch);
            mainShip.draw(batch);
            alienShipPool.drawActiveSprites(batch);
        } else if (state == State.GAME_OVER) {
            gameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags),
                worldBounds.getLeft() + TEXT_MARGIN,
                worldBounds.getTop() - TEXT_MARGIN);
        font.draw(batch, sbHp.append(HP).append(mainShip.getHp()),
                worldBounds.pos.x,
                worldBounds.getTop() - TEXT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(alienEmitter.getLevel()),
                worldBounds.getRight() - TEXT_MARGIN,
                worldBounds.getTop() - TEXT_MARGIN, Align.right);
        font.draw(batch, String.valueOf(mainShip.getHp()), mainShip.pos.x, mainShip.pos.y, Align.center);
    }

    @Override
    public void dispose() {
        background.dispose();
        mainAtlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        alienShipPool.dispose();
        music.dispose();
        mainShip.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDragged(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    public void startNewGame() {
        frags = 0;
        mainShip.startNewGame();
        bulletPool.freeAllActiveObject();
        alienShipPool.freeAllActiveObject();
        explosionPool.freeAllActiveObject();
        state = State.PLAYING;
    }
}
