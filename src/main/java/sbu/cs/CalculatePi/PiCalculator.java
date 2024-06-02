package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    private static BigDecimal pi = new BigDecimal("0");
    private static boolean calculated = false;

    private static class BBPAlgorithm implements Runnable {

        private final int start;
        private final MathContext mc = new MathContext(2000);

        public BBPAlgorithm(int s) {
            this.start = s;
        }

        @Override
        public void run() {
            if (!calculated) {
                BigDecimal sum = new BigDecimal("0");
                for (int i = start; i < start + 50; i++) {
                    BigDecimal d1 = new BigDecimal("0.0625").pow(i);
                    BigDecimal d2 = new BigDecimal("4").divide(new BigDecimal((8 * i + 1)), mc);
                    BigDecimal d3 = new BigDecimal("-2").divide(new BigDecimal((8 * i + 4)), mc);
                    BigDecimal d4 = new BigDecimal("-1").divide(new BigDecimal((8 * i + 5)), mc);
                    BigDecimal d5 = new BigDecimal("-1").divide(new BigDecimal((8 * i + 6)), mc);
                    sum = sum.add(d1.multiply(d2.add(d3, mc).add(d4).add(d5), mc));
                }
                Sum(sum);
            }
        }

        private static synchronized void Sum(BigDecimal n) {
            pi = pi.add(n);
            calculated = true;
        }
    }

    public String calculate(int floatingPoint) {
        ExecutorService pool = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i++) {
            BBPAlgorithm num = new BBPAlgorithm(50 * i);
            pool.execute(num);
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.getMessage();
        }

        return pi.toString().substring(0, floatingPoint + 2);
    }

    public static void main(String[] args) {

    }
}
