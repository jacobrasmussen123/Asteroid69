module Asteroid {
    exports dk.sdu.cbse.asteroid;
    requires Common;
    requires Bullet;
    provides  dk.sdu.cbse.common.services.IGamePluginService with dk.sdu.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService with dk.sdu.cbse.asteroid.AsteroidProcessor;
}