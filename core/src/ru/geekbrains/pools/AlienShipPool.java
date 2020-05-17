package ru.geekbrains.pools;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.sprites.AlienShip;
import ru.geekbrains.sprites.Bullet;

public class AlienShipPool extends SpritesPool<AlienShip> {
    @Override
    protected AlienShip newObject() {
        return new AlienShip();
    }
}
