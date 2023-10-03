import java.util.concurrent.atomic.AtomicInteger;

public class UUIDGenerator {

    private AtomicInteger atomicInteger;

    UUIDGenerator (){
        atomicInteger = new AtomicInteger(1000);
    }
    /**
     * Source: https://stackoverflow.com/questions/2178992/how-to-generate-unique-id-in-java-integer
     *
     * @return unique id
     */
    public int generateUUID(){
        return atomicInteger.incrementAndGet();
    }
}
