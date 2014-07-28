/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseTopicFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringListData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * This class represents the options used by the objects that extend the
 * ExtendedTopicList class to filter a query to retrieve Topic org.jboss.pressgang.ccms.contentspec.entities.
 */
public class TranslatedTopicFieldFilter extends BaseTopicFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> singleFilterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = -6343139695468503659L;

        {
            put(CommonFilterConstants.ZANATA_IDS_FILTER_VAR, CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR, CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);

            put(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
            put(CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                    CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
        }
    });

    private FilterFieldStringListData zanataIds;
    private FilterFieldStringListData notZanataIds;
    private FilterFieldBooleanData latestTranslations;
    private FilterFieldBooleanData latestCompletedTranslations;
    private FilterFieldBooleanData notLatestTranslations;
    private FilterFieldBooleanData notLatestCompletedTranslations;

    public TranslatedTopicFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        /* Zanata ID's */
        zanataIds = new FilterFieldStringListData(CommonFilterConstants.ZANATA_IDS_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_FILTER_VAR_DESC);
        notZanataIds = new FilterFieldStringListData(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR,
                CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR_DESC);

                /* Latest Translations */
        latestTranslations = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestTranslations = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_TRANSLATIONS_FILTER_VAR_DESC);

        /* Latest Completed Translations */
        latestCompletedTranslations = new FilterFieldBooleanData(CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);
        notLatestCompletedTranslations = new FilterFieldBooleanData(
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR,
                CommonFilterConstants.TOPIC_NOT_LATEST_COMPLETED_TRANSLATIONS_FILTER_VAR_DESC);

        addFilterVar(zanataIds);
        addFilterVar(notZanataIds);
        addFilterVar(latestTranslations);
        addFilterVar(latestCompletedTranslations);
        addFilterVar(notLatestTranslations);
        addFilterVar(notLatestCompletedTranslations);
    }

    /**
     * @return A map of the expanded filter field names (i.e. with regular
     *         expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(singleFilterNames);
        return retValue;
    }

    /**
     * @return A map of the base filter field names (i.e. with no regular
     *         expressions) mapped to their descriptions
     */
    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.putAll(singleFilterNames);
        return retValue;
    }

    public FilterFieldStringListData getZanataIds() {
        return zanataIds;
    }

    public FilterFieldStringListData getNotZanataIds() {
        return notZanataIds;
    }

    public FilterFieldBooleanData getLatestTranslations() {
        return latestTranslations;
    }

    public FilterFieldBooleanData getLatestCompletedTranslations() {
        return latestCompletedTranslations;
    }

    public FilterFieldBooleanData getNotLatestTranslations() {
        return notLatestTranslations;
    }

    public FilterFieldBooleanData getNotLatestCompletedTranslations() {
        return notLatestCompletedTranslations;
    }
}
