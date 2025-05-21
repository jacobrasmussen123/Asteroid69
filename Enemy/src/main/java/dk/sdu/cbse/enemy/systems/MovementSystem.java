package dk.sdu.cbse.enemy.systems;

import dk.sdu.cbse.common.data.WallCollisionMode;
import dk.sdu.cbse.enemy.Enemy;

public class MovementSystem {
    public void applyThrust(Enemy e, float dt) {
        double rad = Math.toRadians(e.getRotation());
        e.setDx(e.getDx() + Math.cos(rad) * Enemy.ACCEL * dt);
        e.setDy(e.getDy() + Math.sin(rad) * Enemy.ACCEL * dt);
        clampVelocity(e);
    }

    private void clampVelocity(Enemy e) {
        double speed = Math.hypot(e.getDx(), e.getDy());
        if (speed > Enemy.MAX_SPEED) {
            double scale = Enemy.MAX_SPEED / speed;
            e.setDx(e.getDx() * scale);
            e.setDy(e.getDy() * scale);
        }
    }

    public void moveAndHandleWalls(Enemy e, float dt, int w, int h, WallCollisionMode mode) {
        double newX = e.getX() + e.getDx() * dt;
        double newY = e.getY() + e.getDy() * dt;
        double dx = e.getDx();
        double dy = e.getDy();

        switch (mode) {
            case WRAP:
                newX = wrap(newX, w);
                newY = wrap(newY, h);
                break;

            case BOUNCE:
                if (newX < 0 || newX > w) {
                    dx *= -1;
                    newX = Math.max(0, Math.min(newX, w));
                }
                if (newY < 0 || newY > h) {
                    dy *= -1;
                    newY = Math.max(0, Math.min(newY, h));
                }
                break;

            case STOP:
                if ((newX < 0 && dx < 0) || (newX > w && dx > 0)) dx = 0;
                if ((newY < 0 && dy < 0) || (newY > h && dy > 0)) dy = 0;
                newX = Math.max(0, Math.min(newX, w));
                newY = Math.max(0, Math.min(newY, h));
                break;
        }

        e.setX(newX);
        e.setY(newY);
        e.setDx(dx);
        e.setDy(dy);
    }


    private double wrap(double val, double max) {
        if (val < 0) return val + max;
        if (val > max) return val - max;
        return val;
    }
}