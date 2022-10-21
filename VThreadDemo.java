import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class VThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        var max = 100L;

        final List<Thread> threads = new ArrayList<>();
        final Instant startTime = Instant.now();
        
        for(var i = 0; i < max; i++){
            final var tid = i;
            final Thread t = Thread.startVirtualThread(() -> {
                try {
                    System.out.println("Hi from "+tid+" in "+Thread.currentThread());
                    Thread.sleep(1000);
                    System.out.println("Bye from "+tid+" in "+Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }

        final Instant endTime = Instant.now();
        System.out.println("Time elapsed "+ Duration.between(startTime, endTime).toString());
    }
}