package ru.geekbrains.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.BulletPool;

public class MainShip extends Sprite {

    private static final float SPEED = 0.5f;
    private static final float MARGIN = 0.05f;
    private static final float SIZE_SHIP = 0.15f;
    private final Vector2 moveRightVector;
    private Vector2 targetVector;
    private Vector2 distanceVector;
    private Vector2 speedVector;
    private boolean startTouchMove;
    private Rect worldBounds;
    private boolean pressedLeft;
    private boolean pressedRight;

    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private Vector2 bulletSpeed;

    public MainShip(TextureAtlas textureAtlas, BulletPool bulletPool) {
        super(textureAtlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        bulletRegion = textureAtlas.findRegion("bulletMainShip");
        bulletSpeed = new Vector2(0, 0.5f);
        targetVector = new Vector2();
        speedVector = new Vector2();
        distanceVector = new Vector2();
        moveRightVector = new Vector2(0.5f, 0f);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SIZE_SHIP);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        if (startTouchMove) {
            distanceVector.set(targetVector);
            if (distanceVector.sub(pos).len() > SPEED) {
                pos.add(speedVector);
            } else {
                pos.set(targetVector);
                speedVector.setZero();
                startTouchMove = false;
            }
        } else {
            pos.mulAdd(speedVector, delta);
            if (getLeft() < worldBounds.getLeft()) {
                moveStop();
                setLeft(worldBounds.getLeft());
            }
            if (getRight() > worldBounds.getRight()) {
                moveStop();
                setRight(worldBounds.getRight());
            }
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
        startTouchMove = true;
        this.targetVector.set(touch);
        speedVector.set(touch.sub(pos)).setLength(SPEED);
    }

    private void moveRight() {
        speedVector.set(moveRightVector);
    }

    private void moveLeft() {
        speedVector.set(moveRightVector).rotate(180);
    }

    private void moveStop() {
        speedVector.setZero();
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    moveStop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    moveStop();
                }
        }
        return false;
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletSpeed, 0.01f, worldBounds, 1);
    }
}
