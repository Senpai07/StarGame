package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pools.AlienShipPool;
import ru.geekbrains.sprites.AlienShip;

public class AlienEmitter {

    private static final float GENERATE_INTERVAL = 2f;

    private static final float ALIEN_SMALL_HEIGHT = 0.1f;
    private static final int ALIEN_SMALL_HP = 1;
    private static final float ALIEN_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ALIEN_SMALL_BULLET_VY = -0.3f;
    private static final int ALIEN_SMALL_BULLET_DAMAGE = 1;
    private static final float ALIEN_SMALL_RELOAD_INTERVAL = 3f;

    private static final float ALIEN_MEDIUM_HEIGHT = 0.15f;
    private static final int ALIEN_MEDIUM_HP = 5;
    private static final float ALIEN_MEDIUM_BULLET_HEIGHT = 0.02f;
    private static final float ALIEN_MEDIUM_BULLET_VY = -0.2f;
    private static final int ALIEN_MEDIUM_BULLET_DAMAGE = 5;
    private static final float ALIEN_MEDIUM_RELOAD_INTERVAL = 4f;

    private static final float ALIEN_BIG_HEIGHT = 0.2f;
    private static final int ALIEN_BIG_HP = 10;
    private static final float ALIEN_BIG_BULLET_HEIGHT = 0.04f;
    private static final float ALIEN_BIG_BULLET_VY = -0.1f;
    private static final int ALIEN_BIG_BULLET_DAMAGE = 10;
    private static final float ALIEN_BIG_RELOAD_INTERVAL = 1f;

    private Rect worldBounds;
    private float generateTimer;

    private final TextureRegion[] alienSmallRegions;
    private final TextureRegion[] alienMediumRegions;
    private final TextureRegion[] alienBigRegions;

    private final Vector2 alienSmallV;
    private final Vector2 alienMediumV;
    private final Vector2 alienBigV;

    private final TextureRegion bulletRegion;

    private final AlienShipPool alienShipPool;

    public AlienEmitter(TextureAtlas atlas, AlienShipPool alienShipPool) {
        TextureRegion alien0 = atlas.findRegion("enemy0");
        this.alienSmallRegions = Regions.split(alien0, 1, 2, 2);
        TextureRegion alien1 = atlas.findRegion("enemy1");
        this.alienMediumRegions = Regions.split(alien1, 1, 2, 2);
        TextureRegion alien2 = atlas.findRegion("enemy2");
        this.alienBigRegions = Regions.split(alien2, 1, 2, 2);
        this.alienSmallV = new Vector2(0, -0.2f);
        this.alienMediumV = new Vector2(0, -0.03f);
        this.alienBigV = new Vector2(0, -0.005f);
        this.bulletRegion = atlas.findRegion("bulletEnemy");
        this.alienShipPool = alienShipPool;
    }

    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            AlienShip alienShip = alienShipPool.obtain();
            float type = (float) Math.random();
            if (type < 0.5f) {
                alienShip.set(
                        alienSmallRegions,
                        alienSmallV,
                        bulletRegion,
                        ALIEN_SMALL_BULLET_HEIGHT,
                        ALIEN_SMALL_BULLET_VY,
                        ALIEN_SMALL_BULLET_DAMAGE,
                        ALIEN_SMALL_RELOAD_INTERVAL,
                        ALIEN_SMALL_HP,
                        ALIEN_SMALL_HEIGHT
                );
            } else if (type < 0.8f) {
                alienShip.set(
                        alienMediumRegions,
                        alienMediumV,
                        bulletRegion,
                        ALIEN_MEDIUM_BULLET_HEIGHT,
                        ALIEN_MEDIUM_BULLET_VY,
                        ALIEN_MEDIUM_BULLET_DAMAGE,
                        ALIEN_MEDIUM_RELOAD_INTERVAL,
                        ALIEN_MEDIUM_HP,
                        ALIEN_MEDIUM_HEIGHT
                );
            } else {
                alienShip.set(
                        alienBigRegions,
                        alienBigV,
                        bulletRegion,
                        ALIEN_BIG_BULLET_HEIGHT,
                        ALIEN_BIG_BULLET_VY,
                        ALIEN_BIG_BULLET_DAMAGE,
                        ALIEN_BIG_RELOAD_INTERVAL,
                        ALIEN_BIG_HP,
                        ALIEN_BIG_HEIGHT
                );
            }
            alienShip.pos.x = Rnd.nextFloat(worldBounds.getLeft() + alienShip.getHalfWidth(), worldBounds.getRight() - alienShip.getHalfWidth());
            alienShip.setBottom(worldBounds.getTop());
        }
    }
}
