package SinusGeneratorPackage;

import java.util.*;

/**
 * Creates virtual channel buffer and initializes it with sinusoidal data with random noise (every 100 elements).
 * Channel amplitude -1 .. +1
 * Channel period 360 elements
 */
public class SinusGenerator {

    private List<Float> signalRepository;

    /**
     * Constructs an object with initialized channel buffer.
     *
     * @param  capacity  the capacity of the channel buffer
     * @throws IllegalArgumentException if the specified initial capacity is zero or negative
     */
    public SinusGenerator(int capacity) {

        if (capacity > 0) {
            this.signalRepository = new LinkedList<Float>();
            initializeSignalRepository(capacity);
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
    }

    /**
     * Returns all data from inner channel buffer.
     *
     * @return All data from inner channel buffer.
     */
    public Float[] getSignalData() {

        return getSignalData(signalRepository.size());
    }

    /**
     * Returns from inner channel buffer the amount of points specified in quantity parameter.
     *
     * @param quantity the amount of points that you want to get.
     * @throws IllegalArgumentException if the specified capacity of inner channel buffer is smaller than the quantity parameter or negative.
     */
    public Float[] getSignalData(int quantity) {

        if (quantity < 0)
            throw new IllegalArgumentException("Requested quantity: " + quantity + " is negative");
        if (quantity > signalRepository.size())
            throw new IllegalArgumentException("Requested quantity: " + quantity + " exceeds inner channel buffer size: " + signalRepository.size());

        Float[] temporaryArray = signalRepository.subList(signalRepository.size() - quantity, signalRepository.size())
                .stream().toArray(Float[]::new);
        updateSignalRepository();
        return temporaryArray;
    }

    private void initializeSignalRepository(int capacity) {

        Random random = new Random();
        for (int i = 0; i < capacity; i++) {
            Float temporaryVariable = (float) (Math.PI / 180 * i);
            if (i % 100 == 0) {
                temporaryVariable = temporaryVariable + random.nextFloat() - (float) 0.5;
            }
            signalRepository.add(temporaryVariable);
        }
    }

    private void updateSignalRepository() {

        Collections.rotate(signalRepository, -10);
    }
}
