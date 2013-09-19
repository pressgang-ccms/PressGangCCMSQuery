package org.jboss.pressgang.ccms.filter.structures;

public abstract class FilterFieldDataBase<T> {
    /**
     * The data stored within this UIField
     */
    protected T data = null;
    /**
     * The name of the data field
     */
    protected String name = "";
    /**
     * The description
     */
    protected String description = "";

    protected FilterFieldDataBase(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getBaseName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public abstract T getData();

    public String getDataString() {
        return data.toString();
    }

    public abstract void setData(T data);

    public abstract void setData(String value);
}
