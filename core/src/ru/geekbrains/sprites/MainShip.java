package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class MainShip extends Sprite {

    private static final float SPEED = 0.01f;
    private static final float MARGIN = 0.05f;
    private static final float SIZE_SHIP = 0.15f;
    private Vector2 targetVector;
    private Vector2 distanceVector;
    private Vector2 speedVector;

    public MainShip(TextureAtlas textureAtlas) {
        super(textureAtlas.findRegion("main_ship"), 1, 2, 2);
        targetVector = new Vector2();
        speedVector = new Vector2();
        distanceVector = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(SIZE_SHIP);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        distanceVector.set(targetVector);
        if (distanceVector.sub(pos).len() > SPEED) {
            pos.add(speedVector);
        } else {
            pos.set(targetVector);
            speedVector.setZero();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        startMove(touch);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        startMove(touch);
        return false;
    }

    private void startMove(Vector2 touch) {
        this.targetVector.set(touch);
        speedVector.set(touch.sub(pos)).setLength(SPEED);
    }

}
