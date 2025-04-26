package dk.sdu.cbse.player.systems;

import dk.sdu.cbse.common.WallCollisionMode;
import dk.sdu.cbse.player.Player;

public class MovementSystem {
    public void applyThrust(Player p, float dt) {
        double rad = Math.toRadians(p.getRotation());
        p.setDx(p.getDx() + Math.cos(rad) * Player.ACCEL * dt);
        p.setDy(p.getDy() + Math.sin(rad) * Player.ACCEL * dt);
        clampVelocity(p);
    }

    private void clampVelocity(Player p) {
        double speed = Math.hypot(p.getDx(), p.getDy());
        if (speed > Player.MAX_SPEED) {
            double scale = Player.MAX_SPEED / speed;
            p.setDx(p.getDx() * scale);
            p.setDy(p.getDy() * scale);
        }
    }

    public void moveAndHandleWalls(Player p, float dt, int w, int h, WallCollisionMode mode) {
        double newX = p.getX() + p.getDx() * dt;
        double newY = p.getY() + p.getDy() * dt;
        double dx = p.getDx();
        double dy = p.getDy();

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

        p.setX(newX);
        p.setY(newY);
        p.setDx(dx);
        p.setDy(dy);
    }


    private double wrap(double val, double max) {
        if (val < 0) return val + max;
        if (val > max) return val - max;
        return val;
    }
}