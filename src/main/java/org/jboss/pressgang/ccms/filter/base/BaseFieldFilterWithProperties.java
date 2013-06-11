package org.jboss.pressgang.ccms.filter.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanMapData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringMapData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseFieldFilterWithProperties extends BaseMultiFieldFilter {
    private FilterFieldStringMapData propertyTags;
    private FilterFieldBooleanMapData propertyTagExists;
    private FilterFieldBooleanMapData propertyTagNotExists;

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        propertyTags = new FilterFieldStringMapData(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);
        propertyTagExists = new FilterFieldBooleanMapData(CommonFilterConstants.PROPERTY_TAG_EXISTS,
                CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        propertyTagNotExists = new FilterFieldBooleanMapData(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS,
                CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        addMultiFilterVar(propertyTags);
        addMultiFilterVar(propertyTagExists);
        addMultiFilterVar(propertyTagNotExists);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG + "\\d+", CommonFilterConstants.PROPERTY_TAG_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_EXISTS + "\\d+", CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS + "\\d+", CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_EXISTS, CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS, CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        return retValue;
    }
}
