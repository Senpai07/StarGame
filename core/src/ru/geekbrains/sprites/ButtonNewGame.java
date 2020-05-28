package ru.geekbrains.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screens.GameScreen;

public class ButtonNewGame extends ScaledButton {

    private static final float MARGIN = 0.05f;
    private static final float ANIMATE_INTERVAL = 1f;

    private GameScreen gameScreen;
    private float animateTimer;
    private boolean scaleUp = true;

    public ButtonNewGame(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer >= ANIMATE_INTERVAL) {
            animateTimer = 0f;
            scaleUp = !scaleUp;
        }
        if (scaleUp) {
            setScale(getScale() + 0.001f);
        }
        else  {
            setScale(getScale() - 0.001f);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(-0.1f);
    }

    @Override
    public void action() {
        gameScreen.startNewGame();
    }
}
