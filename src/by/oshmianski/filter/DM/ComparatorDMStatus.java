package by.oshmianski.filter.DM;

import by.oshmianski.objects.DataMainItem;

import java.util.Comparator;

public class ComparatorDMStatus implements Comparator<DataMainItem> {

    public ComparatorDMStatus() {
        super();
    }

    public int compare(DataMainItem x0, DataMainItem x1) {
        return x0.getStatus().compareTo(x1.getStatus());
    }
}
