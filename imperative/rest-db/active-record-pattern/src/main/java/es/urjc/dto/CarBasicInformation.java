package es.urjc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarBasicInformation {

    private long id;
    private String licencePlate;
    private String brand;
    private String model;
    private BigDecimal price;
}
