package com.zl.personal.utils.support;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 一个1.8版本中得optional得本地化代码。用于支持低版本JDK环境<br>
 * Support 1.5
 *
 * @author 郑龙
 * @date 2020/7/21 10:49
 */
public class Optional<T> {
    /**
     * value
     */
    private final T value;


    private static <T> Optional<T> empty() {
        return new Optional<>();
    }

    private Optional() {
        this.value = null;
    }

    private Optional(T value) throws NullPointerException {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * 创建方法
     *
     * @param value 值
     * @param <T>   模板类型
     * @return {@link Optional}
     * @throws NullPointerException 当值为空时，会抛出空指针异常
     */
    public static <T> Optional<T> of(T value) throws NullPointerException {
        return new Optional<>(value);
    }

    /**
     * 创建方法2，允许null值
     *
     * @param value 值
     * @param <T>   模板类型
     * @return {@link Optional}
     */
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    /**
     * 判断值是否有效
     *
     * @return true/false 有效值/null值
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * 当值有效时，执行该处理方法
     *
     * @param consumer 自定义处理方法
     */
    public void ifPresent(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        if (isPresent()) {
            consumer.apply(value);
        }
    }

    /**
     * 获取值
     *
     * @return 值
     * @throws NoSuchElementException 当值为空时，抛出该异常，表示当前值为null
     */
    public T get() throws NoSuchElementException {
        if (isPresent()) {
            return value;
        } else {
            throw new NoSuchElementException("对象为null！无法获取有效值！");
        }

    }

    /**
     * 筛选器
     *
     * @param predicate 自定义条件
     * @return 通过筛选条件时返回本身，否则返回空{@code Optional}
     */
    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return predicate.test(value) ? this : empty();
    }

    /**
     * 应用自定义方法处理对象
     *
     * @param function 自定义方法
     * @param <R>      返回值
     * @return {@code Optional} 值对象
     */
    public <R> Optional<R> map(Function<? super T, ? extends R> function) {
        Objects.requireNonNull(function);
        if (isPresent()) {
            return Optional.ofNullable(function.apply(value));
        } else {
            return empty();
        }
    }

    /**
     * 应用自定义方法处理一个已知得Optional对象
     *
     * @param function 自定义方法
     * @param <R>      返回值
     * @return {@code Optional} 值对象
     */
    public <R> Optional<R> flatMap(Function<? super T, Optional<R>> function) {
        Objects.requireNonNull(function);
        if (isPresent()) {
            return Objects.requireNonNull(function.apply(value));
        } else {
            return empty();
        }
    }

    /**
     * 获取值或指定默认值
     *
     * @param other 当value为null时，使用此默认值替代
     * @return value or other
     */
    public T orElse(T other) {
        return isPresent() ? value : other;
    }

    /**
     * 自定义get值
     *
     * @param supplier {@link Supplier}
     * @return value or Supplier.get() result
     */
    public T orElseGet(Supplier<? extends T> supplier) {
        return isPresent() ? value : supplier.get();
    }

    /**
     * 自定义抛出的异常值
     *
     * @param supplier    {@link Supplier}
     * @param <throwable> {@link Throwable}
     * @return value or throws throwable
     * @throws throwable Throwable
     */
    public <throwable extends Throwable> T orElseThrow(Supplier<throwable> supplier) throws throwable {
        if (isPresent()) {
            return value;
        } else {
            throw supplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Optional)) {
            return false;
        }

        Optional<?> other = (Optional<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
