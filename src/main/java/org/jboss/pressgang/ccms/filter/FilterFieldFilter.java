package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FilterFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.FILTER_IDS_FILTER_VAR, CommonFilterConstants.FILTER_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILTER_NAME_FILTER_VAR, CommonFilterConstants.FILTER_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR, CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData filterIds;
    private FilterFieldStringData filterName;
    private FilterFieldStringData filterDescription;

    public FilterFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        filterIds = new FilterFieldStringData(CommonFilterConstants.FILTER_IDS_FILTER_VAR,
                CommonFilterConstants.FILTER_IDS_FILTER_VAR_DESC);
        filterName = new FilterFieldStringData(CommonFilterConstants.FILTER_NAME_FILTER_VAR,
                CommonFilterConstants.FILTER_NAME_FILTER_VAR_DESC);
        filterDescription = new FilterFieldStringData(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(filterIds);
        addFilterVar(filterName);
        addFilterVar(filterDescription);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }
}
