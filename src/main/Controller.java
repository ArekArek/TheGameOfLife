package main;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class Controller {
    private static final int TABLE_SIZE = 30;
    private static final int CELL_SIZE = 20;
    private static final int TILE_SIZE = CELL_SIZE - 4;
    private boolean boardEditing = true;
    private Group baseTable;
    private int step = -1;

    private static final int THREADS_COUNT = 1;

    @FXML
    private Label myLabel;
    @FXML
    private Label stepLabel;

    @FXML
    private Button myButton;

    @FXML
    private StackPane myTable;

    @FXML
    private void initialize() {
        LifeBoard.init(THREADS_COUNT, TABLE_SIZE);
        baseTable = createBaseTable();

        refreshTable();
        myLabel.setText("editing enabled");
        myButton.setText("start");
    }

    @FXML
    private void buttonPressed() {
        step++;
        if (step == 0) {
            myButton.setText("next");
            myLabel.setText("editing disabled");
        }
        stepLabel.setText("step: " + step);
        boardEditing = false;
        processThreads();
        refreshTable();
        LifeBoard.threadsFinished();
    }

    private void refreshTable() {
        myTable.getChildren().clear();
        Group filling = createFilledBoard();
        myTable.getChildren().add(filling);
    }

    private Group createBaseTable() {
        Group table = new Group();
        IntStream.range(0, LifeBoard.getSize()).boxed().forEach(i ->
                IntStream.range(0, LifeBoard.getSize()).boxed().forEach(j -> {
                    Rectangle cell = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    cell.getStyleClass().add("cellBackground");
                    cell.setOnMouseClicked(e -> {
                        if (boardEditing) {
                            LifeBoard.setState(i, j, true);
                            refreshTable();
                        }
                    });
                    table.getChildren().add(cell);
                })
        );
        return table;
    }

    private Group createFilledBoard() {
        Group board = new Group(baseTable);

        IntStream.range(0, LifeBoard.getSize()).boxed().forEach(i ->
                IntStream.range(0, LifeBoard.getSize()).boxed().forEach(j -> {
                    if (LifeBoard.getState(i, j)) {
                        Label label = new Label();
                        label.setMinSize(TILE_SIZE, TILE_SIZE);
                        label.setPrefSize(TILE_SIZE, TILE_SIZE);
                        label.setMaxSize(TILE_SIZE, TILE_SIZE);
                        label.getStyleClass().add("cellMarked");
                        label.setLayoutX((i + 0.5) * CELL_SIZE - TILE_SIZE / 2);
                        label.setLayoutY((j + 0.5) * CELL_SIZE - TILE_SIZE / 2);
                        label.setOnMouseClicked(e -> {
                            if (boardEditing) {
                                LifeBoard.setState(i, j, false);
                                refreshTable();
                            }
                        });
                        board.getChildren().add(label);
                    }
                })
        );
        return board;
    }

    private static int processThreads() {
        ExecutorService threadPool;
        threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < THREADS_COUNT; i++) {
            threadPool.submit(new LifeBoard());
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            return 0;
        } catch (InterruptedException e) {
            return 1;
        }
    }
}
