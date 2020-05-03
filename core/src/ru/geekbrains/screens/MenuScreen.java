package ru.geekbrains.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private static final int SPEED = 7;
    private Texture background;
    private Texture spaceShip;
    private Sprite backgroundSprite;
    private Sprite spaceShipSprite;
    private Vector2 positionSpaceShipVector;
    private Vector2 speedSpaceShipVector;
    private Vector2 targetVector;
    private Vector2 directionVector;
    private Vector2 distanceVector;

    @Override
    public void show() {
        super.show();
        if (Gdx.app.getType().equals(Application.ApplicationType.Android)) {
            Gdx.graphics.setWindowedMode(1080, 1920);
            background = new Texture("StarsSky_v1.jpg");
        } else {
            Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            background = new Texture("StarsSky1.jpg");
        }
        backgroundSprite = new Sprite(background);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spaceShip = new Texture("SpaceShip1.png");
        spaceShipSprite = new Sprite(spaceShip);
        spaceShipSprite.setPosition(0, 0);
        spaceShipSprite.setSize(200, 200);

        positionSpaceShipVector = new Vector2();
        speedSpaceShipVector = new Vector2(SPEED, SPEED);
        targetVector = new Vector2();
        directionVector = new Vector2();
        distanceVector = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        distanceVector.set(targetVector);
        if (distanceVector.sub(positionSpaceShipVector).len() > SPEED) {
            positionSpaceShipVector.add(directionVector);
        } else {
            positionSpaceShipVector.set(targetVector);
            directionVector.setZero();
        }
        spaceShipSprite.setPosition(positionSpaceShipVector.x, positionSpaceShipVector.y);
        batch.begin();
        backgroundSprite.draw(batch);
        spaceShipSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        super.dispose();
    }

    private void StartMove(int screenX, int screenY) {
        targetVector.set(screenX - spaceShipSprite.getHeight() / 2,
                Gdx.graphics.getHeight() - screenY - spaceShipSprite.getHeight() / 2);
        distanceVector.set(targetVector);
        distanceVector.sub(positionSpaceShipVector);
        directionVector.set(distanceVector);
        directionVector.nor();
        directionVector.scl(speedSpaceShipVector);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        StartMove(screenX, screenY);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        StartMove(screenX, screenY);
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void resize(int width, int height) {
        backgroundSprite.setSize(width, height);
        super.resize(width, height);
    }
}
