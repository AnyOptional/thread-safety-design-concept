#### 核心概念
- 可见性: 一个线程对共享变量的修改，另一个线程能够立即看到。
- 原子性: 一个或多个操作在CPU执行过程中不会被中断。
- 有序性: 程序按照控制流的先后顺序执行。

#### 线程安全问题的诱因
并发编程的Bug之源：
   1. CPU缓存、线程独立缓存带来的可见性问题，线程操作的数据可能不是直接来自于内存。
   2. 编译器优化导致的有序性问题，比如new操作符是分配一块内存直接赋值给引用，后续再在这块内存上初始化变量，这可能导致对象处于不可用状态。
   3. 线程切换带来的原子性问题，线程切换是基于CPU指令的，而不是高级语言中的语句。

因此解决并发Bug的银弹就是按需禁用CPU、线程缓存和编译器优化，这部分由volatile、synchronized 和 final 三个关键字，以及 Happens-Before 规则解决，而解决原子性问题就需要用到锁。

#### Happens-Before概览
##### 如何理解Happens-Before
Happens-Before 并不是"先行发生"，不是说前面一个操作发生在后续操作的前面。它真正要表达的是：前面一个操作的结果对后续操作是可见的，所以比较正式的说法是：Happens-Before 约束了编译器的优化行为，虽允许编译器优化，但是要求编译器优化后一定遵守 Happens-Before 规则。

##### Happens-Before的7个规则
   1. 程序次序规则：在一个线程内，按照控制流顺序，书写在前面的操作Happens-Before于书写在后面的操作。
   2. 管程锁定规则：unlock操作Happens-Before于后面对同一把锁的lock操作。这里必须强调的是同一个锁，而"后面"是指时间上的先后顺序。
   3. volatile变量规则：对一个volatile变量的写操作Happens-Before于后面对这个变量的读操作，这里的"后面"同样是指时间上的先后顺序。
   4. 线程启动规则：Thread对象的start()方法Happens-Before于此线程的每一个动作。
   5. 线程终止规则：线程中的所有操作都Happens-Before于对此线程的终止检测，我们可以通过Thread.join()方法等待线程结束或Thread.isAlive()的返回值等手段检测到线程已经终止执行。
   6. 线程中断规则：对线程interrupt()方法的调用Happens-Before于被中断线程的代码检测到中断事件的发生，可以通过Thread.interrupted()方法检测到是否有中断发生。
   7. 对象终结规则：一个对象的初始化完成(构造函数执行结束)先行发生于它的finalize()方法的开始。

##### Happens-Before的一个特性：
   - 传递性：A Happens-Before于B，B Happens-Before于 C，那么A 必定Happens-Before于C。


    



