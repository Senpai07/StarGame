package ru.geekbrains.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.BulletPool;
import ru.geekbrains.pools.ExplosionPool;

public class AlienShip extends Ship {

    private static final float V_Y = -0.3f;

    public AlienShip(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound sound) {
        super(bulletPool, explosionPool, worldBounds, sound);
    }

    public void set(TextureRegion[] regions,
                    Vector2 beginSpeed,
                    TextureRegion bulletRegion,
                    float bulletHeight,
                    float bulletVY,
                    int bulletDamage,
                    float reloadInterval,
                    int hp,
                    float height) {
        this.regions = regions;
        this.moveVector.set(beginSpeed);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletSpeed.set(0, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        speedVector.set(0, V_Y);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(speedVector, delta);
        if (getTop() < worldBounds.getTop()) {
            speedVector.set(moveVector);
            bulletPosition.set(pos.x, pos.y - getHalfHeight());
            autoShoot(delta);
        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }
}
