package bnorbert.auction.datareader;


import bnorbert.auction.domain.Home;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class HomeReader implements Reader {

    private final HashMap<Long, Home> theHomes;

    public HomeReader(String filename) {
        theHomes = new HashMap<>();
        readFile(filename);
    }

    public HashMap<Long, Home> getTheHomes() {
        return theHomes;
    }

    @Override
    public void readFile(String file) {
        try (Scanner n = new Scanner(new File(file), "UTF-8")) {
            while (n.hasNextLine()) {
                String line = n.nextLine();
                if (line.length() > 0) {
                    createObjects(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createObjects(String homeInfo) {
        String[] info = homeInfo.split(";");

        if (!info[0].matches("-?(0|[1-9]\\d*)")) {
            return;
        }

        long id = Long.parseLong(info[0]);
        String neighborhood = info[1];
        int kitchen = Integer.parseInt(info[2]);
        int lotArea = Integer.parseInt(info[3]);
        String yearBuilt = info[4];
        int fullBath  = Integer.parseInt(info[5]);
        int bedroom  = Integer.parseInt(info[6]);
        String garageYearBuilt  = info[7];
        int garageCars  = Integer.parseInt(info[8]);
        int garageArea  = Integer.parseInt(info[9]);
        double startingPrice = Double.parseDouble(info[10]);

        if (!theHomes.containsKey(id)) {
            Home newHome = new Home(id, neighborhood, kitchen, lotArea, yearBuilt,fullBath, bedroom, garageYearBuilt, garageCars, garageArea, startingPrice);
            theHomes.put(id, newHome);
        }
    }

}
