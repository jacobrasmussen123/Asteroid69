package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.Health;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.enemy.Enemy;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public class EnemyPlugin implements IGamePluginService {
    private static final int ENEMY_COUNT = 8;

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < ENEMY_COUNT; i++) {
            Entity enemy = createEnemyShip(gameData);
            world.addEntity(enemy);
        }
    }


    @Override
    public void stop(GameData gameData, World world) {
        List<Entity> enemies = world.getEntities(Enemy.class);
        for (Entity e : enemies) {
            world.removeEntity(e);
        }
    }

    private Entity createEnemyShip(GameData gameData) {
        Enemy enemy = new Enemy();

        // Shape
        enemy.setPolygonCoordinates(
                -20,   0,
                -15, -10,
                -5, -15,
                5, -15,
                15, -10,
                20,   0,
                15,  10,
                5,  15,
                -5,  15,
                -15, 10
        );
        spawnAtEdge(enemy, gameData);
        enemy.setSize(16.67);
        enemy.setRadius((float) enemy.getSize());
        enemy.setPaint(Color.BLUE);

        return enemy;
    }

    private void spawnAtEdge(Entity enemy, GameData gameData) {
        Random rnd = new Random();
        int edge = rnd.nextInt(4);
        double w = gameData.getDisplayWidth();
        double h = gameData.getDisplayHeight();
        double x, y, rot;

        switch (edge) {
            case 0:
                x = 0;
                y = rnd.nextDouble(0, h);
                rot = rnd.nextDouble(-90, 90);
                break;
            case 1:
                x = w;
                y = rnd.nextDouble(0, h);
                rot = rnd.nextDouble(90, 270);
                break;
            case 2:
                x = rnd.nextDouble(0, w);
                y = 0;
                rot = rnd.nextDouble(0, 180);
                break;
            default:
                x = rnd.nextDouble(0, w);
                y = h;
                rot = rnd.nextDouble(180, 360);
                break;
        }

        enemy.setX(x);
        enemy.setY(y);
        enemy.setRotation(rot);
    }
}
