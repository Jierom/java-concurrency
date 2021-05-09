package example;

public class ConcurrencyTest {

    // 次数少，串行快
    // 次数多，并行快
    private static final long count = 100000000;

    public static  void main(String[] args) throws InterruptedException {
        concurrency();
        serial();
    }

    // 并行代码
    private static void concurrency() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int a = 0;
                for (long i=0; i<count; i++) {
                    a += 1;
                }
            }
        });
        thread.start();
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        thread.join();
        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency:" + time + "ms, b=" + b);
    }

    // 串行代码
    private static void serial() {
        long start = System.currentTimeMillis();
        int a = 0;
        for (long i=0; i<count; i++) {
            a += 1;
        }
        int b = 0;
        for (long i = 0; i < count; i++) {
            b--;
        }
        long time = System.currentTimeMillis() - start;
        System.out.println("serial:" + time + "ms, b=" + b);
    }
}
