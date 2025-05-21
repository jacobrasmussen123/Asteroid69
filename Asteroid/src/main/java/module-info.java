module Asteroid {
    requires Common;

    exports dk.sdu.cbse.asteroid;

    provides dk.sdu.cbse.common.services.IGamePluginService
            with dk.sdu.cbse.asteroid.AsteroidPlugin;
    provides dk.sdu.cbse.common.services.IEntityProcessingService
            with dk.sdu.cbse.asteroid.AsteroidProcessor;
    provides dk.sdu.cbse.common.asteroid.AsteroidSplitter
            with dk.sdu.cbse.asteroid.AsteroidSplitterMain;
}