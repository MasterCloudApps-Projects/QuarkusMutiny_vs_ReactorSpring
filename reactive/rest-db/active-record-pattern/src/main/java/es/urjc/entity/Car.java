package es.urjc.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String licencePlate;
    private String brand;
    private String model;
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Player player;

    public static Uni<Car> findByLicencePlate(String licencePlate) {
        return find("licencePlate", licencePlate).firstResult();
    }

}
