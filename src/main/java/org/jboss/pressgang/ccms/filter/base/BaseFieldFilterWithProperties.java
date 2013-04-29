package org.jboss.pressgang.ccms.filter.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringMapData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseFieldFilterWithProperties extends BaseFieldFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseFieldFilterWithProperties.class);

    private FilterFieldStringMapData propertyTags;

    private List<FilterFieldDataBase<?>> multipleFilterVars = new ArrayList<FilterFieldDataBase<?>>();

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        propertyTags = new FilterFieldStringMapData(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);

        multipleFilterVars.clear();
        multipleFilterVars.add(propertyTags);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG + "\\d+", CommonFilterConstants.PROPERTY_TAG_DESC);
        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);
        return retValue;
    }

    @Override
    public boolean hasFieldName(final String fieldName) {
        if (super.hasFieldName(fieldName)) {
            return true;
        } else {
            boolean retValue = false;
            for (final String name : getFieldNames().keySet()) {
                if (fieldName.matches("^" + name + "$")) {
                    retValue = true;
                    break;
                }
            }

            return retValue;
        }
    }

    @Override
    public String getFieldDesc(final String fieldName) {
        if (super.hasFieldName(fieldName)) {
            return super.getFieldDesc(fieldName);
        } else {
            for (final String name : getFieldNames().keySet()) {
                if (fieldName.matches("^" + name + "$")) {
                    return getFieldNames().get(name);
                }
            }

            return "";
        }
    }

    @Override
    public String getFieldValue(final String fieldName) {
        if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG)) {
            final String index = fieldName.replace(CommonFilterConstants.PROPERTY_TAG, "");

            /*
             * index will be empty if the fieldName is just CommonFilterConstants.PROPERTY_TAG, which can happen when
             * another object is looping over the getBaseFilterNames() keyset.
             */
            if (!index.isEmpty()) {
                try {
                    final Integer indexInt = Integer.parseInt(index);

                    /*
                     * propertyTags will be null unless one of the setPropertyTag() method is called
                     */
                    if (propertyTags.getData() != null && propertyTags.getData().size() > indexInt)
                        return propertyTags.getData().get(indexInt);
                } catch (final NumberFormatException ex) {
                    // could not parse integer, so fail
                    log.warn("Probably a malformed URL query parameter for the 'Property Tag' Topic ID", ex);
                    return null;
                }
            }

            return null;
        } else {
            return super.getFieldValue(fieldName);
        }
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue) {
        if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG)) {
            try {
                final String index = fieldName.replace(CommonFilterConstants.PROPERTY_TAG, "");
                final Integer indexInt = Integer.parseInt(index);
                setPropertyTag(fieldValue, indexInt);
            } catch (final NumberFormatException ex) {
                // could not parse integer, so fail
                log.warn("Probably a malformed URL query parameter for the 'Property Tag' Topic ID", ex);
            }

        } else {
            super.setFieldValue(fieldName, fieldValue);
        }
    }

    protected FilterFieldStringMapData getPropertyTags() {
        return propertyTags;
    }

    protected void setPropertyTag(final String propertyTag, final int index) {
        if (propertyTags.getData() == null) propertyTags.setData(new HashMap<String, String>());

        propertyTags.getData().put(Integer.toString(index), propertyTag);
    }
}
