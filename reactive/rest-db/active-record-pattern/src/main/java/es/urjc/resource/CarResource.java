package es.urjc.resource;

import es.urjc.dto.CarBasicInformation;
import es.urjc.dto.ErrorInformation;
import es.urjc.entity.Car;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;

@Path("/cars")
public class CarResource {

    @GET
    @Path("/")
    public Multi<CarBasicInformation> getAllCars() {
        return Car.listAll()
                .map(carList -> carList.stream()
                        .map(panacheEntity -> (Car) panacheEntity)
                        .map(this::getCarBasicInformation))
                .onItem().transformToMulti(carStream -> Multi.createFrom().items(carStream));
    }

    @GET
    @Path("/{licencePlate}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getCar(@PathParam("licencePlate") String licensePlate) {

        return Car.findByLicencePlate(licensePlate)
                .onItem().ifNotNull().transform(this::getCarBasicInformation)
                .onItem().ifNotNull().transform(carBasicInformation -> ok(carBasicInformation).build())
                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
    }

    @POST
    @Path("/")
    @ReactiveTransactional
    public Uni<Response> saveCar(Car newCar) {

        return Car.findByLicencePlate(newCar.getLicencePlate())
                .onItem().ifNotNull().failWith(this::failWithBadRequestExistingLicencePlateException)
                .onItem().ifNull().continueWith(newCar)
                .flatMap(car -> car.persist())
                .onItem().castTo(Car.class)
                .map(Car::getLicencePlate)
                .flatMap(this::getCar);
    }

    @PUT
    @Path("/update-method/{carId}")
    @ReactiveTransactional
    public Uni<Response> updateCarByUpdateMethod(@PathParam("carId") long carId, Car updateCar) {

        return Car.update("licencePlate = ?1, brand = ?2, model = ?3, price = ?4 where id = ?5",
                updateCar.getLicencePlate(), updateCar.getBrand(), updateCar.getModel(),
                updateCar.getPrice(), carId)
            .onItem()
                .transformToUni(numberOfCarUpdated -> {
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

        return Car.findById(carId)
                .map(Car.class::cast)
                .onItem().ifNotNull().transform(car -> {
                    car.setLicencePlate(updateCar.getLicencePlate());
                    car.setBrand(updateCar.getBrand());
                    car.setModel(updateCar.getModel());
                    car.setPrice(updateCar.getPrice());
                    return car;
                })
                .onItem().ifNull().failWith(this::failWithNotFoundCarException)
                .onItem().transformToUni(updatedCar -> updatedCar.persist())
                .onItem().castTo(Car.class)
                .onItem().transformToUni(updatedCar -> {
                    if (updatedCar.isPersistent()) {
                        return getCar(updatedCar.getLicencePlate());
                    }
                    throw new InternalServerErrorException();
                });
    }

    @DELETE
    @Path("/{carId}")
    @ReactiveTransactional
    public Uni<Response> deleteCar(@PathParam("carId") long carId) {

        return Car.findById(carId)
                .map(Car.class::cast)
                .onItem().ifNotNull().transformToUni(car -> {

                    if (isNull(car.getPlayer())) {
                        return Car.deleteById(carId)
                                .map(isDeleted -> isDeleted ? noContent().build() : null)
                                .onItem().ifNull().failWith(this::failWithNotFoundCarException);
                    }

                    Response badRequestResponse = status(BAD_REQUEST)
                            .entity(new ErrorInformation("Car is associated to player yet"))
                            .build();

                    throw new BadRequestException(badRequestResponse);

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

}