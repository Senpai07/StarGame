package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.Regions;

public class AlienShip extends Sprite {

    private Rect worldBounds;
    private Vector2 speedVector;
    private int armor;

    public AlienShip() {
        regions = new TextureRegion[1];
        speedVector = new Vector2();
    }

    public void set(TextureRegion region,
                    Vector2 beginPos,
                    Vector2 beginSpeed,
                    float height,
                    Rect worldBounds,
                    int armor) {
        this.regions[0] = Regions.split(region,1,2,2)[0];
        this.pos.set(beginPos);
        this.speedVector.set(beginSpeed);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.armor = armor;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(speedVector, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getArmor() {
        return armor;
    }
}
