package by.oshmianski.ui.edt;

import by.oshmianski.objects.DataMainItem;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 05.04.13
 * Time: 9:59
 */
public interface UIProcessorImport {
    /**
     * Performs required UI operations when loading starts. This method can be called from outside EDT.
     */
    @RequiresEDT
    void startLoading();

    /**
     * Performs required UI operations when loading stops. This method can be called from outside EDT.
     */
    @RequiresEDT
    void stopLoading();

    @RequiresEDT
    void progressSetValue(final int value);

    @RequiresEDT
    void progressSetMaximum(final int count);
}
