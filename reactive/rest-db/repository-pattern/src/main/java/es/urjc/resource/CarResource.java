package es.urjc.resource;

import es.urjc.dto.CarBasicInformation;
import es.urjc.dto.ErrorInformation;
import es.urjc.entity.Car;
import es.urjc.repository.CarRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/cars")
public class CarResource {

    private final CarRepository carRepository;

    @Inject
    public CarResource(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GET
    @Path("/")
    public Multi<CarBasicInformation> getAllCars() {
        return this.carRepository.listAll()
                .map(carList -> carList.stream().map(this::getCarBasicInformation))
                .onItem().transformToMulti(carStream -> Multi.createFrom().items(carStream));
    }

    @GET
    @Path("/{licencePlate}")
    public Uni<Response> getCar(@PathParam("licencePlate") String licensePlate) {

        return this.carRepository.findByLicencePlate(licensePlate)
                .onItem().ifNotNull().transform(this::getCarBasicInformation)
                .onItem().ifNotNull().transform(carBasicInformation -> ok(carBasicInformation).build())
                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
    }

    @POST
    @Path("/")
    @ReactiveTransactional
    public Uni<Response> saveCar(Car newCar) {

        return this.carRepository.findByLicencePlate(newCar.getLicencePlate())
                .onItem().ifNotNull().failWith(this::failWithBadRequestExistingLicencePlateException)
                .onItem().ifNull().continueWith(newCar)
                .flatMap(this.carRepository::persist)
                .map(Car::getLicencePlate)
                .flatMap(this::getCar);
    }

    @PUT
    @Path("/update-method/{carId}")
    @ReactiveTransactional
    public Uni<Response> updateCarByUpdateMethod(@PathParam("carId") long carId, Car updateCar) {

        return this.carRepository.update("licencePlate = ?1, brand = ?2, model = ?3, price = ?4 where id = ?5",
                        updateCar.getLicencePlate(), updateCar.getBrand(), updateCar.getModel(),
                        updateCar.getPrice(), carId)
                .chain(numberOfCarUpdated -> {
                    if (numberOfCarUpdated == 1) {
                        return getCar(updateCar.getLicencePlate());
                    }
                    throw failWithNotFoundCarException();
                });
    }

    @PUT
    @Path("/persist-method/{carId}")
    @ReactiveTransactional
    public Uni<Response> updateCarByPersistMethod(@PathParam("carId") long carId, Car updateCar) {

        return this.carRepository.findById(carId)
                .onItem().ifNotNull().transform(car -> {
                    car.setLicencePlate(updateCar.getLicencePlate());
                    car.setBrand(updateCar.getBrand());
                    car.setModel(updateCar.getModel());
                    car.setPrice(updateCar.getPrice());
                    return car;
                })
                .onItem().ifNull().failWith(this::failWithNotFoundCarException)
                .chain(this.carRepository::persist)
                .chain(updatedCar -> {
                    if (this.carRepository.isPersistent(updatedCar)) {
                        return getCar(updatedCar.getLicencePlate());
                    }
                    throw new InternalServerErrorException();
                });
    }

    @DELETE
    @Path("/{carId}")
    @ReactiveTransactional
    public Uni<Response> deleteCar(@PathParam("carId") long carId) {

        return this.carRepository.findById(carId)
                .onItem().ifNotNull().transformToUni(car -> {

                    if (isNull(car.getPlayer())) {
                        return this.carRepository.deleteById(carId)
                                .map(isDeleted -> isDeleted ? noContent().build() : null)
                                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
                    }

                    throw failWithBadRequestCarIsAssociatedToPlayerYet();

                })
                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
    }

    private CarBasicInformation getCarBasicInformation(Car car) {
        return new CarBasicInformation(
                car.getId(), car.getLicencePlate(),
                car.getBrand(), car.getModel(), car.getPrice());
    }

    private NotFoundException failWithNotFoundCarException() {
        Response notFoundCarResponse = status(NOT_FOUND).entity(new ErrorInformation("Not found car")).build();
        return new NotFoundException(notFoundCarResponse);
    }

    private BadRequestException failWithBadRequestExistingLicencePlateException() {
        ErrorInformation errorInformation = new ErrorInformation("Already exists licence plate");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        return new BadRequestException(badRequestResponse);
    }

    private BadRequestException failWithBadRequestCarIsAssociatedToPlayerYet() {
        ErrorInformation errorInformation = new ErrorInformation("Car is associated to player yet");
        Response badRequestResponse = status(BAD_REQUEST).entity(errorInformation).build();
        return new BadRequestException(badRequestResponse);
    }
}
