package demo.controller;

import demo.entity.Detail;
import demo.entity.DetailCategoryEnum;
import demo.service.DetailService;
import io.swagger.api.DetailApi;
import io.swagger.model.DetailSwagger;
import io.swagger.model.DetailToCarSwagger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DetailController implements DetailApi {
    private final DetailService detailService;

    public DetailController(DetailService detailService) {
        this.detailService = detailService;
    }

    @Override
    public ResponseEntity<DetailSwagger> detailGetById(Integer id) {
        try {
            Optional<Detail> detail = detailService.findById(id);
            if (detail.isPresent()) {
                DetailSwagger detailSwagger = new DetailSwagger();
                detailSwagger.setDetailCategory(detail.get().getDetailCategory().getValue());
                detailSwagger.setSerialNumber(detail.get().getSerialNumber());
                return new ResponseEntity<>(detailSwagger, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DetailSwagger>> detailGetBySerialNumber(String number) {
        try {
            List<Detail> details = detailService.findBySerialNumber(number);
            if (details != null && !details.isEmpty()) {
                List<DetailSwagger> detailSwaggerList = this.getDetailSwaggerList(details);

                return new ResponseEntity<>(detailSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<DetailSwagger>> detailGetAll() {
        try {
            List<Detail> details = detailService.findAll();
            if (details != null && !details.isEmpty()) {
                List<DetailSwagger> detailSwaggerList = this.getDetailSwaggerList(details);

                return new ResponseEntity<>(detailSwaggerList, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> detailCreate(DetailSwagger body) {
        try {
            Detail detail = this.getDetail(body);

            boolean created = detailService.save(detail);
            return created ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> detailUpdate(Integer id, DetailSwagger body) {
        try {
            Detail detail = this.getDetail(body);

            boolean updated = detailService.update(detail, id);
            return updated ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> detailAddToCar(DetailToCarSwagger body) {
        try {
            boolean added = detailService.addDetailToCar(body.getCarId(), body.getDetailId());
            return added ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Void> detailRemoveFromCar(DetailToCarSwagger body) {
        try {
            boolean removed = detailService.removeDetailFromCar(body.getCarId(), body.getDetailId());
            return removed ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<DetailSwagger> getDetailSwaggerList(List<Detail> details) {
        List<DetailSwagger> detailSwaggerList = new ArrayList<>();
        details.forEach(detail -> {
            DetailSwagger detailSwagger = new DetailSwagger();
            detailSwagger.setDetailCategory(detail.getDetailCategory().getValue());
            detailSwagger.setSerialNumber(detail.getSerialNumber());
            detailSwaggerList.add(detailSwagger);
        });

        return detailSwaggerList;
    }

    private Detail getDetail(DetailSwagger detailSwagger) {
        Detail detail = new Detail();
        detail.setDetailCategory(!detailSwagger.getDetailCategory().isEmpty() && DetailCategoryEnum.byCode(Integer.valueOf(detailSwagger.getDetailCategory())) != null ? DetailCategoryEnum.byCode(Integer.valueOf(detailSwagger.getDetailCategory())) : null);
        detail.setSerialNumber(detailSwagger.getSerialNumber());

        return detail;
    }
}
