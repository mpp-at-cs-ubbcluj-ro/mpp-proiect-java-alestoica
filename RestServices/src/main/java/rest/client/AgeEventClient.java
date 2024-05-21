package rest.client;

import model.AgeEvent;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import rest.services.ServiceException;

import java.util.Collection;
import java.util.concurrent.Callable;

public class AgeEventClient {
    public static final String URL = "http://localhost:8080/competition/events";

    private final RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public int countParticipants(Long id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), int.class));
    }

    public AgeEvent findByAgeGroupAndSportsEvent(String ageGroup, String sportsEvent) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s/%s", URL, ageGroup, sportsEvent), AgeEvent.class));
    }

    public AgeEvent findOne(Long id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), AgeEvent.class));
    }

    public AgeEvent[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, AgeEvent[].class));
    }

    public AgeEvent add(AgeEvent ageEvent) {
        return execute(() -> restTemplate.postForObject(URL, ageEvent, AgeEvent.class));
    }

    public void delete(Long id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

    public void update(AgeEvent ageEvent) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, ageEvent.getId()), ageEvent);
            return null;
        });
    }
}
