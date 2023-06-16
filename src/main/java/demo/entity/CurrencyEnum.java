package demo.entity;

public enum CurrencyEnum {
    NONE(0, "Отсутствует"),
    RED(1, "Красный доллар"),
    GREEN(2, "Зеленый доллар"),
    BLUE(3, "Синий доллар");

    private Integer code;
    private String value;

    CurrencyEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static CurrencyEnum byCode(Integer code) {
        switch (code) {
            case 0:
                return NONE;
            case 1:
                return RED;
            case 2:
                return GREEN;
            case 3:
                return BLUE;
        }

        return null;
    }
}
