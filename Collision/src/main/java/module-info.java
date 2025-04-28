module Collision {
    requires Asteroid;
    requires Enemy;
    requires Bullet;
    requires Player;
    requires Common;


    provides dk.sdu.cbse.common.services.IPostEntityProcessingService with dk.sdu.cbse.collision.CollisionSystem;
}