package example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CASCounter {

    private AtomicInteger atomicI = new AtomicInteger(0);   //  ��ȫ�������
    private int i = 0;  // ����ȫ�������

    // 100 ���߳�ͬʱ������ÿ���̼߳���10000��
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
        // �ȴ�ִ�����
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

    // ͨ��ѭ�� CAS ʵ��ԭ��
    private void safeCounter() {
        for(;;) {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    // ���̰߳�ȫ
    private void count() {
        i++;
    }

}
