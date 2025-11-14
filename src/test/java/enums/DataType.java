package enums;

public enum DataType {
    CREATED("created"),
    TEST("test");

    private final String name;

    DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
