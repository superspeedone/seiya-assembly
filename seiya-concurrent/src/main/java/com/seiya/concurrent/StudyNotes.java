package com.seiya.concurrent;

public class StudyNotes {

    /**
     * 线程的状态
     *
     * 1. 创建（new）状态: 准备好了一个多线程的对象
     * 2. 就绪（runnable）状态: 调用了start()方法, 等待CPU进行调度
     * 3. 运行（running）状态: 执行run()方法
     * 4. 阻塞（blocked）状态: 暂时停止执行, 可能将资源交给其它线程使用
     * 5. 终止（dead）状态: 线程销毁
     */

    // 当需要新起一个线程来执行某个子任务时，就创建了一个线程。但是线程创建之后，不会立即进入就绪状态，
    // 因为线程的运行需要一些条件（比如内存资源，在前面的JVM内存区域划分一篇博文中知道程序计数器、Java栈、
    // 本地方法栈都是线程私有的，所以需要为线程分配一定的内存空间），只有线程运行需要的所有条件满足了，才进入就绪状态。
    //
    // 当线程进入就绪状态后，不代表立刻就能获取CPU执行时间，也许此时CPU正在执行其他的事情，因此它要等待。当得到CPU
    // 执行时间之后，线程便真正进入运行状态。
    //
    // 线程在运行状态过程中，可能有多个原因导致当前线程不继续运行下去，比如用户主动让线程睡眠（睡眠一定的时间之后再重新执行）、
    // 用户主动让线程等待，或者被同步块给阻塞，此时就对应着多个状态：time waiting（睡眠或等待一定的事件）、
    // waiting（等待被唤醒）、blocked（阻塞）。
    // 当由于突然中断或者子任务执行完毕，线程就会被消亡。

    /**
     * 注:sleep和wait的区别:
     */
    // 1. sleep是Thread类的方法,wait是Object类中定义的方法.
    // 2. Thread.sleep不会导致锁行为的改变, 如果当前线程是拥有锁的, 那么Thread.sleep不会让线程释放锁.
    // 3. Thread.sleep和Object.wait都会暂停当前的线程. OS会将执行时间分配给其它线程. 区别是, 调用wait后,
    // 需要别的线程执行notify/notifyAll才能够重新获得CPU执行时间.

    /**
     * 上下文切换
     */
    // 对于单核CPU来说（对于多核CPU，此处就理解为一个核），CPU在一个时刻只能运行一个线程，
    // 当在运行一个线程的过程中转去运行另外一个线程，这个叫做线程上下文切换（对于进程也是类似）。

    // 由于可能当前线程的任务并没有执行完毕，所以在切换时需要保存线程的运行状态，以便下次重新切换回来时能够继续切换之前的状态运行。
    // 举个简单的例子：比如一个线程A正在读取一个文件的内容，正读到文件的一半，此时需要暂停线程A，转去执行线程B，当再次切换回来执
    // 行线程A的时候，我们不希望线程A又从文件的开头来读取。
    //
    // 因此需要记录线程A的运行状态，那么会记录哪些数据呢？因为下次恢复时需要知道在这之前当前线程已经执行到哪条指令了，所以需要
    // 记录程序计数器的值，另外比如说线程正在进行某个计算的时候被挂起了，那么下次继续执行的时候需要知道之前挂起时变量的值时多少，
    // 因此需要记录CPU寄存器的状态。所以一般来说，线程上下文切换过程中会记录程序计数器、CPU寄存器状态等数据。
    //
    // 说简单点的：对于线程的上下文切换实际上就是 存储和恢复CPU状态的过程，它使得线程执行能够从中断点恢复执行。

    // 虽然多线程可以使得任务执行的效率得到提升，但是由于在线程切换时同样会带来一定的开销代价，并且多个线程会导致
    // 系统资源占用的增加，所以在进行多线程编程时要注意这些因素。


    /**
     * yield()方法
     *
     * 调用yield方法会让当前线程交出CPU权限，让CPU去执行其他的线程。它跟sleep方法类似，同样不会释放锁。
     * 但是yield不能控制具体的交出CPU的时间，另外，yield方法只能让拥有相同优先级的线程有获取CPU执行时间的机会。
     *
     * 注意，调用yield方法并不会让线程进入阻塞状态，而是让线程重回就绪状态，它只需要等待重新获取CPU执行时间，
     * 这一点是和sleep方法不一样的。
     */

    /**
     * sleep()方法
     *
     * 方法sleep()的作用是在指定的毫秒数内让当前“正在执行的线程”休眠（暂停执行）。
     * 这个“正在执行的线程”是指this.currentThread()返回的线程。
     *
     * sleep相当于让线程睡眠，交出CPU，让CPU去执行其他的任务。
     * 但是有一点要非常注意，sleep方法不会释放锁，也就是说如果当前线程持有对某个对象的锁，
     * 则即使调用sleep方法，其他线程也无法访问这个对象。
     */

    /**
     * join()方法
     * 在很多情况下，主线程创建并启动了线程，如果子线程中药进行大量耗时运算，主线程往往将早于子线程结束之前结束。
     * 这时，如果主线程想等待子线程执行完成之后再结束，比如子线程处理一个数据，主线程要取得这个数据中的值，
     * 就要用到join()方法了。方法join()的作用是等待线程对象销毁
     */

    /**
     * 停止线程
     *
     * 停止线程是在多线程开发时很重要的技术点，掌握此技术可以对线程的停止进行有效的处理。
     * 停止一个线程可以使用Thread.stop()方法，但最好不用它。该方法是不安全的，已被弃用。
     * 在Java中有以下3种方法可以终止正在运行的线程：
     *
     * 1. 使用退出标志，使线程正常退出，也就是当run方法完成后线程终止
     * 2. 使用stop方法强行终止线程，但是不推荐使用这个方法，因为stop和suspend及resume一样，都是作废过期的方法，
     *      使用他们可能产生不可预料的结果。
     * 3. 使用interrupt方法中断线程，但这个不会终止一个正在运行的线程，还需要加入一个判断才可以完成线程的停止。
     *
     * 暂停线程  interrupt()方法
     *
     */

    /**
     *  线程优先级特性：
     *
     *  继承性  比如A线程启动B线程，则B线程的优先级与A是一样的。
     *  规则性  高优先级的线程总是大部分先执行完，但不代表高优先级线程全部先执行完。
     *  随机性 优先级较高的线程不一定每一次都先执行完。
     */





}
