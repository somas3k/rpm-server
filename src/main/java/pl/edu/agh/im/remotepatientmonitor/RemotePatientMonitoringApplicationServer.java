package pl.edu.agh.im.remotepatientmonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.im.remotepatientmonitor.auth.UserRepository;
import pl.edu.agh.im.remotepatientmonitor.domain.ApplicationUser;
import pl.edu.agh.im.remotepatientmonitor.domain.Device;
import pl.edu.agh.im.remotepatientmonitor.domain.HeartRateRecord;
import pl.edu.agh.im.remotepatientmonitor.monitoring.DeviceRepository;
import pl.edu.agh.im.remotepatientmonitor.monitoring.HeartRateRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.edu.agh.im.remotepatientmonitor.auth.SecurityConstants.MAIL_ACTIVATION_EXPIRATION_TIME;

@SpringBootApplication
@Component
public class RemotePatientMonitoringApplicationServer implements ApplicationRunner {

	private static final Logger LOG =
			LoggerFactory.getLogger(RemotePatientMonitoringApplicationServer.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(RemotePatientMonitoringApplicationServer.class);
		Optional.ofNullable(System.getenv("PORT")).ifPresent(port -> app.setDefaultProperties(Collections.singletonMap("server.port", port)));
		app.run(args);
	}

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	@Autowired
	private HeartRateRepository heartRateRepository;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("RPM welcome");
		scheduler.scheduleAtFixedRate(this::task, 1, 5, TimeUnit.MINUTES);
		if(userRepository.findByEmail("test@test.com") == null) {
			ApplicationUser user = new ApplicationUser(null, "test@test.com", "test", bCryptPasswordEncoder.encode("test"), true, null);
			userRepository.save(user);
			Device device = new Device("12341234", user, "Samsung Galaxy Watch");
			deviceRepository.save(device);
			HeartRateRecord record = new HeartRateRecord(70, Timestamp.from(Instant.now()));
			record.setDevice(device);
			heartRateRepository.save(record);
		}
	}

	@Transactional
	public void task() {
			userRepository.deleteAll(userRepository.searchExpired().stream().filter(user ->
					(System.currentTimeMillis() - user.getEmailVerificationExpirationTime()) >= MAIL_ACTIVATION_EXPIRATION_TIME)
					.collect(Collectors.toList()));
	}

}
