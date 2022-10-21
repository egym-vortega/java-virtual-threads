import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class FJDemo {

    public static void main(String[] args) throws InterruptedException {
        var max = 100L;

        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        final List<ForkJoinTask> tasks = new ArrayList<>();
        final Instant startTime = Instant.now();

        for(var i = 1; i < max; i++){
            final var tid = i;

            ForkJoinTask t = forkJoinPool.submit(() -> {
                try {
                    System.out.println("Hi from "+tid+" in "+Thread.currentThread());
                    Thread.sleep(1000);
                    System.out.println("Bye from "+tid+" in "+Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            tasks.add(t);
        }

        for (ForkJoinTask t : tasks) {
            t.join();
        }

        final Instant endTime = Instant.now();
        System.out.println("Time elapsed "+ Duration.between(startTime, endTime).toString());
    }
}
