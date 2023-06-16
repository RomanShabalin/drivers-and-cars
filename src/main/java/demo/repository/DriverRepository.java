package demo.repository;

import demo.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    @Query(value = "select * from driver where full_name ilike %:name% order by id", nativeQuery = true)
    List<Driver> findByName(@Param("name") String name);

    @Query(value = "select * from driver where passport ilike %:passport% order by id", nativeQuery = true)
    List<Driver> findByPassport(@Param("passport") String passport);

    @Query(value = "select * from driver order by id limit :limit offset :offset * :limit", nativeQuery = true)
    List<Driver> driverGetAllWithLimitAndOffset(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Modifying
    @Transactional
    @Query(value = "update driver set balance = :balance where id = :driverId", nativeQuery = true)
    void updateBalance(@Param("driverId") Integer driverId, @Param("balance") Double balance);

    @Modifying
    @Transactional
    @Query(value = "update driver set currency = :currency where id = :driverId", nativeQuery = true)
    void updateCurrency(@Param("driverId") Integer driverId, @Param("currency") Integer currency);

    @Query(value = "select * from driver where birth_date = :birthDate", nativeQuery = true)
    List<Driver> findByBirthDate(@Param("birthDate") LocalDate birthDate);
}
