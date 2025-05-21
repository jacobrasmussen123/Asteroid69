package dk.sdu.cbse.asteroid;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {
    private static final int INITIAL_ASTEROID_COUNT = 6;
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        double width = gameData.getDisplayWidth();
        double height = gameData.getDisplayHeight();

        for (int i = 0; i < INITIAL_ASTEROID_COUNT; i++) {
            // Choose a random size
            Asteroid.Size size = Asteroid.Size.values()[
                    random.nextInt(Asteroid.Size.values().length)
                    ];

            // Spawn at a random edge
            double x, y;
            switch (random.nextInt(4)) {
                case 0: // left
                    x = 0;
                    y = random.nextDouble() * height;
                    break;
                case 1: // right
                    x = width;
                    y = random.nextDouble() * height;
                    break;
                case 2: // top
                    x = random.nextDouble() * width;
                    y = 0;
                    break;
                default: // bottom
                    x = random.nextDouble() * width;
                    y = height;
            }

            // Random direction
            double direction = random.nextDouble() * 360.0;

            // Create and add asteroid
            Asteroid asteroid = new Asteroid(size, x, y, direction);
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove all asteroids
        world.getEntities(Asteroid.class)
                .forEach(world::removeEntity);
    }
}
