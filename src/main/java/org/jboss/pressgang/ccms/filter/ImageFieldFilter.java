package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ImageFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.IMAGE_IDS_FILTER_VAR, CommonFilterConstants.IMAGE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR, CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData imageIds;
    private FilterFieldStringData imageOriginalFilename;
    private FilterFieldStringData imageDescription;

    public ImageFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        imageIds = new FilterFieldStringData(CommonFilterConstants.IMAGE_IDS_FILTER_VAR, CommonFilterConstants.IMAGE_IDS_FILTER_VAR_DESC);
        imageOriginalFilename = new FilterFieldStringData(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR,
                CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR_DESC);
        imageDescription = new FilterFieldStringData(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(imageIds);
        addFilterVar(imageOriginalFilename);
        addFilterVar(imageDescription);
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
