package urjc.mastercloudapps.quarkus.spring.examples;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Random;


@GRpcService
public class GenerateRandomNumberService extends ReactorRandomNumberGeneratorGrpc.RandomNumberGeneratorImplBase {

    private static final Logger log = LoggerFactory.getLogger(GenerateRandomNumberService.class);

    @Override
    public Mono<GenerateNumberResponse> generateNumber(Mono<GenerateNumberRequest> request) {
        log.info("[SPRING REACTIVE] - INIT grpc SERVICE thread id " + Thread.currentThread().getName());

        int randomInt = new Random()
                .ints(0, 30)
                .findFirst()
                .getAsInt();

        Mono<GenerateNumberResponse> response = request
                .map(GenerateNumberRequest::getNumber)
                .map(protoRequest -> GenerateNumberResponse
                        .newBuilder()
                        .setNumber(randomInt * protoRequest)
                        .build());

        log.info("[SPRING REACTIVE] - END grpc SERVICE thread id " + Thread.currentThread().getName());
        return response;
    }

}
