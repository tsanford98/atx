package com.thomas.atx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@RestController
public class RestaurantInspectionController {
    @GetMapping("/food-inspection")
    public List<RestaurantInspection> getFoodInspections(@RequestParam(required = false) String name) throws URISyntaxException, IOException, InterruptedException {
        try {
            Gson gson = new Gson();      
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://data.austintexas.gov/resource/ecmv-9xxi.json?$limit=100000"))
                .GET()
                .build();
            
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            // Deserialize directly into List<RestaurantInspection>
            List<RestaurantInspection> restaurantInspections = gson.fromJson(response.body(), new TypeToken<List<RestaurantInspection>>(){}.getType());
			if (name != null) {
				List<RestaurantInspection> newRestaurants = new ArrayList<RestaurantInspection>();
                name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
				for (RestaurantInspection restaurant : restaurantInspections) {
					if (restaurant.restaurant_name().contains(name)) {
						newRestaurants.add(restaurant);
					}
				}
				return newRestaurants;
			} else {
				return restaurantInspections;
			}
        } catch (JsonSyntaxException e) {
            // Handle JSON parsing error
            e.printStackTrace();
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
}
