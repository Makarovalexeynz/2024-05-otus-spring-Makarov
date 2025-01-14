package ru.makarov.services;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.makarov.domain.Butterfly;
import ru.makarov.domain.Caterpillar;

import java.util.Collection;
import java.util.List;

@MessagingGateway
public interface CatepillarGateWay {

    @Gateway(requestChannel  = "caterpillarChannel", replyChannel = "butterflyChannel")
    List<Butterfly> process(Collection<Caterpillar> caterpillars);
}
