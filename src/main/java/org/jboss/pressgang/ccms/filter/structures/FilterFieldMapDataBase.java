package org.jboss.pressgang.ccms.filter.structures;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FilterFieldMapDataBase<U> extends FilterFieldDataBase<Map<Integer, U>> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldMapDataBase.class);

    /**
     * The data stored within this UIField
     */
    protected Map<Integer, U> data = new HashMap<Integer, U>();
    /**
     * Whether or not this object has been "negated"
     */
    protected boolean negated = false;
    /**
     * The name of the data field
     */
    protected String name = "";
    /**
     * The description
     */
    protected String description = "";

    protected FilterFieldMapDataBase(final String name, final String description) {
        super(name, description);
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(final boolean negated) {
        this.negated = negated;
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

    public abstract void put(Integer key, String value);

    @Override
    public void setData(String value) {
        try {
            final List<String> vars = Arrays.asList(value.split(","));

            for (final String var : vars) {
                String[] dataVars = var.split("=", 2);

                put(Integer.parseInt(dataVars[0]), dataVars[1]);
            }
        } catch (final Exception ex) {
            // could not parse, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public String toString() {
        if (data == null) return null;

        final StringBuilder retValue = new StringBuilder();

        for (final Map.Entry<Integer, U> entry : data.entrySet()) {
            if (retValue.length() > 0) retValue.append(",");

            retValue.append(entry.getKey() + "=" + entry.getValue());
        }

        return retValue.toString();
    }
}
