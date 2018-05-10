package presentationcontrol;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.io.IOException;

public class GenerarHidato {
    private Label username;
    private Button logoutbutton;
    private Button menubutton;


    //PRIVATE OBJECTS
    private Slider rowsslider;
    private Label rowslabel;
    private Slider columnslider;
    private Label columnslabel;
    private Rectangle square;
    private Polygon triangle;
    private Polygon hexagon;
    private ComboBox adjacencycombobox;
    private Integer rows;
    private Integer columns;
    private Character celltype;
    private String adjacency;

    private String usern;
    Stage primaryStage;

    public GenerarHidato(String usern, Stage origin) throws IOException {
        primaryStage = origin;
        this.usern = usern;
        Parent root = FXMLLoader.load(getClass().getResource("/forms/GenerarHidato.fxml"));
        primaryStage.setTitle("Generar Hidato - Hidato Game");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();


        //REFERENCES
        username = (Label) primaryStage.getScene().lookup("#usernamelabel");
        logoutbutton = (Button) primaryStage.getScene().lookup("#logoutbutton");
        menubutton = (Button) primaryStage.getScene().lookup("#menubutton");

        //PRIVATE REFERENCES
        rowsslider = (Slider) primaryStage.getScene().lookup("#rowsslider");
        rowslabel = (Label) primaryStage.getScene().lookup("#rowslabel");
        columnslider = (Slider) primaryStage.getScene().lookup("#columnslider");
        columnslabel = (Label) primaryStage.getScene().lookup("#columnslabel");
        square = (Rectangle) primaryStage.getScene().lookup("#square");
        triangle = (Polygon) primaryStage.getScene().lookup("#triangle");
        hexagon = (Polygon) primaryStage.getScene().lookup("#hexagon");
        adjacencycombobox = (ComboBox) primaryStage.getScene().lookup("#adjacencycombobox");


        //ACTIONS
        logoutbutton.setOnMouseClicked(e -> {
            try {
                logout();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        menubutton.setOnMouseClicked(e-> {
            try {
                returnmenu();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        rowsslider.setOnMouseDragged(e->updaterows());
        columnslider.setOnMouseDragged(e->updatecolumns());
        square.setOnMouseClicked(e->squareselected());
        square.setOnMouseEntered(e->squareenter());
        square.setOnMouseExited(e->squareexit());
        triangle.setOnMouseClicked(e->triangleselected());
        triangle.setOnMouseEntered(e->triangleenter());
        triangle.setOnMouseExited(e->triangleexit());
        hexagon.setOnMouseClicked(e->hexagonselected());
        hexagon.setOnMouseEntered(e->hexagonenter());
        hexagon.setOnMouseExited(e->hexagonexit());

        //INITIALIZE GUI
        username.setText(usern);
        adjacencycombobox.setItems(FXCollections.observableArrayList(
           new String("Costat"),
           new String("Costat i Angle")
        ));

    }

    private void logout() throws IOException {
        primaryStage.close();
        primaryStage = new Stage();
        Index i = new Index(primaryStage);
    }

    private void returnmenu() throws IOException {
        primaryStage.close();
        primaryStage = new Stage();
        Menu m = new Menu(usern,primaryStage);
    }

    private void updaterows() {
        double actual = rowsslider.getValue();
        rows = (int) actual;
        rowslabel.setText(String.valueOf(rows));
    }

    private void updatecolumns() {
        double actual = columnslider.getValue();
        columns = (int) actual;
        columnslabel.setText(String.valueOf(columns));
    }

    private void squareselected() {
        celltype = 'Q';
        square.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
        triangle.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        hexagon.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        adjacencycombobox.setItems(FXCollections.observableArrayList(
                new String("Costat"),
                new String("Costat i Angle")
        ));

    }

    private void squareenter() {
        if(celltype == null || celltype != 'Q') square.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
    }

    private void squareexit() {
        if(celltype == null || celltype != 'Q') square.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
    }

    private void triangleselected() {
        celltype = 'T';
        triangle.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
        square.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        hexagon.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        adjacencycombobox.setItems(FXCollections.observableArrayList(
                new String("Costat")
        ));
    }

    private void triangleenter() {
        if(celltype == null || celltype != 'T') triangle.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
    }

    private void triangleexit() {
        if(celltype == null || celltype != 'T') triangle.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
    }

    private void hexagonselected() {
        celltype = 'H';
        hexagon.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
        triangle.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        square.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
        adjacencycombobox.setItems(FXCollections.observableArrayList(
                new String("Costat")
        ));
    }

    private void hexagonenter() {
        if(celltype == null || celltype != 'H') hexagon.setFill(javafx.scene.paint.Color.valueOf("#0f9d58"));
    }

    private void hexagonexit() {
        if(celltype == null || celltype != 'H') hexagon.setFill(javafx.scene.paint.Color.valueOf("#757de8"));
    }
}
