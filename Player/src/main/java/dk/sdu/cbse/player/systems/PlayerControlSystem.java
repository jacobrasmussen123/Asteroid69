package dk.sdu.cbse.player.systems;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.data.WallCollisionMode;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.player.Player;

public class PlayerControlSystem implements IEntityProcessingService {
    private final InputSystem input = new InputSystem();
    private final MovementSystem movement = new MovementSystem();

    @Override
    public void process(GameData data, World world) {
        // Use existing deltaTime and wall mode from GameData
        float dt = data.getDeltaTime();
        int width = data.getDisplayWidth();
        int height = data.getDisplayHeight();
        WallCollisionMode mode = data.getWallMode();

        for (Entity entity : world.getEntities()) {
            if (!(entity instanceof Player)) {
                continue;
            }
            Player player = (Player) entity;

            // Rotation
            if (input.isRotatingLeft(data.getKeys())) {
                player.setRotation(player.getRotation() - player.getRotationSpeed() * dt);
            }
            if (input.isRotatingRight(data.getKeys())) {
                player.setRotation(player.getRotation() + player.getRotationSpeed() * dt);
            }

            // Thrust vs Deceleration
            if (input.isAccelerating(data.getKeys())) {
                movement.applyThrust(player, dt);
            } else {
                movement.applyDeceleration(player, dt, Player.DECEL_FACTOR);
            }

            // Movement & wall handling using GameData's wall mode
            movement.moveAndHandleWalls(player, dt, width, height, mode);
        }
    }
}