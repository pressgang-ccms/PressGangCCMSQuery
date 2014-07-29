/*
  Copyright 2011-2014 Red Hat

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
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class PropertyTagFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR, CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR, CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData propTagIds;
    private FilterFieldStringData propTagName;
    private FilterFieldStringData propTagDescription;

    public PropertyTagFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        propTagIds = new FilterFieldIntegerListData(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR,
                CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR_DESC);
        propTagName = new FilterFieldStringData(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR,
                CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR_DESC);
        propTagDescription = new FilterFieldStringData(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(propTagIds);
        addFilterVar(propTagName);
        addFilterVar(propTagDescription);
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
