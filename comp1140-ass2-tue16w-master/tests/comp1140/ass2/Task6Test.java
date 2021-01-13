package comp1140.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class Task6Test {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test(String action,int a, boolean expected) {
        boolean out = Agamemnon.checkWarp(action,a,true);
        assertTrue("For input action: '" + action
                        + ", and input int : `" + a + "', expected " + expected + " but got " + out,
                out == expected);
    }

    @Test
    public void testCheckWarp() {
        String[] actions = {
                "Oa00",
                "Oa01",
                "Oa00Oj030004",
                "Oa00Oj020008",
                "Oj000306Oj030004",
                "Oj000305Oj030004"
        };

        test(actions[0],0,true);
        test(actions[1],0,true);
        test(actions[2],5,true);
        test(actions[3],5,false);
        test(actions[4],1,false);
        test(actions[4],9,true);
        test(actions[5],1,true);
        test(actions[5],9,true);
    }
}
