package kitchenpos.order;

public enum OrderStatus {

    COOKING, MEAL, COMPLETION;

    public boolean isNotChangeable() {
        return this.equals(COMPLETION);
    }
}
