package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDateTimeData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringListData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ProcessFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.PROCESS_IDS_FILTER_VAR, CommonFilterConstants.PROCESS_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROCESS_STATUS_FILTER_VAR, CommonFilterConstants.PROCESS_STATUS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROCESS_TYPE_FILTER_VAR, CommonFilterConstants.PROCESS_TYPE_FILTER_VAR_DESC);
            put(CommonFilterConstants.STARTDATE_FILTER_VAR, CommonFilterConstants.STARTDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.ENDDATE_FILTER_VAR, CommonFilterConstants.ENDDATE_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringListData processIds;
    private FilterFieldStringData processStatus;
    private FilterFieldIntegerData processType;
    private FilterFieldDateTimeData startDate;
    private FilterFieldDateTimeData endDate;

    public ProcessFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        processIds = new FilterFieldStringListData(CommonFilterConstants.PROCESS_IDS_FILTER_VAR,
                CommonFilterConstants.PROCESS_IDS_FILTER_VAR_DESC);
        processStatus = new FilterFieldStringData(CommonFilterConstants.PROCESS_STATUS_FILTER_VAR,
                CommonFilterConstants.PROCESS_STATUS_FILTER_VAR_DESC);
        processType = new FilterFieldIntegerData(CommonFilterConstants.PROCESS_TYPE_FILTER_VAR,
                CommonFilterConstants.PROCESS_TYPE_FILTER_VAR_DESC);

        // Start/End date
        startDate = new FilterFieldDateTimeData(CommonFilterConstants.STARTDATE_FILTER_VAR,
                CommonFilterConstants.STARTDATE_FILTER_VAR_DESC);
        endDate = new FilterFieldDateTimeData(CommonFilterConstants.ENDDATE_FILTER_VAR,
                CommonFilterConstants.ENDDATE_FILTER_VAR_DESC);

        addFilterVar(processIds);
        addFilterVar(processStatus);
        addFilterVar(processType);
        addFilterVar(startDate);
        addFilterVar(endDate);
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
