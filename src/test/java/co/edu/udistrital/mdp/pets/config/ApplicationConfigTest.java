package co.edu.udistrital.mdp.pets.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class ApplicationConfigTest {

    private final ApplicationConfig config = new ApplicationConfig();

    @Test
    void testModelMapperBean() {
        ModelMapper mapper = config.modelMapper();

        assertNotNull(mapper);
    }

    @Test
    void testCorsConfigurerBean() {
        WebMvcConfigurer configurer = config.corsConfigurer();

        assertNotNull(configurer);

        // Ejecuta el método addCorsMappings del bean.
        CorsRegistry registry = new CorsRegistry();
        configurer.addCorsMappings(registry);

        assertNotNull(registry);
    }
}