package nl.ing.mortgage.assessment.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({ "nl.ing.mortgage.assessment.demo.repository" })
public class DatabaseConfiguration {

}
