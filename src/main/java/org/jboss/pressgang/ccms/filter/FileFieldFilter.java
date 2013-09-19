package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FileFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.FILE_IDS_FILTER_VAR, CommonFilterConstants.FILE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILE_NAME_FILTER_VAR, CommonFilterConstants.FILE_NAME_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData fileIds;
    private FilterFieldStringData fileName;
    private FilterFieldStringData fileDescription;

    public FileFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        fileIds = new FilterFieldIntegerListData(CommonFilterConstants.FILE_IDS_FILTER_VAR, CommonFilterConstants.FILE_IDS_FILTER_VAR_DESC);
        fileName = new FilterFieldStringData(CommonFilterConstants.FILE_NAME_FILTER_VAR, CommonFilterConstants.FILE_NAME_FILTER_VAR_DESC);
        fileDescription = new FilterFieldStringData(CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.FILE_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(fileIds);
        addFilterVar(fileName);
        addFilterVar(fileDescription);
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
