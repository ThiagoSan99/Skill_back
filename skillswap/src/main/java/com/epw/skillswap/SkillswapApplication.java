package com.epw.skillswap;

import com.epw.skillswap.entity.Category;
import com.epw.skillswap.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SkillswapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillswapApplication.class, args);
	}

	@Bean
	CommandLineRunner seedCategories(CategoryRepository categoryRepository) {
		return args -> {
			if (categoryRepository.count() == 0) {
				List<String> names = List.of(
						"Tecnología", "Ciencias", "Idiomas",
						"Arte & Diseño", "Negocios", "Humanidades"
				);
				for (String name : names) {
					categoryRepository.save(
							Category.builder().name(name).build()
					);
				}
			}
		};
	}
}
