package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Bullet extends Sprite {

    private Rect worldBounds;
    private Vector2 speedVector;
    private int damage;
    private Sprite owner;

    public Bullet() {
        regions = new TextureRegion[1];
        speedVector = new Vector2();
    }

    public void set(Sprite owner,
                    TextureRegion region,
                    Vector2 beginPos,
                    Vector2 beginSpeed,
                    float height,
                    Rect worldBounds,
                    int damage) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(beginPos);
        this.speedVector.set(beginSpeed);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speedVector, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Sprite getOwner() {
        return owner;
    }
}
