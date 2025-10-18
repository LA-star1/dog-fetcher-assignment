package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // 1. 构造 API 请求的 URL
        String url = "https://dog.ceo/api/breed/" + breed.toLowerCase() + "/list";

        // 2. 创建请求对象
        Request request = new Request.Builder()
                .url(url)
                .build();

        // 3. 使用 try-catch 块来处理所有可能发生的错误
        try (Response response = client.newCall(request).execute()) {
            // 如果 HTTP 响应码不是 2xx (比如 404, 500)，说明请求本身就失败了
            if (!response.isSuccessful()) {
                throw new BreedNotFoundException();
            }

            // 4. 获取响应体，并解析成 JSON 对象
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

            // 5. 检查 JSON 中的 "status" 字段
            String status = jsonObject.getString("status");
            if ("error".equals(status)) {
                // 如果 API 明确返回 "error"，说明犬种不存在
                throw new BreedNotFoundException();
            }

            // 6. 如果 status 是 "success"，就解析 "message" 数组
            JSONArray subBreedsJsonArray = jsonObject.getJSONArray("message");
            List<String> subBreedsList = new ArrayList<>();
            for (int i = 0; i < subBreedsJsonArray.length(); i++) {
                subBreedsList.add(subBreedsJsonArray.getString(i));
            }
            return subBreedsList;

        } catch (Exception e) {
            // 这个 catch 块会捕获所有其他异常，比如网络不通 (IOException)
            // 或者 JSON 格式错误 (JSONException)。根据作业要求，任何失败
            // 都必须报告为 BreedNotFoundException。
            throw new BreedNotFoundException();
        }
    }
}