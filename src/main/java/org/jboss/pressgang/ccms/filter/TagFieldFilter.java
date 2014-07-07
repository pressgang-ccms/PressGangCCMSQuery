package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilterWithProperties;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class TagFieldFilter extends BaseFieldFilterWithProperties {
    /**
     * A map of the base filter field names that can not have multiple mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.TAG_IDS_FILTER_VAR, CommonFilterConstants.TAG_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TAG_NAME_FILTER_VAR, CommonFilterConstants.TAG_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR, CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.NOT_PROJECT_IDS_FILTER_VAR, CommonFilterConstants.NOT_PROJECT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROJECT_IDS_FILTER_VAR, CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData tagIds;
    private FilterFieldStringData tagName;
    private FilterFieldStringData tagDescription;
    private FilterFieldIntegerListData projectIds;
    private FilterFieldIntegerListData notProjectIds;

    public TagFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        tagIds = new FilterFieldIntegerListData(CommonFilterConstants.TAG_IDS_FILTER_VAR, CommonFilterConstants.TAG_IDS_FILTER_VAR_DESC);
        tagName = new FilterFieldStringData(CommonFilterConstants.TAG_NAME_FILTER_VAR, CommonFilterConstants.TAG_NAME_FILTER_VAR_DESC);
        tagDescription = new FilterFieldStringData(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR_DESC);
        projectIds = new FilterFieldIntegerListData(CommonFilterConstants.PROJECT_IDS_FILTER_VAR,
                CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
        notProjectIds = new FilterFieldIntegerListData(CommonFilterConstants.NOT_PROJECT_IDS_FILTER_VAR,
                CommonFilterConstants.NOT_PROJECT_IDS_FILTER_VAR_DESC);

        addFilterVar(tagIds);
        addFilterVar(tagName);
        addFilterVar(tagDescription);
        addFilterVar(projectIds);
        addFilterVar(notProjectIds);
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
