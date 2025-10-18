package dogapi;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        // 将真正的 API Fetcher 用缓存包装器包裹起来
        BreedFetcher fetcher = new CachingBreedFetcher(new DogApiBreedFetcher());

        // main 方法不是测试的重点，但我们仍然让它正确地工作
        String breed = "hound";
        System.out.println(breed + " has " + getNumberOfSubBreeds(breed, fetcher) + " sub breeds.");

        // 再次调用，验证缓存
        System.out.println("Fetching for hound again (should use cache)...");
        System.out.println(breed + " has " + getNumberOfSubBreeds(breed, fetcher) + " sub breeds.");

        // 测试一个不存在的犬种
        String invalidBreed = "notadog";
        System.out.println("Attempting to fetch invalid breed '" + invalidBreed + "'...");
        // 因为 getNumberOfSubBreeds 会返回 0，所以这里不会抛出异常
        System.out.println(invalidBreed + " has " + getNumberOfSubBreeds(invalidBreed, fetcher) + " sub breeds.");
    }

    /**
     * 返回给定犬种的亚种数量。
     * @param breed 要查询的犬种
     * @param fetcher 要使用的 BreedFetcher
     * @return 亚种的数量。如果犬种不存在或没有亚种，则返回 0。
     */
    public static int getNumberOfSubBreeds(String breed, BreedFetcher fetcher) {
        try {
            // 尝试获取亚种列表
            List<String> subBreeds = fetcher.getSubBreeds(breed);
            // 如果成功，返回列表的大小
            return subBreeds.size();
        } catch (BreedFetcher.BreedNotFoundException e) {
            // 如果 fetcher 抛出了“未找到”异常，我们就捕获它
            // 并按照测试的要求，返回 0。
            return 0;
        }
    }
}