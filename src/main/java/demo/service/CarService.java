package demo.service;

import demo.entity.Car;
import demo.repository.CarRepository;
import demo.repository.DriverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final DriverRepository driverRepository;

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    public CarService(CarRepository carRepository, DriverRepository driverRepository) {
        this.carRepository = carRepository;
        this.driverRepository = driverRepository;
    }

    public Optional<Car> findById(Integer id) {
        return carRepository.findById(id);
    }

    public List<Car> findByVin(String vin) {
        return carRepository.findByVin(vin);
    }

    public List<Car> findByGovNumber(String govNumber) {
        return carRepository.findByGovNumber(govNumber);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public boolean save(Car car) {
        if (car == null ||
                car.getVin() == null || car.getVin().trim().isEmpty() ||
                car.getGovNumber() == null || car.getGovNumber().trim().isEmpty() ||
                car.getBrand() == null || car.getBrand().getValue().trim().isEmpty() ||
                car.getReleaseYear() == null || car.getReleaseYear().toString().trim().isEmpty() || car.getReleaseYear() < 1900 || car.getReleaseYear() > LocalDate.now().getYear()) {
            log.error("Ошибка сохранения автомобиля");
            return false;
        }

        carRepository.save(car);
        log.info("Автомобиль успешно сохранен");
        return true;
    }

    public boolean update(Car car, Integer id) {
        if (carRepository.existsById(id)) {
            Optional<Car> existsCar = carRepository.findById(id);
            Car updatedCar = new Car();

            updatedCar.setId(id);

            if (car.getVin() == null || car.getVin().trim().isEmpty() || car.getVin().equals(existsCar.get().getVin())) {
                updatedCar.setVin(existsCar.get().getVin());
            } else {
                updatedCar.setVin(car.getVin());
            }

            if (car.getGovNumber() == null || car.getGovNumber().trim().isEmpty() || car.getGovNumber().equals(existsCar.get().getGovNumber())) {
                updatedCar.setGovNumber(existsCar.get().getGovNumber());
            } else {
                updatedCar.setGovNumber(car.getGovNumber());
            }

            if (car.getBrand() == null || car.getBrand().getValue().trim().isEmpty() || car.getBrand().equals(existsCar.get().getBrand())) {
                updatedCar.setBrand(existsCar.get().getBrand());
            } else {
                updatedCar.setBrand(car.getBrand());
            }

            if (car.getReleaseYear() == null || car.getReleaseYear().toString().trim().isEmpty() || car.getReleaseYear().equals(existsCar.get().getReleaseYear()) || car.getReleaseYear() < 1900 || car.getReleaseYear() > LocalDate.now().getYear()) {
                updatedCar.setReleaseYear(existsCar.get().getReleaseYear());
            } else {
                updatedCar.setReleaseYear(car.getReleaseYear());
            }

            updatedCar.setDriver(existsCar.get().getDriver() != null ? existsCar.get().getDriver() : null);

            carRepository.save(updatedCar);
            log.info("Автомобиль успешно изменен");
            return true;
        }

        log.error("Ошибка изменения автомобиля");
        return false;
    }

    public boolean addCarToDriver(Integer driverId, Integer carId) {
        if (driverRepository.existsById(driverId) && carRepository.existsById(carId)) {
            carRepository.addCarToDriver(driverId, carId);
            log.info("Автомобиль успешно прикреплен к водителю");
            return true;
        }

        log.error("Ошибка прикрепления автомобиля к водителю");
        return false;
    }

    public boolean removeCarFromDriver(Integer driverId, Integer carId) {
        if (driverRepository.existsById(driverId) && carRepository.existsById(carId)) {
            carRepository.removeCarFromDriver(driverId, carId);
            log.info("Автомобиль успешно откреплен от водителя");
            return true;
        }

        log.error("Ошибка открепления автомобиля от водителя");
        return false;
    }
}
