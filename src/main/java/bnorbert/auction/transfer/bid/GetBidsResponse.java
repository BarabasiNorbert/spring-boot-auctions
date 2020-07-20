package bnorbert.auction.transfer.bid;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;

public class GetBidsResponse {

    private Long id;
    private String email;
    private double amount;
    private Long userId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long homeId;
    private String neighborhood;
    private Integer kitchen;
    private Integer lotArea;
    private String yearBuilt;
    private Integer fullBath;
    private Integer bedroom;
    private String garageYearBuilt;
    private Integer garageCars;
    private Integer garageArea;
    private Double startingPrice;
    private String duration;
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Long getHomeId() {
        return homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Integer getKitchen() {
        return kitchen;
    }

    public void setKitchen(Integer kitchen) {
        this.kitchen = kitchen;
    }

    public Integer getLotArea() {
        return lotArea;
    }

    public void setLotArea(Integer lotArea) {
        this.lotArea = lotArea;
    }

    public String getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(String yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public Integer getFullBath() {
        return fullBath;
    }

    public void setFullBath(Integer fullBath) {
        this.fullBath = fullBath;
    }

    public Integer getBedroom() {
        return bedroom;
    }

    public void setBedroom(Integer bedroom) {
        this.bedroom = bedroom;
    }

    public String getGarageYearBuilt() {
        return garageYearBuilt;
    }

    public void setGarageYearBuilt(String garageYearBuilt) {
        this.garageYearBuilt = garageYearBuilt;
    }

    public Integer getGarageCars() {
        return garageCars;
    }

    public void setGarageCars(Integer garageCars) {
        this.garageCars = garageCars;
    }

    public Integer getGarageArea() {
        return garageArea;
    }

    public void setGarageArea(Integer garageArea) {
        this.garageArea = garageArea;
    }

    public Double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(Double startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
