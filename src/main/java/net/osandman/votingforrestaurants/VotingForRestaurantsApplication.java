package net.osandman.votingforrestaurants;

import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class VotingForRestaurantsApplication implements ApplicationRunner {

    private final PersonRepository personRepository;
    private final RestaurantRepository restaurantRepository;

    public static void main(String[] args) {
        SpringApplication.run(VotingForRestaurantsApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Person> people = personRepository.findAll();
        people.forEach(System.out::println);

        System.out.println(personRepository.findById(100000));
        restaurantRepository.findAll().forEach(System.out::println);
    }
}
