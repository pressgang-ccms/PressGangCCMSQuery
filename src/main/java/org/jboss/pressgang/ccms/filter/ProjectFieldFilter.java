package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ProjectFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.PROJECT_IDS_FILTER_VAR, CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROJECT_NAME_FILTER_VAR, CommonFilterConstants.PROJECT_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData projectIds;
    private FilterFieldStringData projectName;
    private FilterFieldStringData projectDescription;

    public ProjectFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        projectIds = new FilterFieldIntegerListData(CommonFilterConstants.PROJECT_IDS_FILTER_VAR,
                CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
        projectName = new FilterFieldStringData(CommonFilterConstants.PROJECT_NAME_FILTER_VAR,
                CommonFilterConstants.PROJECT_NAME_FILTER_VAR_DESC);
        projectDescription = new FilterFieldStringData(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(projectIds);
        addFilterVar(projectName);
        addFilterVar(projectDescription);
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
