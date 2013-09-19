package org.jboss.pressgang.ccms.filter;

import org.jboss.pressgang.ccms.filter.base.BaseTopicFieldFilter;

/**
 * This class provides a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity. This is also used by the Seam GUI to store the data temporarily.
 */
public class TopicFieldFilter extends BaseTopicFieldFilter {

    public TopicFieldFilter() {
        resetAllValues();
    }
}
