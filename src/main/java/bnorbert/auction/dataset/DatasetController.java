package bnorbert.auction.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);
    private Homes homes;

    public DatasetController() {
        LOGGER.info("Reading data...");
        this.homes = new Homes("homes.txt");
    }

    public Homes getHomes() {
        return homes;
    }

    public void setHomes(Homes homes) {
        this.homes = homes;
    }
}
