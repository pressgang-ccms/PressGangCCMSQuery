package org.jboss.pressgang.ccms.filter.structures;

import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public enum FilterStringLogic {
    MATCHES(CommonFilterConstants.STRING_MATCHES_SUFFIX), CONTAINS(CommonFilterConstants.STRING_CONTAINS_SUFFIX);

    private String suffix;

    FilterStringLogic(final String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return suffix;
    }
}
