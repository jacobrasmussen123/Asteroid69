package dk.sdu.cbse.core;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleLayer;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class Main extends Application {

    private static ModuleLayer pluginLayer;
    private final GameData gameData = new GameData();
    private final World world       = new World();

    public static void main(String[] args) {
        // 1) Locate the "plugins" folder
        Path pluginsDir = Path.of("plugins");

        // 2) Find all modules (JARs) in there
        ModuleFinder finder = ModuleFinder.of(pluginsDir);

        // 3) Extract their module names
        List<String> names = finder.findAll().stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .collect(Collectors.toList());

        // 4) Resolve them against the boot layer
        Configuration config = ModuleLayer.boot()
                .configuration()
                .resolve(finder, ModuleFinder.of(), names);

        // 5) Create a child layer for your plugins
        pluginLayer = ModuleLayer.boot()
                .defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());

        // 6) Launch JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root,
                gameData.getDisplayWidth(),
                gameData.getDisplayHeight()
        );
        primaryStage.setScene(scene);
        primaryStage.setTitle("Asteroid69");
        primaryStage.show();

        // Boot-layer plugins (Enemy)
        ServiceLoader<IGamePluginService> bootPlugins =
                ServiceLoader.load(IGamePluginService.class);
        bootPlugins.forEach(s -> s.start(gameData, world));

        // Plugin-layer plugins (Player)
        ServiceLoader<IGamePluginService> playerPlugins =
                ServiceLoader.load(pluginLayer, IGamePluginService.class);
        playerPlugins.forEach(s -> s.start(gameData, world));

        // Gather processors
        var bootMovers   = ServiceLoader.load(IEntityProcessingService.class);
        var pluginMovers = ServiceLoader.load(pluginLayer, IEntityProcessingService.class);
        var bootPost     = ServiceLoader.load(IPostEntityProcessingService.class);
        var pluginPost   = ServiceLoader.load(pluginLayer, IPostEntityProcessingService.class);

        // Game loop
        new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;
                gameData.setDeltaTime(dt);

                bootMovers.forEach(m -> m.process(gameData, world));
                pluginMovers.forEach(m -> m.process(gameData, world));
                bootPost.forEach(p -> p.process(gameData, world));
                pluginPost.forEach(p -> p.process(gameData, world));

                render(root);
            }
        }.start();
    }

    /** Draws/removes entities each frame. */
    private void render(Pane root) {
        // Remove old views
        root.getChildren().removeIf(n -> {
            Object id = n.getUserData();
            return id instanceof String && world.getEntity((String) id) == null;
        });

        // Draw or update each entity
        world.getEntities().forEach(e -> {
            String id = e.getID();
            Node existing = root.getChildren().stream()
                    .filter(n -> id.equals(n.getUserData()))
                    .findFirst()
                    .orElse(null);

            Polygon poly;
            if (existing == null) {
                poly = new Polygon(e.getPolygonCoordinates());
                poly.setFill(Color.TRANSPARENT);
                poly.setStroke(Color.WHITE);
                poly.setUserData(id);
                root.getChildren().add(poly);
            } else {
                poly = (Polygon) existing;
            }

            poly.setTranslateX(e.getX());
            poly.setTranslateY(e.getY());
            poly.setRotate(e.getRotation());
        });
    }
}
