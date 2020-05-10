package ru.geekbrains.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprites.Background;
import ru.geekbrains.sprites.Logo;

public class MenuScreen extends BaseScreen {
    private Texture background;
    private Background backgroundSprite;
    private Texture logo;
    private Logo logoSprite;

    @Override
    public void show() {
        super.show();

        background = new Texture("StarsSky_v1.jpg");
        backgroundSprite = new Background(background);

        logo = new Texture("SpaceShip1.png");
        logoSprite = new Logo(logo);
        logoSprite.pos.set(0, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        logoSprite.update(delta);
        batch.begin();
        backgroundSprite.draw(batch);
        logoSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        logo.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        logoSprite.startMove(new Vector2(screenX,
                super.getScreenBounds().getHeight() - screenY).mul(super.getScreenToWorld()));
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        logoSprite.startMove(new Vector2(screenX,
                super.getScreenBounds().getHeight() - screenY).mul(super.getScreenToWorld()));
        return super.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public void resize(Rect worldBounds) {
        backgroundSprite.resize(worldBounds);
        logoSprite.resize(worldBounds);
    }
}
