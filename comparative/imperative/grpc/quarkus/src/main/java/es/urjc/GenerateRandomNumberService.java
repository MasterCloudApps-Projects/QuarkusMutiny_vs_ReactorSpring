package es.urjc;

import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;

import java.util.Random;
import java.util.logging.Logger;

@GrpcService
public class GenerateRandomNumberService extends RandomNumberGeneratorGrpc.RandomNumberGeneratorImplBase {

    private static final Logger log = Logger.getLogger(GenerateRandomNumberService.class.getName());

    @Override
    @Blocking
    public void generateNumber(GenerateNumberRequest request, StreamObserver<GenerateNumberResponse> responseObserver) {

        log.info("[QUARKUS IMPERATIVE] - INIT grpc SERVICE thread id " + Thread.currentThread().getId());

        int randomInt = new Random()
                .ints(0, 30)
                .findFirst()
                .getAsInt();

        int response = randomInt * request.getNumber();

        responseObserver.onNext(GenerateNumberResponse.newBuilder().setNumber(response).build());
        responseObserver.onCompleted();

        log.info("[QUARKUS IMPERATIVE] - END grpc SERVICE thread id " + Thread.currentThread().getId());

    }
}