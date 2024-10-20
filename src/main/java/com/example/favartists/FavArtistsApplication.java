package com.example.favartists;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class FavArtistsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FavArtistsApplication.class, args);
	}

}
