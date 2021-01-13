package comp1140.ass2;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class Task7Test {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    private void test_1(ArrayList<String> list, String begin, String end,int[] expected) {
        int[] out = Agamemnon.findPosition(list,begin,end);
        assertTrue("For input list: '" + list + "', begin: '" + begin + "', end: '" + end
                        + "', expected " + Arrays.toString(expected) + " but got " + Arrays.toString(out),
                Arrays.equals(out, expected));
    }
    @Test
    public void test_find() {
        ArrayList<String> list = new ArrayList<>();
        list.add("S0001");
        list.add("S0004");
        list.add("F0105");
        list.add("L0204");
        list.add("L0203");

        int[] a = {0,2};
        test_1(list,"0001","0105",a);
        int[] a_ = {2,0};
        test_1(list,"0105","0001",a_);
        int[] b = {1,3};
        test_1(list,"0004","0204",b);
        int[] b_ = {3,1};
        test_1(list,"0204","0004",b_);
        int[] c = {0,4};
        test_1(list,"0001","0203",c);
        int[] c_ = {4,0};
        test_1(list,"0203","0001",c_);
        int[] d = {2,4};
        test_1(list,"0105","0203",d);
        int[] d_ = {4,2};
        test_1(list,"0203","0105",d_);
    }
}
