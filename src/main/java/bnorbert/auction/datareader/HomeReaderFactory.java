package bnorbert.auction.datareader;

public class HomeReaderFactory extends ReaderFactory {

    public Reader getReader(String filename) {
        return new HomeReader(filename);
    }
}
