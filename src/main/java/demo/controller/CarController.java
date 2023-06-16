package demo.controller;

import demo.entity.Car;
import demo.entity.CarBrandEnum;
import demo.service.CarService;
import io.swagger.api.CarApi;
import io.swagger.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CarController implements CarApi {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public ResponseEntity<CarForGettingSwagger> carGetById(Integer id) {
        try {
            Optional<Car> car = carService.findById(id);
            if (car.isPresent()) {
                CarForGettingSwagger carForGettingSwagger = new CarForGettingSwagger();
                carForGettingSwagger.setVin(car.get().getVin());
                carForGettingSwagger.setGovNumber(car.get().getGovNumber());
                carForGettingSwagger.setBrand(car.get().getBrand().getValue());
                carForGettingSwagger.setReleaseYear(car.get().getReleaseYear());

                List<DetailsSwagger> detailsSwaggerList = new ArrayList<>();
                car.get().getDetails().forEach(detail -> {
                    DetailsSwagger detailsSwagger = new DetailsSwagger();
                    detailsSwagger.setSerialNumber(detail.getSerialNumber());
                    detailsSwagger.setDetailCategory(detail.getDetailCategory().getValue());
                    detailsSwaggerList.add(detailsSwagger);
                });
                carForGettingSwagger.setDetails(detailsSwaggerList);

                return new ResponseEntity<>(carForGettingSwagger, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CarForGettingSwagger>> carGetByVin(String vin) {
        try {
            List<Car> cars = carService.findByVin(vin);
            if (cars != null && !cars.isEmpty()) {
                List<CarForGettingSwagger> carForGettingSwaggerList = this.getCarForGettingSwaggerList(cars);

                return new ResponseEntity<>(carForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CarForGettingSwagger>> carGetByGovNumber(String number) {
        try {
            List<Car> cars = carService.findByGovNumber(number);
            if (cars != null && !cars.isEmpty()) {
                List<CarForGettingSwagger> carForGettingSwaggerList = this.getCarForGettingSwaggerList(cars);

                return new ResponseEntity<>(carForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<CarForGettingSwagger>> carGetAll() {
        try {
            List<Car> cars = carService.findAll();
            if (cars != null && !cars.isEmpty()) {
                List<CarForGettingSwagger> carForGettingSwaggerList = this.getCarForGettingSwaggerList(cars);

                return new ResponseEntity<>(carForGettingSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> carCreate(CarSwagger body) {
        try {
            Car car = this.getCar(body);

            boolean created = carService.save(car);
            return created ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> carUpdate(Integer id, CarSwagger body) {
        try {
            Car car = this.getCar(body);

            boolean updated = carService.update(car, id);
            return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> carAddToDriver(CarToDriverSwagger body) {
        try {
            boolean added = carService.addCarToDriver(body.getDriverId(), body.getCarId());
            return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> carRemoveFromDriver(CarToDriverSwagger body) {
        try {
            boolean removed = carService.removeCarFromDriver(body.getDriverId(), body.getCarId());
            return removed ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<CarForGettingSwagger> getCarForGettingSwaggerList(List<Car> cars) {
        List<CarForGettingSwagger> carForGettingSwaggerList = new ArrayList<>();
        cars.forEach(car -> {
            CarForGettingSwagger carForGettingSwagger = new CarForGettingSwagger();
            carForGettingSwagger.setVin(car.getVin());
            carForGettingSwagger.setGovNumber(car.getGovNumber());
            carForGettingSwagger.setBrand(car.getBrand().getValue());
            carForGettingSwagger.setReleaseYear(car.getReleaseYear());

            List<DetailsSwagger> detailsSwaggerList = new ArrayList<>();
            car.getDetails().forEach(detail -> {
                DetailsSwagger detailsSwagger = new DetailsSwagger();
                detailsSwagger.setSerialNumber(detail.getSerialNumber());
                detailsSwagger.setDetailCategory(detail.getDetailCategory().getValue());
                detailsSwaggerList.add(detailsSwagger);
            });
            carForGettingSwagger.setDetails(detailsSwaggerList);

            carForGettingSwaggerList.add(carForGettingSwagger);
        });

        return carForGettingSwaggerList;
    }

    private Car getCar(CarSwagger carSwagger) {
        Car car = new Car();
        car.setVin(carSwagger.getVin());
        car.setGovNumber(carSwagger.getGovNumber());
        car.setBrand(CarBrandEnum.byBrand(carSwagger.getBrand().toUpperCase()) != null && !carSwagger.getBrand().isEmpty() ? CarBrandEnum.byBrand(carSwagger.getBrand().toUpperCase()) : null);
        car.setReleaseYear(carSwagger.getReleaseYear());

        return car;
    }
}
