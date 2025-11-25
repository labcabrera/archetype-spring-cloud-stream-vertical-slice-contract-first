package org.labcabrera.sample.archetype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SampleArchetypeCloudStream {

	public static void main(String[] args) {
		SpringApplication.run(SampleArchetypeCloudStream.class, args);
	}

}
