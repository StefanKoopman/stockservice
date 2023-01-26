package nl.intergamma.stockservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Stock API", version = "0.1", description = "API to keep track of stock information"))
public class StockserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockserviceApplication.class, args);
	}

}
