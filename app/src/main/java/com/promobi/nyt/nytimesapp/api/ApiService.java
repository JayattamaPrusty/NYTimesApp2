package com.promobi.nyt.nytimesapp.api;

import com.promobi.nyt.nytimesapp.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Jaya on 31/05/18.
 */

public interface ApiService {

    // NYTimes Article Search API
    // Documentation:
    //   - http://developer.nytimes.com/article_search_v2.json
    //   - https://github.com/NYTimes/public_api_specs/tree/master/article_search
    // NYT article search API returns maximum 10 results at a time (page 0 = 1-10, page 1 = 11-20, etc.)
    // The highest page number that can be requested is 120
    String API_KEY = "0312094757e74d13b16cdfa3a0313ce9";
    String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    String API_IMAGE_BASE_URL = "http://www.nytimes.com/";

    @GET("articlesearch.json")
    Call<ApiResponse> query(
            @Query("q") String query,
            @Query("fq") String filteredQuery,
            @Query("begin_date") String beginDate,
            @Query("end_date") String endDate,
            @Query("sort") String sort,
            @Query("page") Integer page);
}
