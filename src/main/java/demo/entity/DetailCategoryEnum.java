package demo.entity;

public enum DetailCategoryEnum {
    BODY(1, "Кузов"),
    ENGINE(2, "Двигатель"),
    TRANSMISSION(3, "Трансмиссия"),
    COOLING(4, "Охлаждение"),
    ELECTRICS(5, "Электрооборудование"),
    SUSPENSION(6, "Подвеска"),
    BREAK(7, "Тормоз"),
    WHEEL(8, "Колесо");

    private Integer code;
    private String value;

    DetailCategoryEnum(Integer code, String value) {
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

    public static DetailCategoryEnum byCode(Integer code) {
        switch (code) {
            case 1:
                return BODY;
            case 2:
                return ENGINE;
            case 3:
                return TRANSMISSION;
            case 4:
                return COOLING;
            case 5:
                return ELECTRICS;
            case 6:
                return SUSPENSION;
            case 7:
                return BREAK;
            case 8:
                return WHEEL;
        }

        return null;
    }
}
