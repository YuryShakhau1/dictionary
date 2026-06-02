package by.shakhau.dictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class DictionaryApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DictionaryApplication.class, args);
    }
}
