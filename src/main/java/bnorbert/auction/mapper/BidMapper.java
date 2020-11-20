package bnorbert.auction.mapper;

import bnorbert.auction.domain.Bid;
import bnorbert.auction.domain.Home;
import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.domain.User;
import bnorbert.auction.transfer.bid.BidDto;
import bnorbert.auction.transfer.bid.BidResponse;
import bnorbert.auction.transfer.bid.GetBidsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BidMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amount", source = "bidDto.amount")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "timeSlot", source = "timeSlot")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "home", source = "home")
    public abstract Bid map(BidDto bidDto, TimeSlot timeSlot, User user, Home home);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "bid.amount")
    @Mapping(target = "timeSlotId", source = "timeSlot.id")
    @Mapping(target = "dayOfWeek", source = "timeSlot.dayOfWeek")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "homeId", source = "home.id")
    @Mapping(target = "neighborhood", source = "home.neighborhood")
    @Mapping(target = "kitchen", source = "home.kitchen")
    @Mapping(target = "lotArea", source = "home.lotArea")
    @Mapping(target = "yearBuilt", source = "home.yearBuilt")
    @Mapping(target = "fullBath", source = "home.fullBath")
    @Mapping(target = "bedroom", source = "home.bedroom")
    @Mapping(target = "garageYearBuilt", source = "home.garageYearBuilt")
    @Mapping(target = "garageCars", source = "home.garageCars")
    @Mapping(target = "garageArea", source = "home.garageArea")
    @Mapping(target = "startingPrice", source = "home.startingPrice")
    @Mapping(target = "createdDate", source = "bid.createdDate")
    public abstract BidResponse mapToBidResponse(Bid bid);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "bid.amount")
    @Mapping(target = "dayOfWeek", source = "timeSlot.dayOfWeek")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "homeId", source = "home.id")
    @Mapping(target = "neighborhood", source = "home.neighborhood")
    @Mapping(target = "kitchen", source = "home.kitchen")
    @Mapping(target = "lotArea", source = "home.lotArea")
    @Mapping(target = "yearBuilt", source = "home.yearBuilt")
    @Mapping(target = "fullBath", source = "home.fullBath")
    @Mapping(target = "bedroom", source = "home.bedroom")
    @Mapping(target = "garageYearBuilt", source = "home.garageYearBuilt")
    @Mapping(target = "garageCars", source = "home.garageCars")
    @Mapping(target = "garageArea", source = "home.garageArea")
    @Mapping(target = "startingPrice", source = "home.startingPrice")
    @Mapping(target = "createdDate", source = "bid.createdDate")
    //@Mapping(target = "duration", expression = "java(getDuration(bid))")
    public abstract GetBidsResponse mapToBidsResponse(Bid bid);

    //String getDuration(Bid bid) {
    //    return TimeAgo.using(bid.getCreatedDate().toEpochMilli());
    //}

    public abstract List<GetBidsResponse> entitiesToEntityDTOs(List<Bid> bids);

}
