/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class FilterFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.FILTER_IDS_FILTER_VAR, CommonFilterConstants.FILTER_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILTER_NAME_FILTER_VAR, CommonFilterConstants.FILTER_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR, CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.FILTER_TYPE_FILTER_VAR, CommonFilterConstants.FILTER_TYPE_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData filterIds;
    private FilterFieldStringData filterName;
    private FilterFieldStringData filterDescription;
    private FilterFieldIntegerData filterType;

    public FilterFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        filterIds = new FilterFieldIntegerListData(CommonFilterConstants.FILTER_IDS_FILTER_VAR,
                CommonFilterConstants.FILTER_IDS_FILTER_VAR_DESC);
        filterName = new FilterFieldStringData(CommonFilterConstants.FILTER_NAME_FILTER_VAR,
                CommonFilterConstants.FILTER_NAME_FILTER_VAR_DESC);
        filterDescription = new FilterFieldStringData(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR_DESC);
        filterType = new FilterFieldIntegerData(CommonFilterConstants.FILTER_TYPE_FILTER_VAR,
                CommonFilterConstants.FILTER_TYPE_FILTER_VAR_DESC);

        addFilterVar(filterIds);
        addFilterVar(filterName);
        addFilterVar(filterDescription);
        addFilterVar(filterType);
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
