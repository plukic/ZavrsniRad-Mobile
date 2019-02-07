package ba.ito.assistance.util;

import io.reactivex.Scheduler;

public interface ISchedulersProvider {
    Scheduler main();
    Scheduler computation();
    Scheduler network();
}
