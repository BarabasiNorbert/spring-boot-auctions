package bnorbert.auction.transfer.bid;

public class BidDto {

    private Long timeSlotId;
    private long amount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Long getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(Long timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    @Override
    public String toString() {
        return "BidDto{" +
                "timeSlotId=" + timeSlotId +
                ", amount=" + amount +
                '}';
    }
}
