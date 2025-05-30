package dk.sdu.cbse.core;

import dk.sdu.cbse.common.data.*;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.common.util.ServiceLocator;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<String, Node> entityViews = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    private Pane gamePane;
    private Scene scene;

    private Label wallModeLabel;
    private Label scoreLabel;
    private Label highScoreLabel;
    private final HttpClient http = HttpClient.newHttpClient();
    private boolean reported = false;
    private List<IGamePluginService> plugins;
    private List<IEntityProcessingService> processors;
    private List<IPostEntityProcessingService> postProcessors;

    private HttpClient httpClient;
    private Text scoreText;

    private static final String SCORE_SERVICE_URL = "http://localhost:8080";

    public Game(
            List<IGamePluginService> plugins,
            List<IEntityProcessingService> processors,
            List<IPostEntityProcessingService> postProcessors
    ) {
        this.plugins = plugins;
        this.processors = processors;
        this.postProcessors = postProcessors;
    }

    public void start(Stage primaryStage) {

        httpClient = HttpClient.newHttpClient();
        resetScore();
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: darkblue;");
        scoreText = new Text(10, 40, "Score: 0");
        scoreText.setFill(Color.ORANGERED);
        gamePane.getChildren().add(scoreText);
        scene = new Scene(gamePane, 1600, 900);
        setupKeyHandling();
        setupLabels();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Eris Emma Myers ASTEROID KILLERS");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();


        for (IGamePluginService p : plugins) {
            p.start(gameData, world);
        }

        scene.widthProperty().addListener((o, oldV, newV) -> resizeArena());
        scene.heightProperty().addListener((o, oldV, newV) -> resizeArena());

        // --- Main loop ---
        new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;

                gameData.setDeltaTime(dt);
                updateKeys();

                // 1) Game logic
                for (IEntityProcessingService s : processors) {
                    s.process(gameData, world);
                }
                for (IPostEntityProcessingService s : postProcessors) {
                    s.process(gameData, world);
                }

                pollScore();
                syncViews();

            }
        }.start();
    }

    private void setupKeyHandling() {
        scene.setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            activeKeys.add(c);
            if (c == KeyCode.F) {
                Stage st = (Stage) scene.getWindow();
                st.setFullScreen(!st.isFullScreen());
            } else if (c == KeyCode.TAB) {
                WallCollisionMode curr = gameData.getWallMode();
                WallCollisionMode[] modes = WallCollisionMode.values();
                gameData.setWallMode(modes[(curr.ordinal()+1)%modes.length]);
                wallModeLabel.setText("Wall Mode: " + gameData.getWallMode());
            }
        });
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    private void updateKeys() {
        GameKeys k = gameData.getKeys();
        k.setKey(GameKeys.UP, activeKeys.contains(KeyCode.UP) || activeKeys.contains(KeyCode.W));
        k.setKey(GameKeys.LEFT, activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A));
        k.setKey(GameKeys.RIGHT, activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D));
        k.setKey(GameKeys.SPACE, activeKeys.contains(KeyCode.SPACE));
        k.update();
    }

    private void setupLabels() {
        wallModeLabel = new Label("Wall Mode: " + gameData.getWallMode());
        wallModeLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#fff; "
                + "-fx-background-color:rgba(197,197,197,0.6);");
        wallModeLabel.relocate(10, 70);
        gamePane.getChildren().add(wallModeLabel);
    }



    private void resizeArena() {
        double w = scene.getWidth(), h = scene.getHeight();
        gamePane.setPrefSize(w, h);
        gameData.setDisplayWidth((int)w);
        gameData.setDisplayHeight((int)h);
    }

    private void syncViews() {
        // Remove deleted
        entityViews.entrySet().removeIf(ent -> {
            if (world.getEntity(ent.getKey()) == null) {
                gamePane.getChildren().remove(ent.getValue());
                return true;
            }
            return false;
        });
        // Add/update existing
        for (Entity e : world.getEntities()) {
            Node v = entityViews.get(e.getID());
            if (v == null) {
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
                v.setTranslateX(e.getX());
                v.setTranslateY(e.getY());
                v.setRotate(e.getRotation());
            }
        }
    }


    private void resetScore() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SCORE_SERVICE_URL + "/score/set/0"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .exceptionally(ex -> null);
    }

    private void pollScore() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SCORE_SERVICE_URL + "/score/get"))
                .GET()
                .build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body ->
                        Platform.runLater(() -> scoreText.setText("Score: " + body))
                )
                .exceptionally(ex -> null);
    }
}
