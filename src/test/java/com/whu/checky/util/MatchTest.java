package com.whu.checky.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MatchTest {
    @Autowired
    Match cls;

    String testUserId = "oM2yQ4jR0La_jZ8hyxkERsqNTh_8";

    @Test
    public void acqCandidateTest() throws Exception{
        Assert.assertEquals(0, cls.acqCandidate(testUserId).size());
    }

    @Test
    public void strgCandidateTest() throws Exception{
        Assert.assertEquals(10, cls.strgCandidate(testUserId).size());
    }

    @Test
    public void randCandidateTest() throws Exception{
        Assert.assertEquals(10, cls.randCandidate(testUserId).size());
    }

    @Test
    public void areaCandidateTest() throws Exception{
        Assert.assertEquals(10, cls.areaCandidate(testUserId).size());
    }

    @Test
    public void hobbyCandidateTest() throws Exception{
        Assert.assertEquals(0, cls.hobbyCandidate(testUserId).size());
    }

    @Test
    public void matchTest() throws Exception{
        MatchType type = new MatchType();
        type.isAcq = true;
        type.isStrg = true;
        type.isRand = true;
        type.isArea = true;
        type.isHobby = true;

        Assert.assertEquals(5, cls.match(testUserId, type, 5).size());
    }
}