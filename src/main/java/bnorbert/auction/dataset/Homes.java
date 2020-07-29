package bnorbert.auction.dataset;

import bnorbert.auction.datareader.HomeReader;
import bnorbert.auction.datareader.ReaderFactory;
import bnorbert.auction.domain.Home;

import java.util.HashMap;

public class Homes {

    private HashMap<Long, Home> theHomes = new HashMap<>();

    public Homes(String filename) {
        createHomes(filename);
    }

    private void createHomes(String filename) {
        ReaderFactory readerFactory = new ReaderFactory();
        HomeReader homeReader = (HomeReader) readerFactory.getReader("homes", filename);
        theHomes = homeReader.getTheHomes();
    }

    public HashMap<Long, Home> getHomes() {
        return theHomes;
    }


}
