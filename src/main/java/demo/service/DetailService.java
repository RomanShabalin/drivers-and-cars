package demo.service;

import demo.entity.Detail;
import demo.entity.DetailCategoryEnum;
import demo.repository.CarRepository;
import demo.repository.DetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetailService {
    private final DetailRepository detailRepository;
    private final CarRepository carRepository;

    private static final Logger log = LoggerFactory.getLogger(DriverService.class);

    public DetailService(DetailRepository detailRepository, CarRepository carRepository) {
        this.detailRepository = detailRepository;
        this.carRepository = carRepository;
    }

    public Optional<Detail> findById(Integer id) {
        return detailRepository.findById(id);
    }

    public List<Detail> findBySerialNumber(String serialNumber) {
        return detailRepository.findBySerialNumber(serialNumber);
    }

    public List<Detail> findAll() {
        return detailRepository.findAll();
    }

    public boolean save(Detail detail) {
        if (detail == null ||
                detail.getDetailCategory() == null || detail.getDetailCategory().getValue().trim().isEmpty() ||
                detail.getSerialNumber() == null || detail.getSerialNumber().trim().isEmpty()) {
            log.error("Ошибка сохранения детали");
            return false;
        }

        detailRepository.save(detail);
        log.info("Деталь успешно сохранена");
        return true;
    }

    public boolean update(Detail detail, Integer id) {
        if (detailRepository.existsById(id)) {
            Optional<Detail> existsDetail = detailRepository.findById(id);
            Detail updatedDetail = new Detail();

            updatedDetail.setId(id);

            if (detail.getDetailCategory() == null || detail.getDetailCategory().getValue().trim().isEmpty() || detail.getDetailCategory().equals(existsDetail.get().getDetailCategory())) {
                updatedDetail.setDetailCategory(existsDetail.get().getDetailCategory());
            } else {
                updatedDetail.setDetailCategory(detail.getDetailCategory());
            }

            if (detail.getSerialNumber() == null || detail.getSerialNumber().trim().isEmpty() || detail.getSerialNumber().equals(existsDetail.get().getSerialNumber())) {
                updatedDetail.setSerialNumber(existsDetail.get().getSerialNumber());
            } else {
                updatedDetail.setSerialNumber(detail.getSerialNumber());
            }

            updatedDetail.setCar(existsDetail.get().getCar() != null ? existsDetail.get().getCar() : null);

            detailRepository.save(updatedDetail);
            log.info("Деталь успешно изменена");
            return true;
        }

        log.error("Ошибка изменения детали");
        return false;
    }

    public boolean addDetailToCar(Integer carId, Integer detailId) {
        if (carRepository.existsById(carId) && detailRepository.existsById(detailId)) {
            Optional<Detail> detail = detailRepository.findById(detailId);
            List<Detail> details = detailRepository.findByCarId(carId);
            int countWheel = 0;

            for (Detail d : details) {
                if (d.getDetailCategory().equals(detail.get().getDetailCategory())) {
                    if (detail.get().getDetailCategory().equals(DetailCategoryEnum.WHEEL)) {
                        countWheel += 1;
                    } else {
                        log.error("Ошибка прикрепления детали к автомобилю: в автомобиле уже содержится указанная категория детали");
                        return false;
                    }

                    if (countWheel == 4) {
                        log.error("Ошибка прикрепления детали к автомобилю: автомобиль уже содержит 4 колеса");
                        return false;
                    }
                }
            }

            detailRepository.addDetailToCar(carId, detailId);
            log.info("Деталь успешно прикреплена к автомобилю");
            return true;
        }

        log.error("Ошибка прикрепления детали к автомобилю");
        return false;
    }

    public boolean removeDetailFromCar(Integer carId, Integer detailId) {
        if (carRepository.existsById(carId) && detailRepository.existsById(detailId)) {
            detailRepository.removeDetailFromCar(carId, detailId);
            log.info("Деталь успешно откреплена от автомобиля");
            return true;
        }

        log.error("Ошибка открепления детали от автомобиля");
        return false;
    }
}
