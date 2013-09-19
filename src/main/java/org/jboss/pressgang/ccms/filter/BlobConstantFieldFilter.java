package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class BlobConstantFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData blobConstantIds;
    private FilterFieldStringData blobConstantName;

    public BlobConstantFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        blobConstantIds = new FilterFieldIntegerListData(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR,
                CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR_DESC);
        blobConstantName = new FilterFieldStringData(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR,
                CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR_DESC);

        addFilterVar(blobConstantIds);
        addFilterVar(blobConstantName);
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
