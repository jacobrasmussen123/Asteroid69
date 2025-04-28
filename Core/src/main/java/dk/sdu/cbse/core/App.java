package dk.sdu.cbse.core;

import dk.sdu.cbse.common.*;
import dk.sdu.cbse.common.services.IGamePluginService;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.scene.control.Label;



import java.util.*;

public class App extends Application {
        private final GameData gameData = new GameData();
        private final World world = new World();
        private Label wallModeLabel = new Label();
        private final List<IGamePluginService> plugins = new ArrayList<>();
        private final Map<String, Node> entityViews = new HashMap<>();
        private Pane root;
        private Scene scene;

        @Override
        public void start(Stage primaryStage) {
                root = new Pane();
                scene = new Scene(root, 800, 600);

                primaryStage.setScene(scene);
                primaryStage.setTitle("Dynamic Asteroids Arena");
                primaryStage.setFullScreenExitHint("");
                primaryStage.show();

                wallModeLabel.setStyle(
                        "-fx-font-size: 16px;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: rgba(0, 0, 0, 0.6);" +
                                "-fx-padding: 4px 8px;" +
                                "-fx-background-radius: 6px;"
                );
                wallModeLabel.setTranslateX(10);
                wallModeLabel.setTranslateY(10);
               // wallModeLabel.setText("Wall Mode: " + gameData.getWallMode());
                root.getChildren().add(wallModeLabel);


                setupKeyInput();
                loadPlugins();

                // Correctly bind arena size directly to scene dimensions
                scene.widthProperty().addListener((obs, oldVal, newVal) -> resizeArena());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> resizeArena());

                // Initial sizing
                resizeArena();

                // Game loop
                new AnimationTimer() {
                        private long last = System.nanoTime();

                        @Override
                        public void handle(long now) {
                                float dt = (now - last) / 1_000_000_000f;
                                last = now;
                                gameData.setDeltaTime(dt);

                                plugins.forEach(p -> p.update(gameData, world));
                                syncViews();
                        }
                }.start();
        }

        private void setupKeyInput() {
                scene.setOnKeyPressed(e -> handleKey(e, true));
                scene.setOnKeyReleased(e -> handleKey(e, false));
                scene.setOnKeyTyped(e -> {
                        if (e.getCharacter().equals("f")) {
                                Stage stage = (Stage) scene.getWindow();
                                stage.setFullScreen(!stage.isFullScreen());
                        }
                });
        }

        private void loadPlugins() {
                ServiceLoader.load(IGamePluginService.class).forEach(p -> {
                        plugins.add(p);
                        p.start(gameData, world);
                });
        }

        private void resizeArena() {
                double width = scene.getWidth();
                double height = scene.getHeight();

                root.setPrefSize(width, height);
                gameData.setDisplayWidth((int) width);
                gameData.setDisplayHeight((int) height);
        }

        private void handleKey(KeyEvent e, boolean down) {
                KeyCode code = e.getCode();
                if (code == KeyCode.UP) gameData.getKeys().setKey(GameKeys.UP, down);
                if (code == KeyCode.LEFT) gameData.getKeys().setKey(GameKeys.LEFT, down);
                if (code == KeyCode.RIGHT) gameData.getKeys().setKey(GameKeys.RIGHT, down);
                if (code == KeyCode.SPACE) gameData.getKeys().setKey(GameKeys.SPACE, down);
                if (code == KeyCode.TAB) {
                        WallCollisionMode current = gameData.getWallMode();
                        WallCollisionMode[] values = WallCollisionMode.values();
                        int nextIndex = (current.ordinal() + 1) % values.length;
                        WallCollisionMode newMode = values[nextIndex];
                        gameData.setWallMode(newMode);
                        wallModeLabel.setText("Wall Mode: " + newMode);
                }

        }

        private void syncViews() {
                entityViews.keySet().removeIf(id -> {
                        if (world.getEntity(id) == null) {
                                root.getChildren().remove(entityViews.get(id));
                                return true;
                        }
                        return false;
                });

                for (Entity e : world.getEntities()) {
                        Node view = entityViews.get(e.getID());
                        if (view == null) {
                                Polygon poly = new Polygon(e.getPolygonCoordinates());
                                root.getChildren().add(poly);
                                entityViews.put(e.getID(), poly);
                                view = poly;
                        }
                        view.setTranslateX(e.getX());
                        view.setTranslateY(e.getY());
                        view.setRotate(e.getRotation());
                }
        }

        public static void main(String[] args) {
                launch(args);
        }
}