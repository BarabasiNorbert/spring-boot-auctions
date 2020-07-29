package bnorbert.auction.datareader;

public class ReaderFactory {

    public Reader getReader(String type, String filename) {

        if (!type.equalsIgnoreCase("homes")) {
            return null;
        }

       ReaderFactory rf = new HomeReaderFactory();
        return rf.getReader(filename);
    }


    public Reader getReader(String filename) {
        return null;
    }
}
