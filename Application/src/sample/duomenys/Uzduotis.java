package sample.duomenys;

import javafx.beans.property.SimpleStringProperty;

public class Uzduotis {

    private SimpleStringProperty trumpasAprasymas = new SimpleStringProperty("");
    private SimpleStringProperty detales = new SimpleStringProperty("");
    private SimpleStringProperty skiltis = new SimpleStringProperty("");

    public Uzduotis() {

    }

    public Uzduotis(String trumpasAprasymas, String detales, String skiltis) {

        this.trumpasAprasymas.set(trumpasAprasymas);
        this.detales.set(detales);
        this.skiltis.set(skiltis);

    }

    public String getTrumpasAprasymas() {
        return String.valueOf(trumpasAprasymas.get());
    }

    public void setTrumpasAprasymas(String trumpasAprasymas) {
        this.trumpasAprasymas.set(trumpasAprasymas);
    }

    public SimpleStringProperty trumpasAprasymasProperty() {
        return trumpasAprasymas;
    }

    public String getDetales() {
        return detales.get();
    }

    public void setDetales(String detales) {
        this.detales.set(detales);
    }

    public SimpleStringProperty detalesProperty() {
        return detales;
    }

    public String getSkiltis() {
        return skiltis.get();
    }

    public SimpleStringProperty skiltisProperty() {
        return skiltis;
    }

    public void setSkiltis(String skiltis) {
        this.skiltis.set(skiltis);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "trumpasAprasymas =" + trumpasAprasymas +
                ", uzduotis =" + detales +
                '}';

    }

}