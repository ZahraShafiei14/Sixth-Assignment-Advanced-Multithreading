package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    private static BigDecimal pi = new BigDecimal("0");

    private static class BBPAlgorithm implements Runnable {

        private final int startPoint;
        private final MathContext mc = new MathContext(1001);

        public BBPAlgorithm(int startPoint) {
            this.startPoint = startPoint;
        }
        @Override
        public void run() {
            BigDecimal sum = new BigDecimal("0");
            for (int i = startPoint; i < startPoint + 50; i++) {
                BigDecimal a = new BigDecimal("0.0625").pow(i);
                BigDecimal b = new BigDecimal("4").divide(new BigDecimal((8*i+1)), mc);
                BigDecimal c = new BigDecimal("-2").divide(new BigDecimal((8*i+4)), mc);
                BigDecimal d = new BigDecimal("-1").divide(new BigDecimal((8*i+5)), mc);
                BigDecimal e = new BigDecimal("-1").divide(new BigDecimal((8*i+6)), mc);
                sum = sum.add(a.multiply(b.add(c, mc).add(d).add(e), mc));
            }
            Sum(sum);
        }

        private static synchronized void Sum(BigDecimal n) {
            pi = pi.add(n);
        }
    }

    public String calculate(int floatingPoint)
    {
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
        PiCalculator pi = new PiCalculator();
        System.out.println(pi.calculate(2));
    }
}