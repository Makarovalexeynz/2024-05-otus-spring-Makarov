package ru.makarov.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import ru.makarov.domain.Butterfly;
import ru.makarov.domain.Caterpillar;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> caterpillarChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> butterflyChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlow.from(caterpillarChannel())
                .split()
                .<Caterpillar, Butterfly>transform(caterpillar ->
                        new Butterfly(caterpillar.getId(), "butterfly"))
                .<Butterfly>log(LoggingHandler.Level.INFO, "Butterfly", Message::getPayload)
                .aggregate()
                .channel(butterflyChannel())
                .get();
    }
}
