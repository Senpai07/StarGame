package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {

    private static final float SPEED = 0.01f;
    private Vector2 targetVector;
    private Vector2 distanceVector;
    private Vector2 directionVector;

    public Logo(Texture texture) {
        super(new TextureRegion(texture));
        targetVector = new Vector2();
        directionVector = new Vector2();
        distanceVector = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
    }

    @Override
    public void update(float delta) {
        distanceVector.set(targetVector);
        if (distanceVector.sub(this.pos).len() > SPEED) {
            this.pos.add(directionVector);
        } else {
            this.pos.set(targetVector);
            directionVector.setZero();
        }
        super.update(delta);
    }

    public void startMove(Vector2 touch) {
        targetVector.set(touch);
        distanceVector.set(targetVector);
        distanceVector.sub(this.pos);
        directionVector.set(distanceVector);
        directionVector.setLength(SPEED);
    }

}
