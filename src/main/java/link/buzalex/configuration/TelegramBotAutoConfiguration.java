package link.buzalex.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@EnableConfigurationProperties(TelegramBotProperties.class)
@ComponentScan("link.buzalex")
public class TelegramBotAutoConfiguration {
}
