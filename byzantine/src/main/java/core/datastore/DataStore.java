package core.datastore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataStore 类用于存储和管理应用程序中的数据
 * 它通过 DataStoreKey 为各种类型的数据提供唯一的存储标识符
 * 并支持按键值对存储数据，以及按数据键类型聚合数据
 */
public class DataStore {
    // 存储 DataStoreKey 与对应数据对象的映射
    private final Map<DataStoreKey<?>, Object> data = new HashMap<>();

    // 存储 DataStoreKey 类型与该类型键关联的值列表的映射
    private final Map<Class<? extends DataStoreKey>, List<Object>> keyClsMapValues = new HashMap<>();

    /**
     * 根据给定的键和值设置数据
     * 如果键已存在，则更新其值，并从旧值列表中移除旧值
     *
     * @param key     要设置或更新的 DataStoreKey 对象
     * @param value   与键关联的值
     * @param <Value> 值的类型
     * @return 设置的值
     */
    public <Value> Value set(@NotNull DataStoreKey<Object> key, Value value) {
        var keyClass = key.getClass();
        var list = this.keyClsMapValues.computeIfAbsent(keyClass, _ -> new ArrayList<>());
        var oldValue = this.data.put(key, value);
        if (oldValue != null) {
            list.remove(oldValue);
        }
        list.add(value);
        return value;
    }

    /**
     * 使用字符串键设置数据，内部会转换为 DataStoreKey 对象
     *
     * @param key     字符串类型的键
     * @param value   与键关联的值
     * @param <Value> 值的类型
     * @return 设置的值
     */
    public <Value> Value set(String key, Value value) {
        this.set(new DataStoreKey<>(key), value);
        return value;
    }

    /**
     * 根据给定的 DataStoreKey 获取存储的数据
     *
     * @param key     用于检索的 DataStoreKey 对象
     * @param <Value> 期望返回的值的类型
     * @return 与键关联的值，如果键不存在则返回 null
     */
    public <Value> Value get(@NotNull DataStoreKey<Object> key) {
        return (Value) this.data.get(key);
    }

    /**
     * 使用字符串键获取数据，内部会转换为 DataStoreKey 对象
     *
     * @param key     字符串类型的键
     * @param <Value> 期望返回的值的类型
     * @return 与键关联的值，如果键不存在则返回 null
     */
    public <Value> Value get(String key) {
        return this.get(new DataStoreKey<>(key));
    }

    /**
     * 获取与指定 DataStoreKey 类型关联的所有值列表
     *
     * @param keyClass DataStoreKey 的类类型
     * @param <Value>  列表中值的类型
     * @return 与指定键类型关联的值列表，如果类型不存在则返回空列表
     */
    public <Value> List<Value> getAll(Class<DataStoreKey<?>> keyClass) {
        var list = this.keyClsMapValues.get(keyClass);
        if (list == null) {
            return new ArrayList<>();
        }
        return (List<Value>) list;
    }

    /**
     * 检查给定的 DataStoreKey 是否存在于数据存储中
     *
     * @param key 要检查的 DataStoreKey 对象
     * @return 如果键存在则返回 true，否则返回 false
     */
    public boolean has(@NotNull DataStoreKey<Object> key) {
        return this.data.containsKey(key);
    }

    /**
     * 使用字符串键检查数据是否存在，内部会转换为 DataStoreKey 对象
     *
     * @param key 字符串类型的键
     * @return 如果键存在则返回 true，否则返回 false
     */
    public boolean has(String key) {
        return this.has(new DataStoreKey<>(key));
    }

    /**
     * 根据给定的 DataStoreKey 移除数据
     * 从值列表中移除与键关联的值
     *
     * @param key 要移除的 DataStoreKey 对象
     */
    public void remove(@NotNull DataStoreKey<Object> key) {
        var keyClass = key.getClass();
        var list = this.keyClsMapValues.get(keyClass);
        if (list != null) {
            list.remove(this.data.remove(key));
        }
    }

    /**
     * 使用字符串键移除数据，内部会转换为 DataStoreKey 对象
     *
     * @param key 字符串类型的键
     */
    public void remove(String key) {
        this.remove(new DataStoreKey<>(key));
    }
}
