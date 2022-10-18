package pk.rafi234.dogly;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class DoglyApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoglyApplication.class, args);
	}

}
