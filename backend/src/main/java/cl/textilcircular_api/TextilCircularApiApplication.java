package cl.textilcircular_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
@EnableAsync
public class TextilCircularApiApplication {

	public static void main(String[] args) throws IOException {
		cargarWalletDesdeClasspath();
		SpringApplication.run(TextilCircularApiApplication.class, args);
	}

	private static void cargarWalletDesdeClasspath() throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:wallet/*");

		if (resources.length == 0) {
			throw new IllegalStateException("No se encontraron archivos en src/main/resources/wallet");
		}

		Path tempWalletDir = Files.createTempDirectory("oracle-wallet-");
		tempWalletDir.toFile().deleteOnExit();

		for (Resource resource : resources) {
			if (resource.isReadable() && resource.getFilename() != null) {
				try (InputStream is = resource.getInputStream()) {
					Path destino = tempWalletDir.resolve(resource.getFilename());
					Files.copy(is, destino, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}

		System.setProperty("oracle.net.tns_admin", tempWalletDir.toAbsolutePath().toString());
	}

}
