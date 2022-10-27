package kitchenpos.dto.request;

import kitchenpos.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    public String getName() {
        return name;
    }

    private MenuGroupRequest() {
    }
}
