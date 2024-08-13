package core.dispatcher;

import lombok.Data;

@Data
public abstract class DispatcherEvent {
    //  打断事件监听器处理流程
    private boolean stop = false;
}
