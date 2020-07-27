package bnorbert.auction.mapper;

import bnorbert.auction.domain.TimeSlot;
import bnorbert.auction.transfer.timeslot.TimeSlotsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TimeSlotMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "dayOfWeek", source = "timeSlot.dayOfWeek")
    @Mapping(target = "startTime", source = "timeSlot.startTime")
    @Mapping(target = "endTime", source = "timeSlot.endTime")
    public abstract TimeSlotsResponse mapToDto(TimeSlot timeSlot);

    public abstract List<TimeSlotsResponse> entitiesToEntityDTOs(List<TimeSlot> timeSlots);
}
