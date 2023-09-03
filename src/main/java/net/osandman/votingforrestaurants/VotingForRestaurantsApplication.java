package net.osandman.votingforrestaurants;

import lombok.AllArgsConstructor;
import net.osandman.votingforrestaurants.entity.Menu;
import net.osandman.votingforrestaurants.entity.Person;
import net.osandman.votingforrestaurants.repository.MenuRepository;
import net.osandman.votingforrestaurants.repository.PersonRepository;
import net.osandman.votingforrestaurants.repository.RestaurantRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class VotingForRestaurantsApplication implements ApplicationRunner {

    private final PersonRepository personRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public static void main(String[] args) {
        SpringApplication.run(VotingForRestaurantsApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println(personRepository.findPersonByIdWithVotes(100000));

        System.out.println(restaurantRepository.findRestaurantWithMenu(100005));

        Menu menu = menuRepository.findMenuByIdWithMenuItems(100008).get();
        System.out.println(menu);
        menu.getMenuItems().forEach(System.out::println);

        Person admin = personRepository.findPersonByEmailIgnoreCase("admin@ya.ru").get();
        System.out.printf("name=%s, roleTypes=%s\n", admin.getName(), admin.getRoles());
    }
}
