package ru.makarov.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.makarov.domain.Butterfly;
import ru.makarov.domain.Caterpillar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GateWayTest {


    @Autowired
    private CatepillarGateWay catepillarGateWay;


    @Test
    void testProcess() {
        Collection<Caterpillar> caterpillars = new ArrayList<>();

        caterpillars.add(new Caterpillar(1L, "caterpillar"));
        caterpillars.add(new Caterpillar(2L, "caterpillar"));

        List<Butterfly> butterflies = catepillarGateWay.process(caterpillars);

        Butterfly testButterfly = new Butterfly(1L, "butterfly");

        assertNotNull(butterflies);
        assertEquals(2, butterflies.size());
        assertEquals(testButterfly, butterflies.get(0));
    }
}





