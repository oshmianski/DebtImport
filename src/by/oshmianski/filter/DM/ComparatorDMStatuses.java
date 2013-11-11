package by.oshmianski.filter.DM;

import by.oshmianski.objects.DataMainItem;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Comparator;

public class ComparatorDMStatuses implements Comparator<DataMainItem> {

    public ComparatorDMStatuses() {
        super();
    }

    public int compare(DataMainItem x0, DataMainItem x1) {
        return ArrayUtils.isEquals(x0.getStatuses(), x1.getStatuses()) ? 0 : -1;
    }
}
