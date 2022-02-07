package es.urjc;

import es.urjc.service.SenderService;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@QuarkusMain
public class AmqpApplication implements QuarkusApplication {

    private final SenderService senderService;

    @Inject
    public AmqpApplication(SenderService senderService) {
        this.senderService = senderService;
    }

    @Override
    public int run(String... args) throws RuntimeException {

        Stream.iterate(0, i -> i + 1)
                .limit(100)
                .map(integer -> "New message " + integer)
                .forEach(this::sendDelayMessage);

        return 0;
    }

    private void sendDelayMessage(String message) {

        try {

            TimeUnit.MILLISECONDS.sleep(200);
            senderService.send(message);

        } catch (InterruptedException e) {
           e.printStackTrace();
        }

    }

}
