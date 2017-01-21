package com.fr.test;

import com.fr.repositories.SppotiRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by djenanewail on 1/21/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSppotiCRUD {

    private SppotiRepository sppotiRepository;

    @Autowired
    public void setSppotiRepository(SppotiRepository sppotiRepository) {
        this.sppotiRepository = sppotiRepository;
    }

    @Test
    public void testSavingSppoti() {


    }
}
