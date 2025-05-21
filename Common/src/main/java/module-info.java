module Common {
    requires javafx.graphics;

    exports dk.sdu.cbse.common.data;
    exports dk.sdu.cbse.common.services;
    exports dk.sdu.cbse.common.asteroid;
    exports dk.sdu.cbse.common.bullet;

    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.cbse.common.asteroid.AsteroidSplitter;
    uses dk.sdu.cbse.common.bullet.BulletSPI;
}