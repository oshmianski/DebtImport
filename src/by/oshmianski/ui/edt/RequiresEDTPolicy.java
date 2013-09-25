package by.oshmianski.ui.edt;

/**
 * EDT execution policy. Code can be executed in EDT synchronously - through
 * <code>javax.swing.SwingUtilities.invokeAndWait(Runnable)</code>, - or asynchronously - through
 * <code>javax.swing.SwingUtilities.invokeLater(Runnable)</code>.
 *
 * @author Eugene Matyushkin aka Skipy
 *
 * @since 13.08.2010
 */
public enum RequiresEDTPolicy {

    /**
     * Asynchronous execution
     */
    ASYNC,

    /**
     * Synchronous execution
     */
    SYNC
}