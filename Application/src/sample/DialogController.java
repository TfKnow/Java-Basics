package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.duomenys.Uzduotis;


public class DialogController {

    @FXML
    private TextArea detalesLaukas;

    @FXML
    private TextField trumpasAprasymasLaukas;

    @FXML
    private TextField skiltisLaukas;


    public Uzduotis apdorotiDuomenys() {

        if (trumpasAprasymasLaukas.getText() == null || detalesLaukas.getText() == null ||
                skiltisLaukas.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Tuščias laukas");
            alert.setHeaderText(null);
            alert.setContentText("Prašome užpildyti visus langus");
            alert.showAndWait();
            trumpasAprasymasLaukas.setText("x");
            detalesLaukas.setText("x");

//           terminasLaukas
        }

        String trumpasAprasymas = trumpasAprasymasLaukas.getText().trim();
        String detales = detalesLaukas.getText().trim();
        String skiltis = String.valueOf(skiltisLaukas.getText());

        Uzduotis uzduotis = new Uzduotis(trumpasAprasymas, detales, skiltis);

        return uzduotis;
    }

    public void modifikuotiUzduoti(Uzduotis uzduotis) {

        trumpasAprasymasLaukas.setText(uzduotis.getTrumpasAprasymas());
        detalesLaukas.setText(uzduotis.getDetales());
        skiltisLaukas.setText(uzduotis.getSkiltis());

    }

    public void updeitas(Uzduotis uzduotis) {

        if (trumpasAprasymasLaukas.getText() == null || detalesLaukas.getText() == null ||
                skiltisLaukas.getText() == null) {
            Alert alertas = new Alert(Alert.AlertType.INFORMATION);
            alertas.setTitle("Tuščias laukas");
            alertas.setHeaderText(null);
            alertas.setContentText("Prašome užpildyti visus laukelius");
            alertas.show();
            return;
        }

        uzduotis.setTrumpasAprasymas(trumpasAprasymasLaukas.getText());
        uzduotis.setDetales(detalesLaukas.getText());
        uzduotis.setSkiltis(skiltisLaukas.getText().toString());
    }
//        String shortDescription = shortDescriptionField.getText().trim();
//        String details = detailsArea.getText().trim();
//        LocalDate deadLineValue = deadlinePicker.getValue();
//
//        ToDoItem newItem = new ToDoItem(shortDescription, details, deadLineValue);
//        ToDoData.getInstance().addToDoItem(newItem);
//        return newItem;
}

