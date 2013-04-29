package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class UserFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.USER_IDS_FILTER_VAR, CommonFilterConstants.USER_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.USER_NAME_FILTER_VAR, CommonFilterConstants.USER_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR, CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData userIds;
    private FilterFieldStringData userTitle;
    private FilterFieldStringData userDescription;

    public UserFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        userIds = new FilterFieldStringData(CommonFilterConstants.USER_IDS_FILTER_VAR, CommonFilterConstants.USER_IDS_FILTER_VAR_DESC);
        userTitle = new FilterFieldStringData(CommonFilterConstants.USER_NAME_FILTER_VAR, CommonFilterConstants.USER_NAME_FILTER_VAR_DESC);
        userDescription = new FilterFieldStringData(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(userIds);
        addFilterVar(userTitle);
        addFilterVar(userDescription);
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
