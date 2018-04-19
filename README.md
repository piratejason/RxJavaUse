# 更新日志
   2018-04-19 init
## RxJavaUse
### 相关链接
[RxJava](https://github.com/ReactiveX/RxJava)

[RxAndroid](https://github.com/ReactiveX/RxAndroid)

[Awesome-RxJava](https://github.com/lzyzsd/Awesome-RxJava)

[RxJava详解](http://gank.io/post/560e15be2dca930e00da1083) 该文sdk略老,api变化多,理解原理,操作思想

### rxjava中集中可用的容器

io.reactivex.Observable: 0..N flows, no backpressure,

io.reactivex.Flowable: 0..N flows, supporting Reactive-Streams and backpressure

Observable优化后的产物

io.reactivex.Single: a flow of exactly 1 item or an error,

io.reactivex.Completable: a flow without items but only a completion or error signal,

io.reactivex.Maybe: a flow with no items, exactly one item or an error.

### 常用api

1. fromIterable,fromArray,just:需要处理的数据的来源

1. map:将原有结构A,转换成结构B(类型A->类型B,一对一)

1. flatMap:将结构A,转换成结构B(一对多)

1. filter:过滤器,数据筛选

1. lift:将数据对象A包装成数据对象B

1. subscribeOn:标记处理业务在什么线程操作

    可用的几个参数,其中最常用的是Schedulers.io()

    Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。

    Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。

    Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。

    Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。

1. observeOn:业务处理完以后,要做的其他业务时,需要在什么线程操作,通常在Andorid里使用AndroidSchedulers.mainThread(),结合[RxAndroid](https://github.com/ReactiveX/RxAndroid)

1. subscribe:开始处理业务,相当于 thread api中的start()

   在没有调用该api时,**1-6**所有的链式都不会执行


