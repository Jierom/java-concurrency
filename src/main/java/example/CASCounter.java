package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CASCounter {

    private AtomicInteger atomicI = new AtomicInteger(0);   //  安全计数结果
    private int i = 0;  // 不安全计数结果

    // 100 个线程同时计数，每个线程计数10000次
    public static void main(String[] args) {
        final CASCounter cas = new CASCounter();
        List<Thread> ts = new ArrayList<Thread>(100);
        long start = System.currentTimeMillis();
        for (int j=0; j<100; j++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0; i<10000; i++) {
                        cas.count();
                        cas.safeCounter();
                    }
                }
            });
            ts.add(t);
        }
        for (Thread t : ts) {
            t.start();
        }
        // 等待执行完毕
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomicI.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    // 通过循环 CAS 实现原子
    private void safeCounter() {
        for(;;) {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    // 非线程安全
    private void count() {
        i++;
    }

}
