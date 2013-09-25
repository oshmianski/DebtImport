package by.oshmianski.filter.CM;

import by.oshmianski.objects.DataChildItem;

import java.util.Comparator;

public class ComparatorCMStatus implements Comparator<DataChildItem> {

    public ComparatorCMStatus() {
        super();
    }

    public int compare(DataChildItem x0, DataChildItem x1) {
        return x0.getStatus().compareTo(x1.getStatus());
    }
}
