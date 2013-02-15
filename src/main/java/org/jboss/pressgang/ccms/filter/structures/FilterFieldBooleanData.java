package org.jboss.pressgang.ccms.filter.structures;

public class FilterFieldBooleanData extends FilterFieldDataBase<Boolean> {

    public FilterFieldBooleanData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Boolean getData() {
        return data;
    }

    @Override
    public void setData(final Boolean data) {
        this.data = data ? true : null;
    }

    @Override
    public void setData(final String value) {
        data = (value == null ? null : (Boolean.parseBoolean(value) ? true : null));
    }

    @Override
    public String toString() {
        return data == null ? "" : data.toString();
    }
}
