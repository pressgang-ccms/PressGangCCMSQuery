package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseTopicFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
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
            put(CommonFilterConstants.TOPIC_MIN_HASH, CommonFilterConstants.TOPIC_MIN_HASH_DESC);
        }
    });

    private FilterFieldStringData topicMinHash;

    public TopicFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        topicMinHash = new FilterFieldStringData(CommonFilterConstants.TOPIC_MIN_HASH, CommonFilterConstants.TOPIC_MIN_HASH_DESC);

        addFilterVar(topicMinHash);
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
