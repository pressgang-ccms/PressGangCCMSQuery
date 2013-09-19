package org.jboss.pressgang.ccms.filter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldMapDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseMultiFieldFilter extends BaseFieldFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseMultiFieldFilter.class);

    private List<FilterFieldMapDataBase<?>> multipleFilterVars = new ArrayList<FilterFieldMapDataBase<?>>();

    @Override
    public List<FilterFieldDataBase<?>> getFields() {
        final List<FilterFieldDataBase<?>> fields = super.getFields();
        fields.addAll(multipleFilterVars);
        return fields;
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();
        multipleFilterVars.clear();
    }

    protected List<FilterFieldMapDataBase<?>> getMultiFilterVars() {
        return Collections.unmodifiableList(multipleFilterVars);
    }

    protected void addMultiFilterVar(final FilterFieldMapDataBase<?> filterVar) {
        multipleFilterVars.add(filterVar);
    }

    @Override
    public String getFieldValue(final String fieldName) {
        // Check the multiple filters
        for (final FilterFieldMapDataBase<?> field : multipleFilterVars) {
            if (fieldName.matches("^" + field.getName() + "\\d+$")) {
                final String index = fieldName.replace(field.getName(), "");

                /*
                 * index will be empty if the fieldName matches, which can happen when
                 * another object is looping over the getBaseFilterNames() keyset.
                 */
                if (!index.isEmpty()) {
                    if (field.getData() != null && field.getData().containsKey(index)) {
                        return field.getData().get(index).toString();
                    }
                }

                return null;
            }
        }

        // If a multi filter field wasn't found just look up the single filter fields
        return super.getFieldValue(fieldName);
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue) {
        // Check the multiple filters
        for (final FilterFieldMapDataBase<?> field : multipleFilterVars) {
            if (fieldName.matches("^" + field.getName() + "\\d+$")) {
                try {
                    final String index = fieldName.replace(field.getName(), "");

                    // Parse the ID
                    final Integer indexInt = Integer.parseInt(index);
                    field.put(indexInt, fieldValue);
                } catch (final NumberFormatException ex) {
                    // could not parse integer, so fail
                    log.warn("Probably a malformed URL query parameter for the " + field.getName() + " ID", ex);
                }

                return;
            }
        }

        // If a multi filter field wasn't found, than the field must be a single filter
        super.setFieldValue(fieldName, fieldValue);
    }

    @Override
    public boolean hasFieldName(final String fieldName) {
        boolean retValue = false;
        for (final String name : getFieldNames().keySet()) {
            if (fieldName.matches("^" + name + "$")) {
                return true;
            }
        }

        return super.hasFieldName(fieldName);
    }

    @Override
    public String getFieldDesc(final String fieldName) {
        for (final String name : getFieldNames().keySet()) {
            if (fieldName.matches("^" + name + "$")) {
                return getFieldNames().get(name);
            }
        }

        return super.getFieldDesc(fieldName);
    }
}
