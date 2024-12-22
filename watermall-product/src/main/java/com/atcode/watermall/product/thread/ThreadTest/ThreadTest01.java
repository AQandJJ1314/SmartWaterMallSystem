package com.atcode.watermall.product.thread.ThreadTest;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 初始化线程的4种方式
 * 1.1 继承 Thread类，重写run()方法
 * public static class ThreadTest001 extends Thread{
 *         @Override
 *         public void run() {
 *             System.out.println("当前线程id:  "+Thread.currentThread().getId());
 *             int i = 10/2;
 *             System.out.println("当前线程的运行结果:  "+ i);
 *
 *         }
 *
 *   public static void main(String[] args) {
 *
 *         ThreadTest001 thread = new ThreadTest001();
 *         thread.start();
 *
 *     }
 * 1.2 实现 Runnable 接口，重写run()方法
 *     public static class ThreadTest002 implements Runnable{
 *
 *         @Override
 *         public void run() {
 *             System.out.println("当前线程id:  "+Thread.currentThread().getId());
 *             int i = 10/2;
 *             System.out.println("当前线程的运行结果:  "+ i);
 *
 *         }
 *     }
 *
 *      public static void main(String[] args) {
 *
 *         System.out.println("main...start...");
 *         Runnable runnable = new ThreadTest002();
 *
 *         new Thread(runnable).start();
 *         System.out.println("main...end...");
 *
 *     }
 * 1.3 实现 Callable 接口 ， FutureTask （可以拿到返回结果， 可以处理异常）
 *      public static class ThreadTest003 implements Callable<Integer> {
 *
 *         @Override
 *         public Integer call() throws Exception {
 *             System.out.println("当前线程id:  "+Thread.currentThread().getId());
 *             int i = 10/2;
 *             System.out.println("当前线程的运行结果:  "+ i);
 *
 *             return i;
 *         }
 *     }
 *      public static void main(String[] args) throws ExecutionException, InterruptedException {
 *
 *         System.out.println("main...start...");
 *         FutureTask<Integer> futureTask = new FutureTask<>(new ThreadTest003());
 *         new Thread(futureTask).start();
 *         Integer integer = futureTask.get();  //阻塞方法，等线程执行完之后再执行
 *         System.out.println("main...end..."+integer);
 *
 *     }
 * 1.4 创建线程池直接提交任务（推荐）
 * 四种创建线程方法的区别
 * 区别：
 * 1、2不能得到返回值。3可以获取返回值
 * 1、2、3都不能控制资源
 * 4可以控制资源，性能稳定，不会一下子所有线程一起运行
 * 总结：
 * 1、实际开发中，只用线程池【高并发状态开启了n个线程，会耗尽资源】
 * 2、当前系统中线程池只有一两个，每个异步任务提交给线程池让他自己去执行
 */
public class ThreadTest01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println("main...start...");
        FutureTask<Integer> futureTask = new FutureTask<>(new ThreadTest003());
        new Thread(futureTask).start();
        Integer integer = futureTask.get();  //阻塞方法，等线程执行完之后再执行
        System.out.println("main...end..."+integer);

    }

    public static class ThreadTest001 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程id:  "+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前线程的运行结果:  "+ i);

        }
    }
    public static class ThreadTest002 implements Runnable{

        @Override
        public void run() {
            System.out.println("当前线程id:  "+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前线程的运行结果:  "+ i);

        }


    }
    public static class ThreadTest003 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程id:  "+Thread.currentThread().getId());
            int i = 10/2;
            System.out.println("当前线程的运行结果:  "+ i);

            return i;
        }
    }

}
