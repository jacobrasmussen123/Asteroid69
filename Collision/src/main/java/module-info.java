module dk.sdu.cbse.collision {
    requires dk.sdu.cbse.common;

    exports dk.sdu.cbse.collision;

    uses dk.sdu.cbse.common.asteroid.AsteroidSplitter;
    provides dk.sdu.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.cbse.collision.CollisionSystem;
}
