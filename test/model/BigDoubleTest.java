package model;

import org.junit.jupiter.api.Test;

public class BigDoubleTest {

    @Test
    public void test() {
        BigDouble first = new BigDouble(10);

        BigDouble second = new BigDouble(20);

        double result = second.add(first).doubleValue();

        System.out.println(result);
    }

}
