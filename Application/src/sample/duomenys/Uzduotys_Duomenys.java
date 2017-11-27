package sample.duomenys;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class Uzduotys_Duomenys {

    private static final String UZDUOCIU_FILE = "uzduotys .xml";
    private static final String UZDUOTIS = "uzduotis";
    private static final String TRUMPAS = "trumpasAprasymas";
    private static final String DETALES = "detales";
    private static final String SKILTIS = "skiltis";


    private static Uzduotys_Duomenys objektas = new Uzduotys_Duomenys();
    private ObservableList<Uzduotis> uzduotys;

    public Uzduotys_Duomenys() {
        uzduotys = FXCollections.observableArrayList();
    }

    public static Uzduotys_Duomenys getObjektas() {
        return objektas;
    }

    //GET OBSERVABLE
    public ObservableList<Uzduotis> getUzduotys() {
        return uzduotys;
    }

    //ADD & REMOVE
    public void addUzduotis(Uzduotis uzduotis) {
        uzduotys.add(uzduotis);
    }

    public void removeUzduotis(Uzduotis uzduotis) {
        uzduotys.remove(uzduotis);
    }


    public void parsinamDoka() throws XMLStreamException, FileNotFoundException {

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream(UZDUOCIU_FILE);
        XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

        Uzduotis uzduotis = null;

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();

                if (startElement.getName().getLocalPart().equals(UZDUOTIS)) {
                    uzduotis = new Uzduotis();
                    continue;
                }
                if (startElement.getName().getLocalPart().equals(TRUMPAS)) {
                    event = eventReader.nextEvent();
                    uzduotis.setTrumpasAprasymas(event.asCharacters().getData());
                    continue;
                }
                if (startElement.getName().getLocalPart().equals(DETALES)) {
                    event = eventReader.nextEvent();
                    uzduotis.setDetales(event.asCharacters().getData());
                    continue;
                }
                if (startElement.getName().getLocalPart().equals(SKILTIS)) {
                    event = eventReader.nextEvent();
                    uzduotis.setSkiltis(event.asCharacters().getData());
                    continue;
                }
            }

            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals(UZDUOTIS)) {
                    uzduotys.add(uzduotis);
                }
            }
        }
    }

    public void issaugauUzduotys() {

        try {
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            XMLEventWriter eventWriter = outputFactory.
                    createXMLEventWriter(new FileOutputStream(UZDUOCIU_FILE));
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");

            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement uzduociuStartoElementas = eventFactory.createStartElement("", "",
                    "uzduotys");
            eventWriter.add(uzduociuStartoElementas);
            eventWriter.add(end);

            for (Uzduotis uzduotis : uzduotys) {
                saugauUzduoti(eventWriter, eventFactory, uzduotis);
            }

            eventWriter.add(eventFactory.createEndElement("", "",
                    "uzduotys"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    private void saugauUzduoti(XMLEventWriter eventWriter, XMLEventFactory eventFactory,
                               Uzduotis uzduotis) throws FileNotFoundException, XMLStreamException {
        XMLEvent end = eventFactory.createDTD("\n");
        StartElement configStartElement = eventFactory.createStartElement("",
                "", UZDUOTIS);
        eventWriter.add(configStartElement);
        eventWriter.add(end);

        nodosKurimas(eventWriter, TRUMPAS, uzduotis.getTrumpasAprasymas());
        nodosKurimas(eventWriter, DETALES, uzduotis.getDetales());
        nodosKurimas(eventWriter, SKILTIS, uzduotis.getSkiltis());

        eventWriter.add(eventFactory.createEndElement("", "", UZDUOTIS));
        eventWriter.add(end);
    }


    private void nodosKurimas(XMLEventWriter eventWriter, String name,
                              String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);

        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        EndElement endElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(endElement);
        eventWriter.add(end);
    }


}