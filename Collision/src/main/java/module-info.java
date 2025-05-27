module Collision {
    requires Common;
    requires java.net.http;

    exports dk.sdu.cbse.collision;

    uses    dk.sdu.cbse.common.asteroid.AsteroidSplitter;

    provides dk.sdu.cbse.common.services.IPostEntityProcessingService with dk.sdu.cbse.collision.CollisionSystem;
}