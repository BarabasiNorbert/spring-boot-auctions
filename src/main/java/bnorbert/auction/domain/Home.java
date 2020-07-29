package bnorbert.auction.domain;

import javax.persistence.*;

@Entity
@Table(name = "homes")
//#foreclosedhomes
public class Home {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String neighborhood;
    private int kitchen;
    private int lotArea;
    private String yearBuilt;
    private int fullBath;
    private int bedroom;
    private String garageYearBuilt;
    private int garageCars;
    private int garageArea;
    private double startingPrice;

    public Home() {
    }

    public Home(long id, String neighborhood, int kitchen, int lotArea, String yearBuilt, int fullBath,
                int bedroom, String garageYearBuilt, int garageCars, int garageArea, double startingPrice) {
        this.id = id;
        this.neighborhood = neighborhood;
        this.kitchen = kitchen;
        this.lotArea = lotArea;
        this.yearBuilt = yearBuilt;
        this.fullBath = fullBath;
        this.bedroom = bedroom;
        this.garageYearBuilt = garageYearBuilt;
        this.garageCars = garageCars;
        this.garageArea = garageArea;
        this.startingPrice = startingPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public int getKitchen() {
        return kitchen;
    }

    public void setKitchen(int kitchen) {
        this.kitchen = kitchen;
    }

    public int getLotArea() {
        return lotArea;
    }

    public void setLotArea(int lotArea) {
        this.lotArea = lotArea;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public int getFullBath() {
        return fullBath;
    }

    public void setFullBath(int fullBath) {
        this.fullBath = fullBath;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public String getGarageYearBuilt() {
        return garageYearBuilt;
    }

    public void setGarageYearBuilt(String garageYearBuilt) {
        this.garageYearBuilt = garageYearBuilt;
    }

    public int getGarageCars() {
        return garageCars;
    }

    public void setGarageCars(int garageCars) {
        this.garageCars = garageCars;
    }

    public int getGarageArea() {
        return garageArea;
    }

    public void setGarageArea(int garageArea) {
        this.garageArea = garageArea;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice = startingPrice;
    }
}
