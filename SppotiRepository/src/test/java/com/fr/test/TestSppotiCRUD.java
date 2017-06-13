package com.fr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * Created by djenanewail on 1/21/17.
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = RepoTestConfig.class)
public class TestSppotiCRUD {

    @Test
    public void testSavingSppoti() {
        System.out.println(UUID.randomUUID().toString() + "-" +  UUID.randomUUID().toString());
    }
}
