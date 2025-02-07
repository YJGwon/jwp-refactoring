package kitchenpos.menu.dto;

import kitchenpos.menu.MenuGroup;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.ofUnsaved(name);
    }

    public String getName() {
        return name;
    }

    private MenuGroupRequest() {
    }
}
