package org.jboss.pressgang.ccms.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringListData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagFieldFilter extends BaseFieldFilter {
    private static final Logger log = LoggerFactory.getLogger(TagFieldFilter.class);

    /**
     * A map of the base filter field names that can not have multiple mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.TAG_IDS_FILTER_VAR, CommonFilterConstants.TAG_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TAG_NAME_FILTER_VAR, CommonFilterConstants.TAG_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR, CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData tagIds;
    private FilterFieldStringData tagName;
    private FilterFieldStringData tagDescription;
    private FilterFieldStringListData propertyTags;

    private List<FilterFieldDataBase<?>> multipleFilterVars = new ArrayList<FilterFieldDataBase<?>>();

    public TagFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        tagIds = new FilterFieldStringData(CommonFilterConstants.TAG_IDS_FILTER_VAR, CommonFilterConstants.TAG_IDS_FILTER_VAR_DESC);
        tagName = new FilterFieldStringData(CommonFilterConstants.TAG_NAME_FILTER_VAR, CommonFilterConstants.TAG_NAME_FILTER_VAR_DESC);
        tagDescription = new FilterFieldStringData(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR_DESC);
        propertyTags = new FilterFieldStringListData(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);

        addFilterVar(tagIds);
        addFilterVar(tagName);
        addFilterVar(tagDescription);

        multipleFilterVars.clear();
        multipleFilterVars.add(this.propertyTags);
    }

    @Override
    public boolean hasFieldName(final String fieldName) {
        boolean retValue = false;
        for (final String name : getFieldNames().keySet()) {
            if (fieldName.matches("^" + name + "$")) {
                retValue = true;
                break;
            }
        }

        return retValue;
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        retValue.put(CommonFilterConstants.PROPERTY_TAG + "\\d+", CommonFilterConstants.PROPERTY_TAG_DESC);

        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        retValue.put(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);

        return retValue;
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
                    if (this.propertyTags.getData() != null && this.propertyTags.getData().size() > indexInt)
                        return this.propertyTags.getData().get(indexInt);

                } catch (final NumberFormatException ex) {
                    // could not parse integer, so fail
                    log.warn("Probably a malformed URL query parameter for the 'Property Tag' Tag ID", ex);
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
                this.setPropertyTag(fieldValue, indexInt);
            } catch (final NumberFormatException ex) {
                // could not parse integer, so fail
                log.warn("Probably a malformed URL query parameter for the 'Property Tag' Tag ID", ex);
            }

        } else {
            super.setFieldValue(fieldName, fieldValue);
        }
    }

    protected void setPropertyTag(final String propertyTag, final int index) {
        if (this.propertyTags.getData() == null) this.propertyTags.setData(new ArrayList<String>());

        if (this.propertyTags.getData().size() < index) {
            final int start = this.propertyTags.getData().size();
            for (int i = start; i < index; ++i) {
                this.propertyTags.getData().add("");
            }
        }

        this.propertyTags.getData().set(index, propertyTag);
    }

}
