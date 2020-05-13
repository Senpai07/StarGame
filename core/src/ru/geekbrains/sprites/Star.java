package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {


    private Vector2 speedVector;
    private Rect worldBounds;

    private float animateTimer;
    private float animateInterval;
    private float starScale;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star"));
        speedVector = new Vector2();
        float speedX = Rnd.nextFloat(-0.005f, 0.005f);
        float speedy = Rnd.nextFloat(-0.2f, -0.05f);
        speedVector.set(speedX, speedy);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(Rnd.nextFloat(0.005f, 0.01f));
        setTop(worldBounds.getTop() - 0.15f);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
        starScale = Rnd.nextFloat(0.5f, 1.5f);
        setScale(starScale);
        animateInterval = Rnd.nextFloat(0.5f, 2f);
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        setScale(getScale() - 0.01f);
        if (animateTimer >= animateInterval) {
            setScale(starScale);
            animateTimer = 0f;
        }
        pos.mulAdd(speedVector, delta);
        checkBounds();
    }

    private void checkBounds() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }

    }
}
