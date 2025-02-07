package kitchenpos.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup ofUnsaved(final String name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected MenuGroup() {
    }
}
