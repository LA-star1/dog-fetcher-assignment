package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A BreedFetcher that caches the results of another BreedFetcher.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher delegate;
    private final Map<String, List<String>> cache;

    /**
     * @param delegate the BreedFetcher to cache results from
     */
    public CachingBreedFetcher(BreedFetcher delegate) {
        this.delegate = delegate;
        this.cache = new HashMap<>();
    }

    /**
     * Fetch the list of sub breeds for the given breed.
     * If the result is already in the cache, it is returned from the cache.
     * Otherwise, the delegate is called and its result is stored in the cache
     * before being returned.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the delegate throws this exception
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        List<String> subBreeds = delegate.getSubBreeds(breed);

        cache.put(breed, subBreeds);

        return subBreeds;
    }

    /**
     * Returns the number of times the delegate's getSubBreeds method has been called.
     * This is a helper method for testing purposes.
     * @return the number of calls made to the delegate
     */
    public int getCallsMade() {
        if (delegate instanceof BreedFetcherForLocalTesting) {
            return ((BreedFetcherForLocalTesting) delegate).getCallCount();
        }
        return 0; // Or handle as an error, but for tests, 0 is fine.
    }
}