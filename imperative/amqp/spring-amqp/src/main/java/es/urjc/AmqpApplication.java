package es.urjc;

import es.urjc.service.SenderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@SpringBootApplication
public class AmqpApplication implements CommandLineRunner {

    private final SenderService senderService;

    public AmqpApplication(SenderService senderService) {
        this.senderService = senderService;
    }

    @Override
    public void run(String... args) throws Exception {

        Stream.iterate(0, i -> i + 1)
                .limit(100)
                .map(integer -> "New message " + integer)
                .forEach(this::sendDelayMessage);

    }

    private void sendDelayMessage(String message) {

        try {

            TimeUnit.MILLISECONDS.sleep(200);
            senderService.send(message);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(AmqpApplication.class, args);
    }

}
