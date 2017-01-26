package couchbase;

import couchbase.config.MyCouchbaseConfiguration;
import couchbase.repository.UserInfoRepository;
import couchbase.service.MyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Tushar Chokshi @ 1/25/17.
 */
@Configuration
@Import({MyCouchbaseConfiguration.class})
//@ComponentScan
public class ApplicationConfiguration {

    @Bean
    public MyService myService(UserInfoRepository userInfoRepository) {
        return new MyService(userInfoRepository);
    }

}
