package by.oshmianski.loaders;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 02.09.12
 * Time: 20:58
 */
public abstract interface Loader {
    public void cancel();
    public void execute();
}
