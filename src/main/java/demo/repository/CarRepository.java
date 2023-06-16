package demo.repository;

import demo.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    @Query(value = "select * from car where vin ilike %:vin% order by id", nativeQuery = true)
    List<Car> findByVin(@Param("vin") String vin);

    @Query(value = "select * from car where gov_number ilike %:govNumber% order by id", nativeQuery = true)
    List<Car> findByGovNumber(@Param("govNumber") String govNumber);

    @Modifying
    @Transactional
    @Query(value = "update car set driver_id = :driverId where id = :carId", nativeQuery = true)
    void addCarToDriver(@Param("driverId") Integer driverId, @Param("carId") Integer carId);

    @Modifying
    @Transactional
    @Query(value = "update car set driver_id = null where driver_id = :driverId and id = :carId", nativeQuery = true)
    void removeCarFromDriver(@Param("driverId") Integer driverId, @Param("carId") Integer carId);
}