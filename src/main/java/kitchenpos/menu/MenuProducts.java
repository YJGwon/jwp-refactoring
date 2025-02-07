package kitchenpos.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.common.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<MenuProduct> values;

    public MenuProducts(final List<MenuProduct> values) {
        this.values = new ArrayList<>(values);
    }

    public Price calculateTotalAmount() {
        final List<Price> amounts = values.stream()
                .map(MenuProduct::getAmount)
                .collect(Collectors.toList());
        return Price.sum(amounts);
    }

    public List<MenuProduct> getValues() {
        return Collections.unmodifiableList(values);
    }

    protected MenuProducts() {
    }
}
