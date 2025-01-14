package ru.makarov.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.makarov.domain.Butterfly;
import ru.makarov.domain.Caterpillar;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatepillarServiceImpl implements CatepillarService {

    private final CatepillarGateWay catepillarGateWay;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    private long nextId = 1;


    @Override
    public void startGenerateOrderLoop() {
        int numCaterpillars = randomGenerator.nextInt(50, 100);

        List<Caterpillar> caterpillars = new ArrayList<>(numCaterpillars);

        for (int i = 0; i < numCaterpillars; i++) {
            caterpillars.add(generateCaterpillar());
        }

        if (!caterpillars.isEmpty()) {
            log.info("Sending {} caterpillars for processing", caterpillars.size());
            List<Butterfly> butterflies = catepillarGateWay.process(caterpillars);
            log.info("Received {} butterflies after processing", butterflies.size());
        }
    }

    private Caterpillar generateCaterpillar() {
        return new Caterpillar(nextId++, "caterpillar");
    }

}
