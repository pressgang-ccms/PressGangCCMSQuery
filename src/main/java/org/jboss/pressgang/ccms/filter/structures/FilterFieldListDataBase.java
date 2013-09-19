package org.jboss.pressgang.ccms.filter.structures;

import java.util.List;

public abstract class FilterFieldListDataBase<T> extends FilterFieldDataBase<List<T>> {

    /**
     * The data stored within this UIField
     */
    protected List<T> data = null;
    /**
     * Whether or not this object has been "negated"
     */
    protected boolean negated = false;

    protected FilterFieldListDataBase(final String name, final String description) {
        super(name, description);
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(final boolean negated) {
        this.negated = negated;
    }
}
