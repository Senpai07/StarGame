package ru.geekbrains.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pools.BulletPool;
import ru.geekbrains.pools.ExplosionPool;

public class MainShip extends Ship {

    private static final float SPEED = 0.015f;
    private static final float MARGIN = 0.05f;
    private static final float SIZE_SHIP = 0.15f;
    private static final int HP = 100;

    private Vector2 targetVector;
    private Vector2 distanceVector;
    private boolean startTouchMove;

    private boolean pressedLeft;
    private boolean pressedRight;

    public MainShip(TextureAtlas textureAtlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(textureAtlas.findRegion("main_ship"), 1, 2, 2);
        targetVector = new Vector2();
        distanceVector = new Vector2();
        moveVector.set(0.5f, 0f);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = textureAtlas.findRegion("bulletMainShip");
        bulletSpeed = new Vector2(0f, 0.7f);
        bulletHeight = 0.01f;
        bulletDamage = 1;
        reloadInterval = 0.2f;
        reloadTimer = reloadInterval;
        hp = HP;
        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(SIZE_SHIP);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
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

    public void dispose() {
        shootSound.dispose();
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
        speedVector.set(moveVector);
    }

    private void moveLeft() {
        speedVector.set(moveVector).rotate(180);
    }

    private void moveStop() {
        speedVector.setZero();
    }

    public boolean keyDown(int keycode) {
        if (!startTouchMove) {
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
            }
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        if (!startTouchMove) {
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
        }
        return false;
    }

}
