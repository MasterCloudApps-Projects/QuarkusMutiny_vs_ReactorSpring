package urjc.mastercloudapps.quarkus.quarkus.examples;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

import java.util.Random;
import java.util.logging.Logger;

@GrpcService
public class GenerateRandomNumberService implements RandomNumberGenerator {

    private static final Logger log = Logger.getLogger(GenerateRandomNumberService.class.getName());

    @Override
    public Uni<GenerateNumberResponse> generateNumber(GenerateNumberRequest request) {
        log.info("[QUARKUS REACTIVE] - INIT grpc SERVICE thread id " + Thread.currentThread().getId());

        int randomInt = new Random()
                .ints(0, 30)
                .findFirst()
                .getAsInt();

        int response = randomInt * request.getNumber();

        Uni<GenerateNumberResponse> responseItem = Uni.createFrom().item(() ->
                GenerateNumberResponse.newBuilder().setNumber(response).build()
        );

        log.info("[QUARKUS REACTIVE] - END grpc SERVICE thread id " + Thread.currentThread().getId());

        return responseItem;
    }
}