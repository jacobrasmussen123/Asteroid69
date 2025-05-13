package dk.sdu.cbse.player.systems;

import dk.sdu.cbse.common.WallCollisionMode;
import  dk.sdu.cbse.player.Player;

public class MovementSystem {
    public void applyThrust(Player player, float dt) {
        double rad = Math.toRadians(player.getRotation());
        player.setDx(player.getDx() + Math.cos(rad) * Player.ACCEL * dt);
        player.setDy(player.getDy() + Math.sin(rad) * Player.ACCEL * dt);
        clampVelocity(player);
    }

    public void applyDeceleration(Player player, float dt, double decelFactor) {
        player.setDx(player.getDx() * Math.pow(decelFactor, dt * 60));
        player.setDy(player.getDy() * Math.pow(decelFactor, dt * 60));
    }


    private void clampVelocity(Player player) {
        double speed = Math.hypot(player.getDx(), player.getDy());
        if (speed > Player.MAX_SPEED) {
            double scale = Player.MAX_SPEED / speed;
            player.setDx(player.getDx() * scale);
            player.setDy(player.getDy() * scale);
        }
    }

    public void moveAndHandleWalls(Player player, float dt, int w, int h, WallCollisionMode mode) {
        double newX = player.getX() + player.getDx() * dt;
        double newY = player.getY() + player.getDy() * dt;
        double dx = player.getDx();
        double dy = player.getDy();

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

        player.setX(newX);
        player.setY(newY);
        player.setDx(dx);
        player.setDy(dy);
    }


    private double wrap(double val, double max) {
        if (val < 0) return val + max;
        if (val > max) return val - max;
        return val;
    }
}