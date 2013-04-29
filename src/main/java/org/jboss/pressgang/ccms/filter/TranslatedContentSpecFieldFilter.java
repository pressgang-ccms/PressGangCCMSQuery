package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class TranslatedContentSpecFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR,
                    CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.ZANATA_IDS_FILTER_VAR, CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR, CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringData translatedContentSpecIds;
    private FilterFieldStringData zanataIds;
    private FilterFieldStringData notZanataIds;

    public TranslatedContentSpecFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        translatedContentSpecIds = new FilterFieldStringData(CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR,
                CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR_DESC);

        /* Zanata ID's */
        zanataIds = new FilterFieldStringData(CommonFilterConstants.ZANATA_IDS_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
        notZanataIds = new FilterFieldStringData(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);

        addFilterVar(translatedContentSpecIds);
        addFilterVar(zanataIds);
        addFilterVar(notZanataIds);
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
