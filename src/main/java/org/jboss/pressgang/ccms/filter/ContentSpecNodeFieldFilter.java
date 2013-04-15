package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecNodeFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData csNodeIds;
    private FilterFieldStringData csNodeTitle;
    private FilterFieldIntegerData csNodeType;

    public ContentSpecNodeFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        csNodeIds = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR_DESC);
        csNodeTitle = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR_DESC);
        csNodeType = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR_DESC);

        addFilterVar(csNodeIds);
        addFilterVar(csNodeTitle);
        addFilterVar(csNodeType);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }
}
