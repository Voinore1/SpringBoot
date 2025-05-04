package org.example.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private CategorySeeder categorySeeder;

    @Autowired
    private ProductSeeder productSeeder;

    @Autowired
    private RoleSeeder roleSeeder;

    @Autowired
    private UserSeeder userSeeder;

    @Override
    public void run(String... args) throws Exception {
        categorySeeder.seed();
        productSeeder.seed();
        roleSeeder.seed();
        userSeeder.seed();
    }
}
