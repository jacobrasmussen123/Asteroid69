package dk.sdu.cbse.core;

import dk.sdu.cbse.common.data.*;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.common.util.ServiceLocator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ModuleConfig.class);
        ctx.getBean(Game.class).start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
