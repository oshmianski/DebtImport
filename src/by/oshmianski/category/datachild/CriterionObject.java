package by.oshmianski.category.datachild;

import by.oshmianski.objects.DataChildItem;

public class CriterionObject extends TreeCriterion {

    public CriterionObject() {
        super("Object", "object", true);
    }

    @Override
    public DataChildItem getPathItem(DataChildItem item, String parentCat) {
        return getOrCreateItem(item, item.getObject(), parentCat);
    }
}