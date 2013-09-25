package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;
import by.oshmianski.utils.MyLog;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.GlazedListsSwing;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.*;

public class ItemCatSumsOne implements ListEventListener {
    private final GroupingList itemsByCat;
    private final EventList itemsByCatSwingThread;
    private final Map<Integer, ItemCats> listCat;
    private final List methods;
    private final List methodsUpper;
    private final Class cls;

    private List<Integer> statuses = new ArrayList();

    public ItemCatSumsOne(EventList<DataChildItem> items, Map<Integer, ItemCats> listCat, Comparator itemsGrouper, List methods) throws ClassNotFoundException {
        itemsByCat = new GroupingList(items, itemsGrouper);
        this.itemsByCatSwingThread = GlazedListsSwing.swingThreadProxyList(itemsByCat);
        this.itemsByCatSwingThread.addListEventListener(this);
        this.listCat = listCat;
        this.methods = methods;
        this.methodsUpper = new ArrayList(methods.size());

        for (Iterator iterator = methods.iterator(); iterator.hasNext(); ) {
            final String method = (String) iterator.next();

            char[] stringArray = method.toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            methodsUpper.add(new String(stringArray));
        }

        this.cls = Class.forName("by.oshmianski.objects.DataChildItem");
    }

    @Override
    public void listChanged(ListEvent listEvent) {
        while (listEvent.next()) {
            final int type = listEvent.getType();
            final int index = listEvent.getIndex();

            int catKey = 0;
            int count = 0;
            float salarySum = 0;
            float outputAverage = 0;

            boolean isDelete = false;

            List itemsOfThisCat = null;

            if (type == ListEvent.INSERT) {
                itemsOfThisCat = (List) itemsByCatSwingThread.get(index);

                final DataChildItem item = (DataChildItem) itemsOfThisCat.get(0);
                final StringBuilder sb = new StringBuilder();

                for (Iterator iterator = methodsUpper.iterator(); iterator.hasNext(); ) {
                    String method = (String) iterator.next();
                    try {
                        Method meth = cls.getMethod("get" + method, null);
                        String val = (String) meth.invoke(item, null);
                        sb.append(val);

                    } catch (Exception e) {
                        MyLog.add2Log(e);
                    }
                }

                catKey = sb.toString().hashCode();
//                count = itemsOfThisCat.size();

                statuses.add(index, catKey);

            } else if (type == ListEvent.UPDATE) {
                itemsOfThisCat = (List) itemsByCatSwingThread.get(index);

                catKey = statuses.get(index);
//                count = itemsOfThisCat.size();

            } else if (type == ListEvent.DELETE) {
//                catKey = statuses.remove(index);
                isDelete = true;

            } else {
                throw new IllegalStateException();
            }

            if (!isDelete) {
                final ItemCats cats = listCat.get(catKey);

                salarySum = 0;
                count = 0;
                outputAverage = 0;

                DataChildItem item;
                for (Iterator iter = itemsOfThisCat.iterator(); iter.hasNext(); ) {
                    item = (DataChildItem) iter.next();

//                    outputAverage += item.getOutput();
//                    salarySum +=item.getSalaryFactWhole();

                    ++count;

                    item = null;
                }

                outputAverage = outputAverage / count;

                if (cats != null) {
                    cats.setCount(count);
//                    cats.setSalarySum(salarySum);
//                    cats.setOuputAverage(outputAverage);
                } else {
                    final ItemCats catsNew = new ItemCats(count);

                    listCat.put(catKey, catsNew);
                }
            }
        }
    }

    public void dispose() {
        try {
            itemsByCat.dispose();
            itemsByCatSwingThread.dispose();
            listCat.clear();
            methods.clear();
            methodsUpper.clear();
            statuses.clear();
        } catch (Exception e) {
            MyLog.add2Log(e);
        }
    }
}
