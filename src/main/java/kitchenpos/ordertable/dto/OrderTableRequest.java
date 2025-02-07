package kitchenpos.ordertable.dto;

import kitchenpos.ordertable.OrderTable;

public class OrderTableRequest {

    private int numberOfGuests;
    private boolean empty;

    public OrderTableRequest(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return OrderTable.ofUnsaved(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    private OrderTableRequest() {
    }
}
