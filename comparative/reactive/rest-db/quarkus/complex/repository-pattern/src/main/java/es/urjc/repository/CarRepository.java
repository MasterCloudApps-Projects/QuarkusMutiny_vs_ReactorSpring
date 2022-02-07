package es.urjc.repository;

import es.urjc.entity.Car;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car> {

    public Uni<Car> findByLicencePlate(String licencePlate) {
        return find("licencePlate", licencePlate).firstResult();
    }

}
