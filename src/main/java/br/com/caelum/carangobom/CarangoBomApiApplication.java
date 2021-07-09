package br.com.caelum.carangobom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CarangoBomApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarangoBomApiApplication.class, args);
	}

}
