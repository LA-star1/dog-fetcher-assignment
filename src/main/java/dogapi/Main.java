package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        BreedFetcher fetcher = new CachingBreedFetcher(new DogApiBreedFetcher());

        String breed = "hound";
        System.out.println(breed + " has " + getNumberOfSubBreeds(breed, fetcher) + " sub breeds.");

        System.out.println("Fetching for hound again (should use cache)...");
        System.out.println(breed + " has " + getNumberOfSubBreeds(breed, fetcher) + " sub breeds.");

        String invalidBreed = "notadog";
        System.out.println("Attempting to fetch invalid breed '" + invalidBreed + "'...");

        System.out.println(invalidBreed + " has " + getNumberOfSubBreeds(invalidBreed, fetcher) + " sub breeds.");
    }

    public static int getNumberOfSubBreeds(String breed, BreedFetcher fetcher) {
        try {
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            return subBreeds.size();
        } catch (BreedFetcher.BreedNotFoundException e) {

            return 0;
        }
    }
}