package nl.ing.mortgage.assessment.demo;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

	private final Environment env;

	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	private static final String DEV = "dev";
	private static final String PROD = "prod";

	public DemoApplication(Environment env) {
		this.env = env;
	}

	/**
	 * Initializes app.
	 */
	@PostConstruct
	public void initApplication() {
		Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
		if (
				activeProfiles.contains(DEV) &&
						activeProfiles.contains(PROD)
		) {
			LOG.error(
					"dev and prod profiles can not be activated at the same time."
			);
		}
	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(DemoApplication.class);
		addDefaultProfile(app);
		Environment env = app.run(args).getEnvironment();
		logApplicationStartup(env);
	}

	/**
	 * Load 'dev' profiles when nothing is selected
	 * @param app
	 */
	public static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defProperties = new HashMap<>();
		defProperties.put(SPRING_PROFILE_DEFAULT, DEV);
		app.setDefaultProperties(defProperties);
	}

	private static void logApplicationStartup(Environment env) {
		String applicationName = env.getProperty("spring.application.name");
		String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
		String serverPort = env.getProperty("server.port");
		String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
				.filter(StringUtils::isNotBlank)
				.orElse("/");
		LOG.info(
				"""
    
                ----------------------------------------------------------
                \tApplication '{}' is running:
                \t{}://localhost:{}{}
                \tActive Profile(s): \t{}
                ----------------------------------------------------------""",
				applicationName,
				protocol,
				serverPort,
				contextPath,
				env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
		);
	}
}
