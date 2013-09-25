package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 8-058
 * Date: 19.06.12
 * Time: 11:19
 */
public class DataChildItemTreeFormat implements TreeList.Format<DataChildItem> {
    private final List criteria;

    public DataChildItemTreeFormat(List criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public void getPath(List<DataChildItem> path, DataChildItem item) {
        TreeCriterion criterion;
        int indentLevel = 0;
        int sortLevel = 0;

        final StringBuilder sb = new StringBuilder();
        DataChildItem itemTmp = null;

        for (Iterator iterator = criteria.iterator(); iterator.hasNext(); ) {
            criterion = (TreeCriterion) iterator.next();

            itemTmp = criterion.getPathItem(item, sb.toString());
            indentLevel++;

            if (itemTmp != null) {
                itemTmp.setParentCat(sb.toString());
                itemTmp.setIndentLevel(indentLevel);
                itemTmp.setSortLevel(sortLevel);

                path.add(itemTmp);

                sb.append(itemTmp.getMainCat());
            } else {
                sb.append("null");
                sortLevel--;
            }

            itemTmp = null;
        }
//        if (item.getSort() == 1 || item.getSort() == 3) {
//            item.setParentCat(item.getUl() + item.getRs() + item.getCurrency());
//        }
        path.add(item);
        sb.setLength(0);
        criterion = null;
    }

    @Override
    public boolean allowsChildren(DataChildItem item) {
        return item.isSynthetic();
    }

    @Override
    public Comparator getComparator(int i) {
        return GlazedLists.comparableComparator();
    }

}
