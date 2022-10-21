import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VThreadSizeDemo {
    private static Pattern VT_PATTERN = Pattern.compile("(.*)/runnable@(.*)");

    public static void main(String[] args) throws InterruptedException {
        var max = 100L;

        final List<Thread> threads = new ArrayList<>();
        final Instant startTime = Instant.now();

        final Set<String> poolNames = ConcurrentHashMap.newKeySet();
        final Set<String> threadNames = ConcurrentHashMap.newKeySet();

        for(var i = 1; i < max; i++){
            final var tid = i;
            final Thread t = Thread.startVirtualThread(() -> {
                try {
                    final ReentrantLock lock = new ReentrantLock();
                    lock.lock();

                    System.out.println("Hi from " + tid + " in " + Thread.currentThread());
                    Thread.sleep(1000);
                    System.out.println("Bye from " + tid + " in " + Thread.currentThread());

                    lock.unlock();

                    System.out.println("Worker "+getWorkerName());
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

    private static String getWorkerName() {
        final String name = Thread.currentThread().toString();
        final Matcher matcher = VT_PATTERN.matcher(name);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private static String getPoolName() {
        final String name = Thread.currentThread().toString();
        final Matcher matcher = VT_PATTERN.matcher(name);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }
}
