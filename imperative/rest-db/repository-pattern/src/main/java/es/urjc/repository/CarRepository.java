package es.urjc.repository;

import es.urjc.entity.Car;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public Optional<Car> findByLicencePlate(String licencePlate) {
        return find("licencePlate", licencePlate).firstResultOptional();
    }

}
