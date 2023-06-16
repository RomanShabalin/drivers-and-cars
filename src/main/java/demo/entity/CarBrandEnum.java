package demo.entity;

public enum CarBrandEnum {
    LADA("LADA", "Лада"),
    KIA("KIA", "Киа"),
    MERCEDES("MERCEDES", "Мерседес"),
    FORD("FORD", "Форд"),
    PORSCHE("PORSCHE", "Порше"),
    BMW("BMW", "БМВ"),
    TOYOTA("TOYOTA", "Тойота");

    private String brand;
    private String value;

    CarBrandEnum(String brand, String value) {
        this.brand = brand;
        this.value = value;
    }

    public String getBrand() {
        return brand;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static CarBrandEnum byBrand(String brand) {
        switch (brand) {
            case "LADA":
                return LADA;
            case "KIA":
                return KIA;
            case "MERCEDES":
                return MERCEDES;
            case "FORD":
                return FORD;
            case "PORSCHE":
                return PORSCHE;
            case "BMW":
                return BMW;
            case "TOYOTA":
                return TOYOTA;
        }

        return null;
    }
}
