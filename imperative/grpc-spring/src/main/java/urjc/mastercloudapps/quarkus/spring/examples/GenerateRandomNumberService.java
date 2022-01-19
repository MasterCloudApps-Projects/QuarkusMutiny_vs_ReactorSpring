package urjc.mastercloudapps.quarkus.spring.examples;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


@GRpcService
public class GenerateRandomNumberService extends RandomNumberGeneratorGrpc.RandomNumberGeneratorImplBase {

    private static final Logger log = LoggerFactory.getLogger(GenerateRandomNumberService.class);

    @Override
    public void generateNumber(GenerateNumberRequest request, StreamObserver<GenerateNumberResponse> responseObserver) {
        log.info("[SPRING IMPERATIVE] - INIT grpc SERVICE thread id " + Thread.currentThread().getName());

        int randomInt = new Random()
                .ints(0, 30)
                .findFirst()
                .getAsInt();

        int response = randomInt * request.getNumber();

        responseObserver.onNext(GenerateNumberResponse.newBuilder().setNumber(response).build());
        responseObserver.onCompleted();

        log.info("[SPRING IMPERATIVE] - END grpc SERVICE thread id " + Thread.currentThread().getName());

    }

}
