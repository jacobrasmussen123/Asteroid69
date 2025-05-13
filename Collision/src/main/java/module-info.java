module Collision {
    exports dk.sdu.cbse.collision;
    requires Asteroid;
    requires Enemy;
    requires Bullet;
    requires Player;
    requires Common;
    requires javafx.graphics;



    provides dk.sdu.cbse.common.services.IPostEntityProcessingService with dk.sdu.cbse.collision.CollisionSystem;
}