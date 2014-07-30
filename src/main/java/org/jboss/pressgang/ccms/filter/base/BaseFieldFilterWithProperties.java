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

package org.jboss.pressgang.ccms.filter.base;

import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanMapData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringMapData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class BaseFieldFilterWithProperties extends BaseMultiFieldFilter {
    private FilterFieldStringMapData propertyTags;
    private FilterFieldBooleanMapData propertyTagExists;
    private FilterFieldBooleanMapData propertyTagNotExists;

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        propertyTags = new FilterFieldStringMapData(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);
        propertyTagExists = new FilterFieldBooleanMapData(CommonFilterConstants.PROPERTY_TAG_EXISTS,
                CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        propertyTagNotExists = new FilterFieldBooleanMapData(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS,
                CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        addMultiFilterVar(propertyTags);
        addMultiFilterVar(propertyTagExists);
        addMultiFilterVar(propertyTagNotExists);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG + "\\d+", CommonFilterConstants.PROPERTY_TAG_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_EXISTS + "\\d+", CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS + "\\d+", CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.put(CommonFilterConstants.PROPERTY_TAG, CommonFilterConstants.PROPERTY_TAG_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_EXISTS, CommonFilterConstants.PROPERTY_TAG_EXISTS_DESC);
        retValue.put(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS, CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS_DESC);

        return retValue;
    }
}
