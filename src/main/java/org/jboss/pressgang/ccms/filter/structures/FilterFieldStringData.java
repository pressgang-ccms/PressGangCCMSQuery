package org.jboss.pressgang.ccms.filter.structures;

import java.util.List;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

public class FilterFieldStringData extends FilterFieldDataBase<String> {
    private FilterStringLogic searchLogic = FilterStringLogic.CONTAINS;
    private boolean caseInsensitive = true;

    public FilterFieldStringData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public String getName() {
        if (searchLogic == FilterStringLogic.CONTAINS) {
            return super.getName();
        } else {
            return super.getName() + searchLogic.toString();
        }
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(final String data) {
        this.data = data;
    }

    public <T> void setData(final List<T> data) {
        this.data = CollectionUtilities.toSeperatedString(data, ",");
    }

    @Override
    public String toString() {
        return data == null ? null : data.toString();
    }

    public FilterStringLogic getSearchLogic() {
        return searchLogic;
    }

    public void setSearchLogic(FilterStringLogic searchLogic) {
        this.searchLogic = searchLogic;
    }

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }
}
