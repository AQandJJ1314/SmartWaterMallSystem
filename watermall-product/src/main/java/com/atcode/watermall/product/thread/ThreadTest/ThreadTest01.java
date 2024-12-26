package com.atcode.watermall.product.thread.ThreadTest;


import java.util.concurrent.*;

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
    public  void thread01(String[] args) throws ExecutionException, InterruptedException {
        /**
         * 线程池[ExecutorService]
         * 创建线程池来执行异步任务
         * 方式1 ：Executors
         * 方式2 ：new ThreadPoolExecutor()
         */

        /**
         * 线程池七大参数
         * 1.corePoolSize[5]:  核心线程数[一直存在除非设置了allowCoreThreadTimeOut]  线程创建好以后准备就绪的线程数量，就等待来接受异步任务去执行。
         *                      5个 Thread thread = new Thread();
         * 2.maximumPoolSize[200]:  最大线程数量 控制资源
         * 3.keepAliveTime:  存活时间  如果当前线程的数量大于核心线程(core)的数量
         *                   释放空闲的线程(maximumPoolSize-corePoolSize),只要空闲线程大于指定的keepAliveTime
         * 4.unit: 时间单位
         * 5.BlockingQueue<Runnable> workQueue: 阻塞队列。如果任务有很多，就会把多的任务放到队列里。
         *                                      只要有线程空闲，就会去队列里面取出新的任务继续执行
         * 6.ThreadFactory threadFactory: 线程的创建工厂
         * 7.RejectedExecutionHandler handler: 如果队列满了，按照我们的拒绝策略拒绝执行任务
         *
         * 执行顺序:
         * 1.线程池创建，准备好core数量的核心线程，准备接受任务.
         * 2.core满了之后，就将新来的任务放到阻塞队列中，空闲的core就会自己去阻塞队列中获取任务执行
         * 3.阻塞队列满了就会开新线程执行，最大能开到maximumPoolSize所指定的数量
         * 4.maximumPoolSize满了之后就执行RejectedExecutionHandler拒绝策略拒绝任务
         * 5.max线程都执行完任务进入空闲，在等待keepAliveTime时间之后，被释放，释放数量应该为maximumPoolSize-corePoolSize
         *            new LinkedBlockingDeque<>(),默认是Integer的最大值，但是会内存不够 里面的参数可以使用压力测试之后能达到的峰值
         * 6.ThreadFactory可以使用默认的线程工厂，内部做的处理是将Runnable封装成Thread
         * 7.丢弃策略：后续补充  默认的丢弃策略 new ThreadPoolExecutor.AbortPolicy()
         */

        /**
         * 丢弃策略：
         * RejectedExecutionHandler handler：拒绝策略。如果任务队列和最大线程数量满了，按照指定的拒绝策略执行任务。
         * Rejected：丢弃最老的
         * Caller：调用者同步调用，直接调用run方法，不创建线程了
         * Abort （默认）：直接丢弃新任务
         * Discard：丢弃新任务，并且抛出异常
         *
         * 1、丢弃最老的 Rejected
         * 2、调用者同步调用，直接调用run方法，不创建线程了 Caller
         * 3、直接丢弃新任务 Abort 【默认使用这个】
         * 4、丢弃新任务，并且抛出异常 Discard
         *
         */

        /**
         * 一个线程池 core 7； max 20 ， queue： 50， 100 并发进来怎么分配的；
         * 先有 7 个能直接得到执行， 接下来 50 个进入队列排队， 在多开 13 个继续执行。 现在 70 个任务已经被安排上了，剩下 30 个默认拒绝策略
         */

        /**
         *  使用线程池的好处
         * 1、降低资源的消耗【减少创建销毁线程的开销】
         * 通过重复利用已经创建好的线程降低线程的创建和销毁带来的损耗
         *
         * 2、提高响应速度【控制线程个数】
         * 因为线程池中的线程数没有超过线程池的最大上限时,有的线程处于等待分配任务的状态，当任务来时无需创建新的线程就能执行
         *
         * 3、提高线程的可管理性【例如系统中可以创建两个线程池，核心线程池、非核心线程池【短信等】，关闭非核心线程池释放内存资源】
         * 线程池会根据当前系统特点对池内的线程进行优化处理，减少创建和销毁线程带来的系统开销。无限的创建和销毁线程不仅消耗系统资源，还降低系统的稳定性，使用线程池进行统一分配
         */

        /**
         * 执行器工具类的4种线程池
         * 1、newCachedThreadPool：缓存线程池。核心线程数是0，如果空闲会回收所有线程
         *
         * 创建一个可缓存线程池， 如果线程池长度超过处理需要， 可灵活回收空闲线程， 若无可回收， 则新建线程。
         *
         * 2、newFixedThreadPool：固定大小的线程池。核心线程数 = 最大线程数，【不回收】
         *
         *  创建一个定长线程池， 可控制线程最大并发数， 超出的线程会在队列中等待。
         *
         * 3、newScheduledThreadPool：定时任务线程池。多久之后执行【可提交核心线程数，最大线程数是Integer.Max】
         *
         *  创建一个定长线程池， 支持定时及周期性任务执行。
         *
         * 4、newSingleThreadPool：单线程化的线程池。核心与最大都只有一个【不回收】,后台从队列中获取任务
         *
         *  创建一个单线程化的线程池， 它只会用唯一的工作线程来执行任务， 保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

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


    /**
     * CompletableFuture 异步编排
     * 当我们在异步任务编程的时候，可能会有场景如下：
     *
     * 可能你会想到用之前我们学到的Callable的方式去获取结果后，再执行；
     *
     * 但是这样子不能保证是异步与异步之间的结果；
     *
     * C不能感知到AB的结果后再异步执行；
     *
     * 所以，这里我们就引出了 CompletableFuture
     *
     * Future：可以获取到异步结果
     *
     */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);


        //不带返回值的runAsync
//        CompletableFuture.runAsync(()->{
//            System.out.println("当前线程id:  "+Thread.currentThread().getId());
//            int i = 10/2;
//            System.out.println("当前线程的运行结果:  "+ i);
//        },executor);

        /**
         * whenComplete 方法完成后的感知  成功时完成回调 只能感知结果不能处理
         * handle  能感知结果，异常，处理结果  （方法执行完之后的处理，无论是成功完成还是失败完成）
         * thenApply thenRun  线程串行化  加Async指新开一个线程执行，不加指共用一个线程
         */


//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程id:  " + Thread.currentThread().getId());
//            int i = 10 / 0;  //模拟异常
//            System.out.println("当前线程的运行结果:  " + i);
//            return i;
//        }, executor).whenComplete((result,exception)->{
//            //虽然能得到异常信息，但是没法修改返回数据
//            System.out.println("异步任务完成了...结果是"+result+";  异常是："+exception);
//        }).exceptionally(throwable -> {
//            //可以感知异常同时返回默认值
//            return 10;
//        });
        // R apply(T t);

        //方法执行完成之后的处理，无论是成功完成还是失败完成
        System.out.println("main...start...");
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程id:  " + Thread.currentThread().getId());
            int i = 10 / 0;  //模拟异常
            System.out.println("当前线程的运行结果:  " + i);
            return i;
        }, executor).handle((res,thr)->{
            if(res!=null){return res*2;}
            if(thr!=null){return 0;}
            return 0;
                });
        //    R apply(T t, U u);
        System.out.println("main...end...最终返回结果: "+future.get());
    }

}


