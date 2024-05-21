package start;

import model.AgeEvent;
import model.AgeGroup;
import model.SportsEvent;
import org.springframework.web.client.RestClientException;
import rest.client.AgeEventClient;
import rest.services.ServiceException;

public class StartRestClient {
    private static final AgeEventClient ageEventClient = new AgeEventClient();

    public static void main(String[] args) {
        AgeEvent ageEvent = new AgeEvent(AgeGroup.GROUP_12_15_YEARS, SportsEvent.METERS_1500);

        try {
            System.out.println("Printing all events");
            show(() -> {
                AgeEvent[] ageEvents = ageEventClient.getAll();
                for (AgeEvent event : ageEvents) {
                    System.out.println(event);
                }
            });

            System.out.println("Finding the event with id 6");
            show(() -> System.out.println(ageEventClient.findOne(6L)));

            System.out.println("Finding the event with age group GROUP_12_15_YEARS and sports event METERS_1500");
            show(() -> System.out.println(ageEventClient.findByAgeGroupAndSportsEvent("GROUP_12_15_YEARS", "METERS_1500")));

            System.out.println("Counting the number of participants registered for the event with id 6");
            show(() -> System.out.println(ageEventClient.countParticipants(6L)));

            System.out.println("Deleting event with id 6");
            ageEventClient.delete(6L);

            System.out.println("Printing all events");
            show(() -> {
                AgeEvent[] ageEvents = ageEventClient.getAll();
                for (AgeEvent event : ageEvents) {
                    System.out.println(event);
                }
            });

            System.out.println("Adding event with id 6, age group GROUP_12_15_YEARS and sports event METERS_1500");
            show(() -> System.out.println(ageEventClient.add(ageEvent)));

            System.out.println("Printing all events");
            show(() -> {
                AgeEvent[] ageEvents = ageEventClient.getAll();
                for (AgeEvent event : ageEvents) {
                    System.out.println(event);
                }
            });
        } catch(RestClientException e){
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e);
        }
    }
}
