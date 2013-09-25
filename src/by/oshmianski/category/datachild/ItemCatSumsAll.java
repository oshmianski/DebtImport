package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oshmianski
 * Date: 23.06.12
 * Time: 23:07
 */
public class ItemCatSumsAll {
    private final List criterionList;
    private final EventList<DataChildItem> items;
    private final Map<Integer, ItemCats> listCat;
    private ItemCatSumsOne cat;
    private List comparators;
    private List methods;
    private List itemCatSumsOneList;

    public ItemCatSumsAll(EventList<DataChildItem> items, Map<Integer, ItemCats> listCat, List criterionList) {
        this.criterionList = criterionList;
        this.items = items;
        this.listCat = listCat;
    }

    public void Install() {
        comparators = new ArrayList(criterionList.size());
        methods = new ArrayList(criterionList.size());
        itemCatSumsOneList = new ArrayList(criterionList.size());

        for (Iterator iterator = criterionList.iterator(); iterator.hasNext(); ) {
            final TreeCriterion criterion = (TreeCriterion) iterator.next();

            final Comparator comporator = GlazedLists.beanPropertyComparator(DataChildItem.class, criterion.getMethod());

            comparators.add(comporator);
            methods.add(criterion.getMethod());

//            System.out.println(methods);

            try {
                cat = new ItemCatSumsOne(items, listCat, GlazedLists.chainComparators(comparators), methods);
                itemCatSumsOneList.add(cat);
            } catch (ClassNotFoundException e) {
                MyLog.add2Log(e);
            }
        }
    }

    public void dispose(){
        try {
            Iterator it = itemCatSumsOneList.iterator();
            while(it.hasNext()) {
                ItemCatSumsOne element = (ItemCatSumsOne)it.next();
                element.dispose();
            }
            itemCatSumsOneList.clear();

            comparators.clear();
            methods.clear();
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }
}
