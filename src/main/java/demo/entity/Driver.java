package demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "driver")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private LocalDate birthDate;

    private String passport;

    @Enumerated(value = EnumType.STRING)
    private DriverLicenseCategoryEnum driverLicenseCategory;

    private Integer experience;

    private Double balance;

    private CurrencyEnum currency;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Car> cars = new ArrayList<>();
}
