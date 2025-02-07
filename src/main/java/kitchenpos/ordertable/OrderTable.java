package kitchenpos.ordertable;

import java.util.Objects;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofUnsaved(final int numberOfGuests, final boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void joinGroup(final Long tableGroupId) {
        checkEmpty();
        checkNotGrouped();
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
        this.empty = false;
    }

    public void acceptGuests(final int numberOfGuests) {
        checkMinGuests(numberOfGuests);
        checkNotEmpty();
        acceptGuests();
        this.numberOfGuests = numberOfGuests;
    }

    public void acceptGuests() {
        checkNotGrouped();
        this.empty = false;
    }

    public void clear() {
        checkNotGrouped();
        this.empty = true;
    }

    private void checkMinGuests(final int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotGrouped() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkEmpty() {
        if (!empty) {
            throw new IllegalArgumentException();
        }
    }

    private void checkNotEmpty() {
        if (empty) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public Optional<Long> getTableGroupId() {
        return Optional.ofNullable(tableGroupId);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    protected OrderTable() {
    }
}
