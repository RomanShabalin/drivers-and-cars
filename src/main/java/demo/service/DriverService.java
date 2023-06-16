package demo.service;

import demo.entity.CurrencyEnum;
import demo.entity.Driver;
import demo.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Optional<Driver> findById(Integer id) {
        return driverRepository.findById(id);
    }

    public List<Driver> findByName(String name) {
        return driverRepository.findByName(name);
    }

    public List<Driver> findByPassport(String passport) {
        return driverRepository.findByPassport(passport);
    }

    public List<Driver> findAll() {
        return driverRepository.findAll();
    }

    public List<Driver> driverGetAllWithLimitAndOffset(Integer limit, Integer offset) {
        return driverRepository.driverGetAllWithLimitAndOffset(limit, offset);
    }

    public boolean save(Driver driver) {
        if (driver == null ||
                driver.getFullName() == null || driver.getFullName().trim().isEmpty() ||
                driver.getBirthDate() == null || driver.getBirthDate().toString().trim().isEmpty() ||
                driver.getPassport() == null || driver.getPassport().trim().isEmpty() ||
                driver.getDriverLicenseCategory() == null || driver.getDriverLicenseCategory().getValue().trim().isEmpty() ||
                driver.getExperience() == null || driver.getExperience().toString().trim().isEmpty()) {
            log.error("Ошибка сохранения водителя");
            return false;
        }

        driverRepository.save(driver);
        log.info("Водитель успешно сохранен");
        return true;
    }

    public boolean update(Driver driver, Integer id) {
        if (driverRepository.existsById(id)) {
            Optional<Driver> existsDriver = driverRepository.findById(id);
            Driver updatedDriver = new Driver();

            updatedDriver.setId(id);

            if (driver.getFullName() == null || driver.getFullName().trim().isEmpty() || driver.getFullName().equals(existsDriver.get().getFullName())) {
                updatedDriver.setFullName(existsDriver.get().getFullName());
            } else {
                updatedDriver.setFullName(driver.getFullName());
            }

            if (driver.getBirthDate() == null || driver.getBirthDate().toString().trim().isEmpty() || driver.getBirthDate().equals(existsDriver.get().getBirthDate())) {
                updatedDriver.setBirthDate(existsDriver.get().getBirthDate());
            } else {
                updatedDriver.setBirthDate(driver.getBirthDate());
            }

            if (driver.getPassport() == null || driver.getPassport().trim().isEmpty() || driver.getPassport().equals(existsDriver.get().getPassport())) {
                updatedDriver.setPassport(existsDriver.get().getPassport());
            } else {
                updatedDriver.setPassport(driver.getPassport());
            }

            if (driver.getDriverLicenseCategory() == null || driver.getDriverLicenseCategory().getValue().trim().isEmpty() || driver.getDriverLicenseCategory().equals(existsDriver.get().getDriverLicenseCategory())) {
                updatedDriver.setDriverLicenseCategory(existsDriver.get().getDriverLicenseCategory());
            } else {
                updatedDriver.setDriverLicenseCategory(driver.getDriverLicenseCategory());
            }

            if (driver.getExperience() == null || driver.getExperience().toString().trim().isEmpty() || driver.getExperience().equals(existsDriver.get().getExperience())) {
                updatedDriver.setExperience(existsDriver.get().getExperience());
            } else {
                updatedDriver.setExperience(driver.getExperience());
            }

            driverRepository.save(updatedDriver);
            log.info("Водитель успешно изменен");
            return true;
        }

        log.error("Ошибка изменения водителя");
        return false;
    }

    public boolean updateBalance(Integer driverId, Double balance, Integer currencyId, boolean isAdding) {
        if (driverRepository.existsById(driverId) && CurrencyEnum.byCode(currencyId) != null && currencyId != 0) {
            Optional<Driver> driver = driverRepository.findById(driverId);
            double newBalance = 0.0;

            if (isAdding) {
                if (driver.get().getCurrency().equals(CurrencyEnum.NONE)) {
                    driverRepository.updateCurrency(driverId, currencyId);
                    driver.get().setCurrency(CurrencyEnum.byCode(currencyId));
                }

                if (currencyId.equals(driver.get().getCurrency().getCode())) {
                    newBalance = driver.get().getBalance() + balance;
                } else {
                    switch (currencyId) {
                        case 1:
                            if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                                newBalance = driver.get().getBalance() + (balance * 2.5);
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                                newBalance = driver.get().getBalance() + (balance * (2.5 * 0.6));
                            }

                            break;
                        case 2:
                            if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                                newBalance = driver.get().getBalance() + (balance / 2.5);
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                                newBalance = driver.get().getBalance() + (balance * 0.6);
                            }

                            break;
                        case 3:
                            if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                                newBalance = driver.get().getBalance() + (balance / (2.5 * 0.6));
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                                newBalance = driver.get().getBalance() + (balance / 0.6);
                            }

                            break;
                    }
                }
            } else {
                if (currencyId.equals(driver.get().getCurrency().getCode())) {
                    if (balance > driver.get().getBalance()) {
                        log.info("Ошибка списания баланса: баланс водителя меньше суммы списания");
                        return false;
                    }

                    newBalance = driver.get().getBalance() - balance;
                } else {
                    switch (currencyId) {
                        case 1:
                            if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                                newBalance = driver.get().getBalance() - (balance * 2.5);
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                                newBalance = driver.get().getBalance() - (balance * (2.5 * 0.6));
                            }

                            break;
                        case 2:
                            if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                                newBalance = driver.get().getBalance() - (balance / 2.5);
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                                newBalance = driver.get().getBalance() - (balance * 0.6);
                            }

                            break;
                        case 3:
                            if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                                newBalance = driver.get().getBalance() - (balance / (2.5 * 0.6));
                            }

                            if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                                newBalance = driver.get().getBalance() - (balance / 0.6);
                            }

                            break;
                    }

                    if (newBalance < 0.0) {
                        log.info("Ошибка списания баланса: баланс водителя меньше суммы списания");
                        return false;
                    }
                }
            }

            driverRepository.updateBalance(driverId, newBalance);

            log.info("Баланс успешно изменен");
            return true;
        }

        log.error("Ошибка изменения баланса");
        return false;
    }

    public Double getBalance(Integer driverId, Integer currencyId) {
        if (driverRepository.existsById(driverId) && CurrencyEnum.byCode(currencyId) != null && currencyId != 0) {
            Optional<Driver> driver = driverRepository.findById(driverId);

            if (currencyId.equals(driver.get().getCurrency().getCode())) {
                return driver.get().getBalance();
            } else {
                double newBalance = 0.0;

                switch (currencyId) {
                    case 1:
                        if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                            newBalance = driver.get().getBalance() / 2.5;
                        }

                        if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                            newBalance = driver.get().getBalance() / (2.5 * 0.6);
                        }

                        break;
                    case 2:
                        if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                            newBalance = driver.get().getBalance() * 2.5;
                        }

                        if (driver.get().getCurrency().equals(CurrencyEnum.BLUE)) {
                            newBalance = driver.get().getBalance() / 0.6;
                        }

                        break;
                    case 3:
                        if (driver.get().getCurrency().equals(CurrencyEnum.RED)) {
                            newBalance = driver.get().getBalance() * (2.5 * 0.6);
                        }

                        if (driver.get().getCurrency().equals(CurrencyEnum.GREEN)) {
                            newBalance = driver.get().getBalance() * 0.6;
                        }

                        break;
                }

                return newBalance;
            }
        }

        return null;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void getBirthdayBoys() {
        List<Driver> driverList = new ArrayList<>();

        for (long i = 18; i < 70L; i++) {
            List<Driver> drivers = driverRepository.findByBirthDate(LocalDate.now().minusYears(i));
            if (drivers != null && !drivers.isEmpty()) {
                driverList.addAll(drivers);
            }
        }

        if (driverList.isEmpty()) {
            log.error("Сегодня именинники отсутствуют");
        } else {
            driverList.forEach(driver -> {
                log.info("Сегодня свой день рождения празднует: {} ({} год/года/лет)", driver.getFullName(), LocalDate.now().getYear() - driver.getBirthDate().getYear());
            });
        }
    }
}
