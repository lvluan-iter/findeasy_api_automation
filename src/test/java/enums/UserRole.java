package enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    OWNER("owner"),
    GUEST("guest"),
    ANONYMOUS("anonymous");

    private final String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

}
