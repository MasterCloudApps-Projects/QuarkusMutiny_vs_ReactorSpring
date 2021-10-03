package es.urjc.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

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

    public static Optional<Car> findByLicencePlate(String licencePlate) {
        return find("licencePlate", licencePlate).firstResultOptional();
    }

}
