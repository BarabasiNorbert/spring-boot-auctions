package bnorbert.auction.mapper;

import bnorbert.auction.domain.Home;
import bnorbert.auction.transfer.home.HomeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class HomeMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "timeSlotId", source = "timeSlot.id")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "dayOfWeek", source = "timeSlot.dayOfWeek")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
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
    public abstract HomeResponse mapToHomeResponse(Home home);

    public abstract List<HomeResponse> entitiesToEntityDTOs(List<Home> homes);
}
