package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.common.GameData;
import dk.sdu.cbse.common.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        for(int i = 0; i < 4; i++) {
            Asteroid asteroid = new Asteroid(
                    Asteroid.Size.LARGE,
                    Math.random() * gameData.getDisplayWidth(),
                    Math.random() * gameData.getDisplayHeight(),
                    Math.random() * 360
            );
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity e : world.getEntities(Asteroid.class)) {
            world.removeEntity(e);
        }
    }

    @Override
    public void update(GameData data, World world) {}
}