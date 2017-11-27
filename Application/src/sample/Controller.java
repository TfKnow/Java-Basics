package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import sample.duomenys.Uzduotis;
import sample.duomenys.Uzduotys_Duomenys;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane pagrindas;

    @FXML
    private List<Uzduotis> uzduotys;

    @FXML
    private TableView<Uzduotis> lentele;

    @FXML
    private TextArea detales;

    @FXML
    private Label terminas;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private TextField filterLaukas;

    private ObservableList<Uzduotis> mockingDuomenys = FXCollections.observableArrayList();

    private Uzduotys_Duomenys duomenys;


    public void initialize() throws FileNotFoundException, XMLStreamException {

        //Sukuriam nauja duomenu objekta (xml elementai, nodos, add/remove, observablelist)...
        duomenys = new Uzduotys_Duomenys();

        //SAX parseris analizuoja dokumentą "uzduotys .xml" ir visus perdirbtus duomenys perkelia į observable list'ą
        duomenys.parsinamDoka();

        //Lenta TableView su vienu cellvaluefactory trumpasAprasymas, metodas 'paspaudziau' teksto area ribose išmeta elemento detales...
        lentele.setItems(duomenys.getUzduotys());

        //kontekstinė meniuškė užsetinta ant tableviewo su tokiomis pat funkcijomis esančiomis toolbar'e
        contextMenu = new ContextMenu();
        MenuItem createMenuItem = new MenuItem("New");
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem exitApp = new MenuItem("Exit");


        //metodu pririsimas prie konteksto meniu id Su liambda
        createMenuItem.setOnAction(event -> pridetiNaujaUzduoti());
        deleteMenuItem.setOnAction(event -> istrintiUzduoti());
        editMenuItem.setOnAction(event -> modifikavimoLangas());
        exitApp.setOnAction(event -> Platform.exit());


        contextMenu.getItems().addAll(createMenuItem, editMenuItem, deleteMenuItem, exitApp);
        lentele.setContextMenu(contextMenu);


        //Bandom filtruot table
        mockingDuomenys = duomenys.getUzduotys();

        FilteredList<Uzduotis> filtruotas = new FilteredList<Uzduotis>(mockingDuomenys, p -> true);
        filterLaukas.textProperty().addListener((observable, oldValue, newValue) -> {

            filtruotas.setPredicate(uzduotis -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //new value = reiksme egzistuojanti tekstfielde
                String lowerCaseFilter = newValue.toLowerCase();
                if (uzduotis.getTrumpasAprasymas().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                if (uzduotis.getSkiltis().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });
        });

        //Apvyniojam filtruotą sąrašą į Sortiruotą sąrašą (FilteredList neįmanoma pavaizduot...)
        SortedList<Uzduotis> sortiruotiDuomenys = new SortedList<Uzduotis>(filtruotas);
        sortiruotiDuomenys.comparatorProperty().bind(lentele.comparatorProperty());
        lentele.setItems(sortiruotiDuomenys);

    }


//        sarasoPerziura.setItems(duomenys.getUzduotys());

////        Uzduotis uzduotis = new Uzduotis("wdw", "wdwfd", LocalDate.of(1996, 05, 23));
//        uzduotys = new ArrayList<Uzduotis>();
//        uzduotys.add(uzduotis);
//        sarasoPerziura.getItems().setAll(uzduotys);
//        sarasoPerziura.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


    public void paspaudziau() {
        Uzduotis uzduotis = lentele.getSelectionModel().getSelectedItem();
//        System.out.println("Pasirinkta užduotis yra " + uzduotis);
        StringBuilder stringBuilder = new StringBuilder(uzduotis.getDetales());
        detales.setText(stringBuilder.toString());
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        terminas.setText("SKYRIUS: " + uzduotis.getSkiltis());
    }

    public void uzdarauAp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit? \nIf you're sure, press OK Button");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Shutting down the application...");
            Platform.exit();
        }
    }

    public void pridetiNaujaUzduoti() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pagrindas.getScene().getWindow());
        dialog.setTitle("Sukurti naują užduotį");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogas.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> pasirinkimas = dialog.showAndWait();

        if (pasirinkimas.isPresent() && pasirinkimas.get() == ButtonType.OK) {
            DialogController dialogController = fxmlLoader.getController();
            Uzduotis uzduotis = dialogController.apdorotiDuomenys();
            duomenys.addUzduotis(uzduotis);
            duomenys.issaugauUzduotys();
            lentele.getSelectionModel().select(uzduotis);
        }
    }

    public void istrintiUzduoti() {

        Uzduotis uzduotis = lentele.getSelectionModel().getSelectedItem();
        if (uzduotis == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Užduotis nepasirinkta");
            alert.setHeaderText(null);
            alert.setContentText("Prašau pasirinkti sąrašo elementą, kurį norite ištrinti");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Ištrinti užduotį");
        alert.setHeaderText(null);
        alert.setContentText("Ištrinti užduotį: " + uzduotis.getTrumpasAprasymas()
                + "\nAr esate tikras ? OK - taip, CANCEL - ne");

        Optional<ButtonType> rezultatas = alert.showAndWait();

        if (rezultatas.isPresent() && (rezultatas.get() == ButtonType.OK)) {
            duomenys.removeUzduotis(uzduotis);

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Užduotis pašalinta");
            alert1.setHeaderText(null);
            alert1.setContentText("Užduotis" + uzduotis.getTrumpasAprasymas() +
                    " buvo sėkmingai ištrinta");
            duomenys.issaugauUzduotys();
        }

    }

    @FXML
    public void modifikavimoLangas() {
        Uzduotis pasirinktaUzduotis = lentele.getSelectionModel().getSelectedItem();
        if (pasirinktaUzduotis == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nepasirinkote");
            alert.setHeaderText(null);
            alert.setContentText("Prašome pasirinkti užduotį");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pagrindas.getScene().getWindow());
        dialog.setTitle("Modifikacija");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogas.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Negalėjome užkrauti");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogController dialogController = fxmlLoader.getController();
        dialogController.modifikuotiUzduoti(pasirinktaUzduotis);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dialogController.updeitas(pasirinktaUzduotis);
            duomenys.issaugauUzduotys();
        }
    }

}
