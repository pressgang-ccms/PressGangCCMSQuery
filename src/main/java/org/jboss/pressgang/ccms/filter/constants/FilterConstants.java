package org.jboss.pressgang.ccms.filter.constants;

import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FilterConstants {
    /**
     * The default logic to be applied to tags within a category
     */
    public static final String DEFAULT_INTERNAL_LOGIC = CommonFilterConstants.OR_LOGIC;
    /**
     * The default logic to be applied between categories
     */
    public static final String DEFAULT_EXTERNAL_LOGIC = CommonFilterConstants.AND_LOGIC;
    /**
     * The default internal category logic state
     */
    public static final int CATEGORY_INTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_INTERNAL_OR_STATE;
    /**
     * The default external category logic state
     */
    public static final int CATEGORY_EXTERNAL_DEFAULT_STATE = CommonFilterConstants.CATEGORY_EXTERNAL_AND_STATE;
    /**
     * The default logic to be applied to the search fields
     */
    public static final String LOGIC_FILTER_VAR_DEFAULT_VALUE = "and";
}
