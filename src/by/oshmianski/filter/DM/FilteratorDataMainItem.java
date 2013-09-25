package by.oshmianski.filter.DM;/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import by.oshmianski.objects.DataMainItem;
import ca.odell.glazedlists.TextFilterator;

import java.util.List;

public class FilteratorDataMainItem implements TextFilterator<DataMainItem> {
    public void getFilterStrings(List<String> baseList, DataMainItem item) {

        baseList.add(item.getDescription());
    }
}