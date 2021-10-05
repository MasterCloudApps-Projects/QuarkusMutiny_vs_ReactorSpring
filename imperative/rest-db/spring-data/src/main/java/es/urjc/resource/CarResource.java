package es.urjc.resource;

import es.urjc.dto.CarBasicInformation;
import es.urjc.dto.ErrorInformation;
import es.urjc.entity.Car;
import es.urjc.repository.CarRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/cars")
public class CarResource {

    private final CarRepository carRepository;

    @Inject
    public CarResource(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GET
    @Path("/")
    public List<CarBasicInformation> getAllCars() {
        return this.carRepository.findAll().stream()
                .map(this::getCarBasicInformation)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{licencePlate}")
    public Response getCar(@PathParam("licencePlate") String licensePlate) {
        return this.carRepository.findByLicencePlate(licensePlate)
                .map(this::getCarBasicInformation)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found car")))
                .build();
    }

    @POST
    @Path("/")
    @Transactional
    public Response saveCar(Car car) {

        if (this.carRepository.findByLicencePlate(car.getLicencePlate()).isEmpty()) {
            this.carRepository.save(car);
            return getCar(car.getLicencePlate());
        } else {
            return status(BAD_REQUEST).entity(new ErrorInformation("Already exists licence plate")).build();
        }

    }

    @PUT
    @Path("/update-method/{carId}")
    @Transactional
    public Response updateCarByUpdateMethod(@PathParam("carId") long carId, Car updateCar) {

        this.carRepository.update(updateCar.getLicencePlate(), updateCar.getBrand(), updateCar.getModel(),
                updateCar.getPrice(), carId);

        return getCar(updateCar.getLicencePlate());
    }

    @PUT
    @Path("/save-method/{carId}")
    @Transactional
    public Response updateCarByPersistMethod(@PathParam("carId") long carId, Car updateCar) {
        return this.carRepository.findById(carId)
                .map(car -> {
                    car.setLicencePlate(updateCar.getLicencePlate());
                    car.setBrand(updateCar.getBrand());
                    car.setModel(updateCar.getModel());
                    car.setPrice(updateCar.getPrice());
                    return car;
                })
                .map(updatedCar -> {
                    this.carRepository.save(updatedCar);
                    return getCar(updatedCar.getLicencePlate());
                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found car")).build());
    }

    @DELETE
    @Path("/{carId}")
    @Transactional
    public Response deleteCar(@PathParam("carId") long carId) {

        return this.carRepository.findById(carId)
                .map(car -> {

                    if (isNull(car.getPlayer())) {
                       this.carRepository.deleteById(carId);
                        return noContent();
                    } else {
                        return status(BAD_REQUEST).entity(new ErrorInformation("Car is associated to player yet"));
                    }

                })
                .orElse(Response.status(NOT_FOUND).entity(new ErrorInformation("Not found car")))
                .build();

    }

    private CarBasicInformation getCarBasicInformation(Car car) {
        return new CarBasicInformation(
                car.getId(), car.getLicencePlate(),
                car.getBrand(), car.getModel(), car.getPrice());
    }

}