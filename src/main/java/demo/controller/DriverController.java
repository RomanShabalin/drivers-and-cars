package demo.controller;

import demo.entity.CurrencyEnum;
import demo.entity.Driver;
import demo.entity.DriverLicenseCategoryEnum;
import demo.service.DriverService;
import io.swagger.api.DriverApi;
import io.swagger.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
public class DriverController implements DriverApi {
    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @Override
    public ResponseEntity<DriverForGettingSwagger> driverGetById(Integer id) {
        try {
            Optional<Driver> driver = driverService.findById(id);
            if (driver.isPresent()) {
                DriverForGettingSwagger driverForGettingSwagger = new DriverForGettingSwagger();
                driverForGettingSwagger.setFullName(driver.get().getFullName());
                driverForGettingSwagger.setBirthDate(driver.get().getBirthDate().toString());
                driverForGettingSwagger.setPassport(driver.get().getPassport());
                driverForGettingSwagger.setDriverLicenseCategory(driver.get().getDriverLicenseCategory().getValue());
                driverForGettingSwagger.setExperience(driver.get().getExperience());

                List<CarsSwagger> carsSwaggerList = new ArrayList<>();
                driver.get().getCars().forEach(car -> {
                    CarsSwagger carsSwagger = new CarsSwagger();
                    carsSwagger.setGovNumber(car.getGovNumber());
                    carsSwagger.setBrand(car.getBrand().getValue());
                    carsSwaggerList.add(carsSwagger);
                });
                driverForGettingSwagger.setCars(carsSwaggerList);

                return new ResponseEntity<>(driverForGettingSwagger, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DriverForGettingSwagger>> driverGetByName(String name) {
        try {
            List<Driver> drivers = driverService.findByName(name);
            if (drivers != null && !drivers.isEmpty()) {
                List<DriverForGettingSwagger> driverForGettingSwaggerList = this.getDriverForGettingSwaggerList(drivers);

                return new ResponseEntity<>(driverForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DriverForGettingSwagger>> driverGetByPassport(String passport) {
        try {
            List<Driver> drivers = driverService.findByPassport(passport);
            if (drivers != null && !drivers.isEmpty()) {
                List<DriverForGettingSwagger> driverForGettingSwaggerList = this.getDriverForGettingSwaggerList(drivers);

                return new ResponseEntity<>(driverForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DriverForGettingSwagger>> driverGetAll() {
        try {
            List<Driver> drivers = driverService.findAll();
            if (drivers != null && !drivers.isEmpty()) {
                List<DriverForGettingSwagger> driverForGettingSwaggerList = this.getDriverForGettingSwaggerList(drivers);

                return new ResponseEntity<>(driverForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DriverForGettingSwagger>> driverGetAllWithLimitAndOffset(Integer limit, Integer offset) {
        try {
            List<Driver> drivers = driverService.driverGetAllWithLimitAndOffset(limit, offset);
            if (drivers != null && !drivers.isEmpty()) {
                List<DriverForGettingSwagger> driverForGettingSwaggerList = this.getDriverForGettingSwaggerList(drivers);

                return new ResponseEntity<>(driverForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<BalanceSwagger> driverGetBalance(Integer driverId, Integer currencyId) {
        try {
            Double balance = driverService.getBalance(driverId, currencyId);
            Optional<Driver> driver = driverService.findById(driverId);
            if (balance != null && driver.isPresent()) {
                BalanceSwagger balanceSwagger = new BalanceSwagger();
                balanceSwagger.setFullName(driver.get().getFullName());
                balanceSwagger.setBalance(balance);
                balanceSwagger.setOriginalCurrency(driver.get().getCurrency().getValue());

                return new ResponseEntity<>(balanceSwagger, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> driverGetBirthdayBoys() {
        try {
            driverService.getBirthdayBoys();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> driverCreate(DriverSwagger body) {
        try {
            Driver driver = this.getDriver(body);

            boolean created = driverService.save(driver);
            return created ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> driverUpdate(Integer id, DriverSwagger body) {
        try {
            Driver driver = this.getDriver(body);

            boolean updated = driverService.update(driver, id);
            return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> driverAddBalance(BalanceToDriverSwagger body) {
        try {
            boolean added = driverService.updateBalance(body.getDriverId(), body.getBalance(), body.getCurrencyId(), true);
            return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> driverDeleteBalance(BalanceToDriverSwagger body) {
        try {
            boolean deleted = driverService.updateBalance(body.getDriverId(), body.getBalance(), body.getCurrencyId(), false);
            return deleted ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<DriverForGettingSwagger> getDriverForGettingSwaggerList(List<Driver> drivers) {
        List<DriverForGettingSwagger> driverForGettingSwaggerList = new ArrayList<>();
        drivers.forEach(driver -> {
            DriverForGettingSwagger driverForGettingSwagger = new DriverForGettingSwagger();
            driverForGettingSwagger.setFullName(driver.getFullName());
            driverForGettingSwagger.setBirthDate(driver.getBirthDate().toString());
            driverForGettingSwagger.setPassport(driver.getPassport());
            driverForGettingSwagger.setDriverLicenseCategory(driver.getDriverLicenseCategory().getValue());
            driverForGettingSwagger.setExperience(driver.getExperience());
            driverForGettingSwagger.setBalance(driver.getBalance());

            List<CarsSwagger> carsSwaggerList = new ArrayList<>();
            driver.getCars().forEach(car -> {
                CarsSwagger carsSwagger = new CarsSwagger();
                carsSwagger.setGovNumber(car.getGovNumber());
                carsSwagger.setBrand(car.getBrand().getValue());
                carsSwaggerList.add(carsSwagger);
            });
            driverForGettingSwagger.setCars(carsSwaggerList);

            driverForGettingSwaggerList.add(driverForGettingSwagger);
        });

        return driverForGettingSwaggerList;
    }

    private Driver getDriver(DriverSwagger driverSwagger) {
        Driver driver = new Driver();
        driver.setFullName(driverSwagger.getFullName());
        driver.setBirthDate(LocalDate.parse(driverSwagger.getBirthDate()));
        driver.setPassport(driverSwagger.getPassport());
        driver.setDriverLicenseCategory(DriverLicenseCategoryEnum.byCategory(driverSwagger.getDriverLicenseCategory().toUpperCase()) != null && !driverSwagger.getDriverLicenseCategory().isEmpty() ? DriverLicenseCategoryEnum.byCategory(driverSwagger.getDriverLicenseCategory().toUpperCase()) : null);
        driver.setExperience(driverSwagger.getExperience());
        driver.setBalance(0.0);
        driver.setCurrency(CurrencyEnum.NONE);

        return driver;
    }
}
