package by.itstep.organizaer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(prefix = "project")
//@PropertySource(value = "classpath:project.yml")
@Component
public class ProjectConfiguration {

    private String formatDate;

    private String formatDateTime;
}
