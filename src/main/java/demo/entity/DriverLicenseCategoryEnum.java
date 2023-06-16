package demo.entity;

public enum DriverLicenseCategoryEnum {
    A("A", "Мотоциклы"),
    B("B", "Легковые автомобили"),
    C("C", "Грузовые автомобили"),
    D("D", "Автобусы"),
    M("M", "Мопеды"),
    T("T", "Трамваи/троллейбусы");

    private String category;
    private String value;

    DriverLicenseCategoryEnum(String category, String value) {
        this.category = category;
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static DriverLicenseCategoryEnum byCategory(String category) {
        switch (category) {
            case "A":
                return A;
            case "B":
                return B;
            case "C":
                return C;
            case "D":
                return D;
            case "M":
                return M;
            case "T":
                return T;
        }

        return null;
    }
}
