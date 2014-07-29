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
import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class CategoryFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR, CommonFilterConstants.CATEGORY_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR, CommonFilterConstants.CATEGORY_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR, CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR, CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR,
                    CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData categoryIds;
    private FilterFieldStringData categoryName;
    private FilterFieldStringData categoryDescription;
    private FilterFieldBooleanData categoryIsMutuallyExclusive;
    private FilterFieldBooleanData categoryIsNotMutuallyExclusive;

    public CategoryFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        categoryIds = new FilterFieldIntegerListData(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR,
                CommonFilterConstants.CATEGORY_IDS_FILTER_VAR_DESC);
        categoryName = new FilterFieldStringData(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR,
                CommonFilterConstants.CATEGORY_NAME_FILTER_VAR_DESC);
        categoryDescription = new FilterFieldStringData(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR,
                CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR_DESC);
        categoryIsMutuallyExclusive = new FilterFieldBooleanData(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR,
                CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR_DESC);
        categoryIsNotMutuallyExclusive = new FilterFieldBooleanData(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR,
                CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR_DESC);

        addFilterVar(categoryIds);
        addFilterVar(categoryName);
        addFilterVar(categoryDescription);
        addFilterVar(categoryIsMutuallyExclusive);
        addFilterVar(categoryIsNotMutuallyExclusive);
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
