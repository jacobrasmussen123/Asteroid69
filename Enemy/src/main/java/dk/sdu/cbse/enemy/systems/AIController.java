package dk.sdu.cbse.enemy.systems;

import dk.sdu.cbse.common.Entity;
import dk.sdu.cbse.enemy.Enemy;
import java.util.Random;

public class AIController {
    private final Random random = new Random();
    private double currentTargetAngle;
    private double angleUpdateTimer;

    public void updateAI(Enemy enemy, Entity target, float dt) {
        if (target == null) return;

        // Calculate angle to player
        double dx = target.getX() - enemy.getX();
        double dy = target.getY() - enemy.getY();
        double targetAngle = Math.toDegrees(Math.atan2(dy, dx));

        // Add some randomness to the targeting
        if (angleUpdateTimer <= 0 || random.nextDouble() < 0.1) {
            currentTargetAngle = targetAngle + (random.nextDouble() * 60 - 30);
            angleUpdateTimer = 0.5f;
        }
        angleUpdateTimer -= dt;

        // Rotate towards target angle
        double angleDiff = currentTargetAngle - enemy.getRotation();
        angleDiff = (angleDiff + 180) % 360 - 180; // Normalize to -180..180

        if (angleDiff < -5) {
            enemy.setRotation(enemy.getRotation() - Enemy.ROT_SPEED * dt);
        } else if (angleDiff > 5) {
            enemy.setRotation(enemy.getRotation() + Enemy.ROT_SPEED * dt);
        }

        // Accelerate randomly but mostly towards player
        if (random.nextDouble() < 0.8) {
            double rad = Math.toRadians(enemy.getRotation());
            enemy.setDx(enemy.getDx() + Math.cos(rad) * Enemy.ACCEL * dt);
            enemy.setDy(enemy.getDy() + Math.sin(rad) * Enemy.ACCEL * dt);
        }
    }

    public boolean shouldShoot(Random random) {
        return random.nextDouble() < 0.02; // 2% chance per frame
    }
}