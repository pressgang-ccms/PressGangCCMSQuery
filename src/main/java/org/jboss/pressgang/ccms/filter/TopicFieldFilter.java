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
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * This class provides a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity. This is also used by the Seam GUI to store the data temporarily.
 */
public class TopicFieldFilter extends BaseTopicFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> singleFilterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = -6343139695468503659L;

        {
            put(CommonFilterConstants.TOPIC_MIN_HASH_VAR, CommonFilterConstants.TOPIC_MIN_HASH_VAR_DESC);
            put(CommonFilterConstants.CREATED_BY_VAR, CommonFilterConstants.CREATED_BY_VAR_DESC);
            put(CommonFilterConstants.NOT_CREATED_BY_VAR, CommonFilterConstants.NOT_CREATED_BY_VAR_DESC);
            put(CommonFilterConstants.EDITED_BY_VAR, CommonFilterConstants.EDITED_BY_VAR_DESC);
            put(CommonFilterConstants.NOT_EDITED_BY_VAR, CommonFilterConstants.NOT_EDITED_BY_VAR_DESC);
        }
    });

    private FilterFieldStringData topicMinHash;
    private FilterFieldStringData createdBy;
    private FilterFieldStringData notCreatedBy;
    private FilterFieldStringData editedBy;
    private FilterFieldStringData notEditedBy;

    public TopicFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        topicMinHash = new FilterFieldStringData(CommonFilterConstants.TOPIC_MIN_HASH_VAR, CommonFilterConstants.TOPIC_MIN_HASH_VAR_DESC);
        createdBy = new FilterFieldStringData(CommonFilterConstants.CREATED_BY_VAR, CommonFilterConstants.CREATED_BY_VAR_DESC);
        notCreatedBy = new FilterFieldStringData(CommonFilterConstants.NOT_CREATED_BY_VAR, CommonFilterConstants.NOT_CREATED_BY_VAR_DESC);
        editedBy = new FilterFieldStringData(CommonFilterConstants.EDITED_BY_VAR, CommonFilterConstants.EDITED_BY_VAR_DESC);
        notEditedBy = new FilterFieldStringData(CommonFilterConstants.NOT_EDITED_BY_VAR, CommonFilterConstants.NOT_EDITED_BY_VAR_DESC);

        addFilterVar(topicMinHash);
        addFilterVar(createdBy);
        addFilterVar(notCreatedBy);
        addFilterVar(editedBy);
        addFilterVar(notEditedBy);
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

    public FilterFieldStringData getTopicMinHash() {
        return topicMinHash;
    }

    public void setTopicMinHash(final FilterFieldStringData topicMinHash) {
        this.topicMinHash = topicMinHash;
    }
}
