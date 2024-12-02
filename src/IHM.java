import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;


public class IHM extends Application {

    String fileName = "B3/csv/data.csv";
    String fileNameCor = "B3/csv/transport_data.csv";
    TextField tVille1 = new TextField("Ville 1");
    TextField tVille2 = new TextField("Ville 2");
    CheckBox transportAvion = new CheckBox("" + ModaliteTransport.AVION);
    CheckBox transportBus = new CheckBox("" + ModaliteTransport.BUS + "     ");
    CheckBox transportTrain = new CheckBox("" + ModaliteTransport.TRAIN + " ");
    ComboBox<TypeCout> critère = new ComboBox<>();
    MenuItem menuItem1 = new MenuItem("Copier");
    MenuItem menuItem2 = new MenuItem("Coller");
    MenuItem menuItem3 = new MenuItem("Fermer");
    ListView<String> list = new ListView<String>();
    BorderPane border = new BorderPane();
    Slider prixSlider = new Slider();
    Slider co2Slider = new Slider();
    Slider tempsSlider = new Slider();
    TextField prixLabelSlid = new TextField();
    TextField co2LabelSlid = new TextField();
    TextField tempsLabelSlid = new TextField();
    HBox prixHBox = new HBox();
    HBox co2HBox = new HBox();
    HBox tempsHBox = new HBox();
    VBox main = new VBox();
    VBox inMain = new VBox();
    VBox sliderBox = new VBox();
    Button recherche = new Button("Rechercher");
    Map<TypeCout, Double> minCout = new HashMap<>();
    Map<TypeCout, Double> maxCout = new HashMap<>();

    
    public void start(Stage stage) {
        
        class RechercheHandler implements EventHandler<ActionEvent> {
            public void handle(ActionEvent event) {
                border.setBottom(null);
                ModaliteTransport m1 = null;
                ModaliteTransport m2 = null;
                ModaliteTransport m3 = null;
                if (transportAvion.isSelected()) {
                    m1 = ModaliteTransport.valueOf(transportAvion.getText().trim());
                }
                if (transportBus.isSelected()) {
                    m2 = ModaliteTransport.valueOf(transportBus.getText().trim());
                }
                if (transportTrain.isSelected()) {
                    m3 = ModaliteTransport.valueOf(transportTrain.getText().trim());
                }
                Voyageur v = new Voyageur(m1, m2, m3, critère.getValue());
                if(main.getChildren().contains(sliderBox)){   
                    v = new Voyageur(m1, m2, m3, critère.getValue(), tempsSlider.getValue(), prixSlider.getValue(), co2Slider.getValue());
                }
                Plateforme p = new Plateforme(v);
                p.launch(fileName, tVille1.getText(), tVille2.getText(), fileNameCor);
                List<List<String>> listRes = p.getResultatChemin();
                if(!listRes.isEmpty()){
                    list.getItems().clear();
                    for(int i = 0; i < listRes.size(); i++){
                        list.getItems().addAll(listRes.get(i).toString());
                    }
                    list.setPrefWidth(stage.getWidth()*0.60);
                    border.setLeft(list);
                    minCout = p.getMinCout();
                    maxCout = p.getMaxCout();
                    prixSlider.setMin(minCout.get(TypeCout.PRIX)); prixSlider.setMax(maxCout.get(TypeCout.PRIX));
                    co2Slider.setMin(minCout.get(TypeCout.CO2)); co2Slider.setMax(maxCout.get(TypeCout.CO2));
                    tempsSlider.setMin(minCout.get(TypeCout.TEMPS)); tempsSlider.setMax(maxCout.get(TypeCout.TEMPS));
                    co2Slider.setMajorTickUnit((co2Slider.getMax()-co2Slider.getMin())/4);
                    prixSlider.setMajorTickUnit((prixSlider.getMax()-prixSlider.getMin())/4);
                    tempsSlider.setMajorTickUnit((tempsSlider.getMax()-tempsSlider.getMin())/4);
                    if(!main.getChildren().contains(sliderBox)){
                        prixSlider.setValue(prixSlider.getMax());co2Slider.setValue(co2Slider.getMax());tempsSlider.setValue(tempsSlider.getMax());
                        prixLabelSlid.setText(""+prixSlider.getMax());co2LabelSlid.setText(""+co2Slider.getMax());tempsLabelSlid.setText(""+tempsSlider.getMax());
                        main.getChildren().addAll(sliderBox);
                    } 
                }else {
                    Label l = new Label("Aucun resultat");
                    VBox vTemp = new VBox();
                    vTemp.getChildren().add(l);
                    border.setLeft(vTemp);
                    vTemp.setAlignment(Pos.CENTER);
                    vTemp.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
                    vTemp.setPrefWidth(400);
                }
            }
        }
        class MonRenduDeCellule extends ListCell<String> {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    // Créer un Canvas pour dessiner la cellule
                    int i = 1;
                    int y = 20;
                    int x = 110;
                    Canvas canvas = new Canvas(list.getWidth(), 100); // Ajuster la taille si nécessaire
                    GraphicsContext gc = canvas.getGraphicsContext2D();

                    // Dessiner le contenu sur le canvas
                    String[] details = item.split(", ");
                    details[details.length-1] = details[details.length-1].substring(0,details[details.length-1].length()-2);
                    gc.fillText(details[0].substring(1), 10, y);
                    while (i < details.length - 6){
                        gc.fillText(details[i], x, y);
                        x+= 100;
                        if(x >canvas.getWidth()){
                            y=y+20;
                            x=10;
                        }
                        i += 1;
                    }
                    gc.fillText(details[i+1],10,140*i+1);
                    gc.fillText(details[details.length-6].toUpperCase() + " : " + details[details.length - 5], 10, y+20);
                    gc.fillText(details[details.length-4].toUpperCase() + " : " + details[details.length - 3], 170, y+20);
                    gc.fillText(details[details.length-2].toUpperCase() + " : " + details[details.length-1], 330, y+20);
                    Double co2 = 0.0;
                    if(details[details.length-6].toUpperCase().equals(TypeCout.CO2.toString())) co2 = Double.parseDouble(details[details.length - 5]);
                    if(details[details.length-4].toUpperCase().equals(TypeCout.CO2.toString())) co2 = Double.parseDouble(details[details.length - 3]);
                    if(details[details.length-2].toUpperCase().equals(TypeCout.CO2.toString())) co2 = Double.parseDouble(details[details.length - 1]);
                    if ( co2 <= (minCout.get(TypeCout.CO2)*2)){
                        System.out.println("Coucou");
                        gc.setFill(Color.GREEN);
                        gc.fillOval(10,y+25,15,15);
                        gc.setFill(Color.BLACK);
                        
                    }
                    setGraphic(canvas);
                } else {
                    setGraphic(null);
                }
            }
        }


        //Liste des resultats
        list.setCellFactory((Callback<ListView<String>, ListCell<String>>) new Callback<ListView<String>,
          ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new MonRenduDeCellule();
            }
          }
        );

        //Hbox top
        HBox top = new HBox();
        Menu outils = new Menu("Outils");
        MenuBar outilsBar = new MenuBar();
        outils.getItems().addAll(menuItem1, menuItem2, menuItem3);
        outilsBar.getMenus().addAll(outils);
        top.getChildren().add(outilsBar);
        menuItem3.setOnAction(event -> stage.close());
        menuItem2.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            if (clipboard.hasString()) {
                tVille2.setText(clipboard.getString());
            }
        });
        menuItem1.setOnAction(event -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(tVille1.getText());
            clipboard.setContent(content);
        });
        
        //style Hbox top
        top.getStyleClass().add("top-hbox");
        outilsBar.getStyleClass().add("outilsBar");

        HBox ville1 = new HBox();
        final Label lV1 = new Label("De :");
        HBox ville2 = new HBox();
        final Label lV2 = new Label("A :");

        // CheckBox
        VBox checkBoxes = new VBox();
        checkBoxes.getChildren().addAll(transportAvion, transportBus, transportTrain);
        checkBoxes.setAlignment(Pos.CENTER);
        checkBoxes.setSpacing(10);

        // ComboBox
        for (int i = 0; i < TypeCout.values().length; i++) {
            critère.getItems().add(TypeCout.values()[i]);
        }
        recherche.setOnAction(new RechercheHandler());

        //Remplissage de la VBox Main
        ville1.getChildren().addAll(lV1, tVille1);
        ville2.getChildren().addAll(lV2, tVille2);
        ville1.setAlignment(Pos.CENTER);
        ville2.setAlignment(Pos.CENTER);

        //Sliders

        Label labelPrixSlid = new Label("Prix :");
        prixSlider.setShowTickMarks(false);
        prixSlider.setShowTickLabels(true);
        prixSlider.setOnMouseDragged( e -> {
            prixLabelSlid.setText(""+(Math.ceil(prixSlider.getValue()*100)/100));
        });
        prixLabelSlid.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                try {
                    Double val = Double.parseDouble(newValue);
                    prixLabelSlid.setText(""+val);
                    prixSlider.setValue(val);
                }catch(NumberFormatException e){
                    prixLabelSlid.setText(oldValue);
                }
            }
        });
        Label labelCo2Slid = new Label("Co2 :");
        co2Slider.setShowTickMarks(false);
        co2Slider.setShowTickLabels(true);
        co2Slider.setOnMouseDragged(e ->{
            co2LabelSlid.setText(""+(Math.ceil(co2Slider.getValue()*1000)/1000));
        });
        co2LabelSlid.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                try {
                    Double val = Double.parseDouble(newValue);
                    co2LabelSlid.setText(""+val);
                    co2Slider.setValue(val);
                }catch(NumberFormatException e){
                    co2LabelSlid.setText(oldValue);
                }
            }
        });
        Label labelTempsSlid = new Label("Durée :");
        tempsSlider.setShowTickMarks(false);
        tempsSlider.setShowTickLabels(true);
        tempsSlider.setOnMouseDragged(e ->{
            tempsLabelSlid.setText(""+(Math.ceil(tempsSlider.getValue()*100)/100));
        });
        tempsLabelSlid.textProperty().addListener((ChangeListener<? super String>) new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                try {
                    Double val = Double.parseDouble(newValue);
                    tempsLabelSlid.setText(""+val);
                    tempsSlider.setValue(val);
                }catch(NumberFormatException e){
                    tempsLabelSlid.setText(oldValue);
                }
            }
        });
        prixHBox.getChildren().addAll(labelPrixSlid,prixLabelSlid);
        co2HBox.getChildren().addAll(labelCo2Slid,co2LabelSlid);
        tempsHBox.getChildren().addAll(labelTempsSlid,tempsLabelSlid);

        sliderBox.getChildren().addAll(prixHBox,prixSlider,co2HBox,co2Slider,tempsHBox,tempsSlider);
        sliderBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.setMinSize(300, 300);
        sliderBox.setMaxSize(300, 300);

        main.setMinHeight(600);
        main.setMinWidth(400);
        main.getChildren().addAll(inMain);
        main.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        main.setAlignment(Pos.CENTER);
        inMain.getChildren().addAll(ville1, ville2, checkBoxes, critère, recherche);
        inMain.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        inMain.setAlignment(Pos.CENTER);
        inMain.setMinSize(300, 300);

        //css InMain
        inMain.getStyleClass().add("inMain");
        //css sliderBox
        sliderBox.getStyleClass().add("sliderBox");

        /*StackPane imagePane = new StackPane();
        Image image1 = new Image("file:B3/pics/landscape1.jpg");
        Image image2 = new Image("file:B3/pics/landscape2.jpg");
        Image image3 = new Image("file:B3/pics/landscape3.jpg");

        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);

        imageView1.setFitHeight(250);
        imageView1.setFitWidth(300);
        imageView2.setFitHeight(300); 
        imageView2.setFitWidth(300);
        imageView3.setFitHeight(300);
        imageView3.setFitWidth(300);

        //imageView1.setPreserveRatio(true);
        //imageView2.setPreserveRatio(true);
        //imageView3.setPreserveRatio(true);
        //(top,right,bottom,left)
        StackPane.setMargin(imageView1, new Insets(100, 300, 0, 20)); // Image1 
        StackPane.setMargin(imageView2, new Insets(50, 150, 60, 150)); // Image2 
        StackPane.setMargin(imageView3, new Insets(0, 50, 50, 0)); // Image3 

        imageView1.getStyleClass().add("image-view");
        imageView2.getStyleClass().add("image-view");
        imageView3.getStyleClass().add("image-view");

        //Sta

        imagePane.getChildren().addAll(imageView1, imageView2, imageView3);
        imagePane.getStyleClass().add("image-container");*/
        
        border.setTop(top);
        border.setCenter(main);
        border.getStyleClass().add("background-image");
        border.getStylesheets().add("Style.css");

        Scene scene = new Scene(border, 400, 400);
        stage.setTitle("FlyingDustries");
        stage.getIcons().add(new Image("logo.png"));
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }
    

    public static void main(String[] args) {
        Application.launch(args);
    }
}
