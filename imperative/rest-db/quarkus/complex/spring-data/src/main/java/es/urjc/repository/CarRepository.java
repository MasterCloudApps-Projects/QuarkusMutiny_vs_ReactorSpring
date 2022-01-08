package es.urjc.repository;

import es.urjc.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByLicencePlate(String licencePlate);

    @Modifying
    @Query("update Car c " +
            "set c.licencePlate = :licencePlate, c.brand = :brand, c.model = :model, c.price = :price " +
            "where c.id = :carId")
    void update(@Param("licencePlate") String licencePlate, @Param("brand") String brand,
                @Param("model") String model, @Param("price") BigDecimal price, @Param("carId") Long carId);

}
