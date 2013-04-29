package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilterWithProperties;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecFieldFilter extends BaseFieldFilterWithProperties {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR_DESC);
            put(CommonFilterConstants.EDITED_IN_LAST_DAYS, CommonFilterConstants.EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS, CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.EDITED_IN_LAST_MINUTES, CommonFilterConstants.EDITED_IN_LAST_MINUTES_DESC);
            put(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES, CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES_DESC);
        }
    });

    private FilterFieldStringData contentSpecIds;
    private FilterFieldStringData contentSpecTitle;
    private FilterFieldStringData contentSpecProduct;
    private FilterFieldStringData contentSpecVersion;
    private FilterFieldIntegerData editedInLastDays;
    private FilterFieldIntegerData notEditedInLastDays;
    private FilterFieldIntegerData editedInLastMins;
    private FilterFieldIntegerData notEditedInLastMins;

    public ContentSpecFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        contentSpecIds = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);
        contentSpecTitle = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR_DESC);
        contentSpecProduct = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR_DESC);
        contentSpecVersion = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR_DESC);
        
        /* Edited in last days */
        editedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.EDITED_IN_LAST_DAYS,
                CommonFilterConstants.EDITED_IN_LAST_DAYS_DESC);
        notEditedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS_DESC);

        /* Edited in last minutes */
        editedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.EDITED_IN_LAST_MINUTES_DESC);
        notEditedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES_DESC);

        setupSingleFilterVars();
    }

    protected void setupSingleFilterVars() {
        addFilterVar(contentSpecIds);
        addFilterVar(contentSpecTitle);
        addFilterVar(contentSpecProduct);
        addFilterVar(contentSpecVersion);
        addFilterVar(editedInLastDays);
        addFilterVar(notEditedInLastDays);
        addFilterVar(editedInLastMins);
        addFilterVar(notEditedInLastMins);
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
