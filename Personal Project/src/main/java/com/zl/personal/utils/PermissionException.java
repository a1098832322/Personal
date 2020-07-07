package com.zl.personal.utils;

/**权限许可异常
 * @author 郑龙
 * @date 2020/7/7 9:54
 */
public class PermissionException extends RuntimeException {
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PermissionException(String message) {
        super(message);
    }
}
