package core.dispatcher;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 事件分发器，用于管理和分发{@link DispatcherEvent}的实现类事件
 */
public class Dispatcher {
    // 事件类到处理函数列表的映射，用于存储每个事件类型对应的处理函数
    private final Map<Class<? extends DispatcherEvent>, List<Function<? extends DispatcherEvent, Void>>> evtClsMapHandlers = new HashMap<>();

    /**
     * 注册一个事件处理函数
     *
     * @param cls     事件的类，表示要处理的事件类型
     * @param handler 事件处理函数，接受一个事件对象并返回null
     * @param <E>     事件的具体类型，必须是{@link DispatcherEvent}的子类
     * @return 当前{@link Dispatcher}实例，支持链式调用
     */
    public <E extends DispatcherEvent> Dispatcher handle(Class<E> cls, Function<E, Void> handler) {
        var list = this.evtClsMapHandlers.computeIfAbsent(cls, k -> new ArrayList<>());
        list.add(handler);
        return this;
    }

    /**
     * 移除一个已经注册的事件处理函数
     *
     * @param cls     事件的类，表示要移除处理函数的事件类型
     * @param handler 要移除的事件处理函数
     * @return 当前{@link Dispatcher}实例，支持链式调用
     */
    public Dispatcher unhandled(Class<? extends DispatcherEvent> cls, Function<? extends DispatcherEvent, Void> handler) {
        var list = this.evtClsMapHandlers.get(cls);
        if (list != null) {
            list.remove(handler);
        }
        return this;
    }

    /**
     * 分发一个事件，触发所有注册了的处理函数
     *
     * @param evt 要分发的事件对象，不能为空
     * @param <E> 事件的具体类型，必须是{@link DispatcherEvent}的子类
     */
    public <E extends DispatcherEvent> void dispatch(@NotNull E evt) {
        var list = this.evtClsMapHandlers.get(evt.getClass());
        if (list != null) {
            for (Function<? extends DispatcherEvent, Void> handler : list) {
                Function<E, Void> castedHandler = (Function<E, Void>) handler;
                castedHandler.apply(evt);
                if (evt.isStop()) {
                    break;
                }
            }
        }
    }
}
