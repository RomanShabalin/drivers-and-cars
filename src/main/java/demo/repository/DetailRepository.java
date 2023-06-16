package demo.repository;

import demo.entity.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Integer> {
    @Query(value = "select * from detail where serial_number ilike %:serialNumber% order by id", nativeQuery = true)
    List<Detail> findBySerialNumber(@Param("serialNumber") String serialNumber);

    @Modifying
    @Transactional
    @Query(value = "update detail set car_id = :carId where id = :detailId", nativeQuery = true)
    void addDetailToCar(@Param("carId") Integer carId, @Param("detailId") Integer detailId);

    @Modifying
    @Transactional
    @Query(value = "update detail set car_id = null where car_id = :carId and id = :detailId", nativeQuery = true)
    void removeDetailFromCar(@Param("carId") Integer carId, @Param("detailId") Integer detailId);

    @Query(value = "select * from detail where car_id = :carId ", nativeQuery = true)
    List<Detail> findByCarId(@Param("carId") Integer carId);
}
