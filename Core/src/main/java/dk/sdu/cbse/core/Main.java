package dk.sdu.cbse.core;

import dk.sdu.cbse.common.data.*;
import dk.sdu.cbse.common.services.IGamePluginService;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;

import java.util.*;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final List<IGamePluginService> plugins = new ArrayList<>();
    private final Map<String, Node> entityViews = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    private Pane gamePane;
    private Scene scene;

    private Label wallModeLabel;

    private final List<IEntityProcessingService> processors     = new ArrayList<>();
    private final List<IPostEntityProcessingService> postProcessors = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: darkblue;");
        scene = new Scene(gamePane, 1600, 900);

        setupKeyHandling();
        setupLabels();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dietz Asteroids");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        loadPlugins();     // plugins start() called once here
        loadProcessors();  // processors & postProcessors loaded once
        resizeArena();

        scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeArena());
        scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeArena());

        new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;

                gameData.setDeltaTime(dt);
                updateKeys();

                for (IEntityProcessingService s : processors) {
                    s.process(gameData, world);
                }
                for (IPostEntityProcessingService s : postProcessors) {
                    s.process(gameData, world);
                }

                syncViews();
            }
        }.start();
    }

    private void setupKeyHandling() {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            activeKeys.add(code);
            if (code == KeyCode.F) {
                Stage stage = (Stage) scene.getWindow();
                stage.setFullScreen(!stage.isFullScreen());
            } else if (code == KeyCode.TAB) {
                WallCollisionMode current = gameData.getWallMode();
                WallCollisionMode[] modes = WallCollisionMode.values();
                int next = (current.ordinal() + 1) % modes.length;
                gameData.setWallMode(modes[next]);
                wallModeLabel.setText("Wall Mode: " + modes[next]);
            }
        });
        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
    }

    private void updateKeys() {
        GameKeys keys = gameData.getKeys();
        keys.setKey(GameKeys.UP,
                activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W)
        );
        keys.setKey(GameKeys.LEFT,
                activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)
        );
        keys.setKey(GameKeys.RIGHT,
                activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)
        );
        keys.setKey(GameKeys.SPACE, activeKeys.contains(KeyCode.SPACE));
        keys.update();
    }

    private void setupLabels() {
        wallModeLabel = new Label("Wall Mode: " + gameData.getWallMode());
        wallModeLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#fff; "
                + "-fx-background-color:rgba(197,197,197,0.6);");
        wallModeLabel.setTranslateX(10);
        wallModeLabel.setTranslateY(10);
        gamePane.getChildren().add(wallModeLabel);
    }

    private void loadPlugins() {
        ServiceLoader.load(IGamePluginService.class).forEach(p -> {
            plugins.add(p);
            p.start(gameData, world);   // each plugin spawns its entities exactly once
        });
    }

    private void loadProcessors() {
        ServiceLoader.load(IEntityProcessingService.class)
                .forEach(processors::add);
        ServiceLoader.load(IPostEntityProcessingService.class)
                .forEach(postProcessors::add);
    }

    private void resizeArena() {
        double width = scene.getWidth(), height = scene.getHeight();
        gamePane.setPrefSize(width, height);
        gameData.setDisplayWidth((int) width);
        gameData.setDisplayHeight((int) height);
    }

    private void syncViews() {
        // remove views for deleted entities
        entityViews.entrySet().removeIf(entry -> {
            if (world.getEntity(entry.getKey()) == null) {
                gamePane.getChildren().remove(entry.getValue());
                return true;
            }
            return false;
        });

        // add/update views for existing entities
        for (Entity e : world.getEntities()) {
            Node view = entityViews.get(e.getID());
            if (view == null) {
                Polygon poly = new Polygon(e.getPolygonCoordinates());
                poly.setRotate(e.getRotation());
                poly.setTranslateX(e.getX());
                poly.setTranslateY(e.getY());
                Color base = e.getBaseColor();
                poly.setFill(base.darker());
                poly.setStroke(base.brighter());
                poly.setStrokeWidth(3);
                gamePane.getChildren().add(poly);
                entityViews.put(e.getID(), poly);
            } else {
                view.setTranslateX(e.getX());
                view.setTranslateY(e.getY());
                view.setRotate(e.getRotation());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
