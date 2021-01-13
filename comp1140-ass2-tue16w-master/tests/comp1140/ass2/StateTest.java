package comp1140.ass2;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

/** StateTest -- our own tests for checking:
 * 1. isStateWellFormed
 * 2. isStateValid
 *
 * Through this process, we can understand how test works
 * and how to write good tests.
 *
 */
public class StateTest {

    private static String EDGE_MAP = new String(
            "S0001S0004F0105L0204F0206L0203L0306S0307L0408S0409S0510F0508F0611S0712F0813S0809S0911S1015F1114L1112S1216F1217S1315F1314L1418L1419F1520L1619S1617F1722L1820L1823S1924F1921F2025L2126F2122L2226F2325F2324F2427S2428L2529L2628L2729L2728S2831S2930S3031"
    );

    @Rule
    public Timeout timeout = Timeout.millis(1000);

    @Before
    public void setUp() throws Exception {
        // String[] input;
        // boolean expect;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void wellFormedStateTest() {
        // Test rule 1
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {}));
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30"}));
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30","Bb30","Bb30"}));
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", null}));
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {null, EDGE_MAP}));
        assertFalse("not well formed -- rule1 failed", Agamemnon.isStateWellFormed(new String[] {null,null}));
        assertTrue("well formed -- rule1", Agamemnon.isStateWellFormed(new String[] {"Bb30", EDGE_MAP}));

        // Test rule 2
        assertFalse("not well formed -- rule2 failed", Agamemnon.isStateWellFormed(new String[] {"Bb301", EDGE_MAP.concat("012")}));
        assertFalse("not well formed -- rule2 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", EDGE_MAP.concat("012")}));
        assertFalse("not well formed -- rule2 failed", Agamemnon.isStateWellFormed(new String[] {"Bb301", EDGE_MAP}));
        assertTrue("well formed -- rule2", Agamemnon.isStateWellFormed(new String[] {"Bb30", EDGE_MAP}));

        // Test rule 3
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Xb30", EDGE_MAP}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bz30", EDGE_MAP}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bbii", EDGE_MAP}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb8i", EDGE_MAP}));

        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", "T0001"}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", "Si001"}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", "S0i01"}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", "S00i1"}));
        assertFalse("not well formed -- rule3 failed", Agamemnon.isStateWellFormed(new String[] {"Bb30", "S000i"}));
    }

    @Test
    public void isStateValid() {
        // Test rule 1
        assertFalse("State not valid -- rule1 failed", Agamemnon.isStateValid(new String[] {"Of04Bc04Bf09Oc11Oj13Bh14", EDGE_MAP}));

        // Test rule 2
        assertFalse("State not valid -- rule2 failed", Agamemnon.isStateValid(new String[] {"Of04Bc05Bf35Oc11Oj13Bh14", EDGE_MAP}));

        // Test rule 3
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Oa04Oa05", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Ob04Ob05", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Oh04Oh05", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Ba04Ba05", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Oi04Oi05Oi06", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Bi04Bi05Bi06", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Of04Of05Of06Of07", EDGE_MAP}));
        assertFalse("State not valid -- rule3 failed", Agamemnon.isStateValid(new String[] {"Bf04Bf05Bf06Bf07", EDGE_MAP}));

        // Test rule 4
        assertTrue("State not valid -- rule1 failed", Agamemnon.isStateValid(new String[] {"Of04Bc08Bf09Oc11Oj13Bh14", EDGE_MAP}));
        assertFalse("State not valid -- rule1 failed", Agamemnon.isStateValid(new String[] {"Of04Bc08Bf09Oc11Bj13Bh14", EDGE_MAP}));
    }
}