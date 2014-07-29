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

package org.jboss.pressgang.ccms.filter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterStringLogic;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * This class provides the bases for a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity.
 */
public abstract class BaseFieldFilter implements IFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 1530842914263615035L;

        {
            put(CommonFilterConstants.LOGIC_FILTER_VAR, CommonFilterConstants.LOGIC_FILTER_VAR_DESC);

        }
    });

    private List<FilterFieldDataBase<?>> filterVars = new ArrayList<FilterFieldDataBase<?>>();

    private FilterFieldStringData logic;

    /**
     * Reset all of the Field Variables to their default values.
     */
    protected void resetAllValues() {
        logic = new FilterFieldStringData(CommonFilterConstants.LOGIC_FILTER_VAR, CommonFilterConstants.LOGIC_FILTER_VAR_DESC);

        filterVars.clear();
        filterVars.add(logic);
    }

    @Override
    public List<FilterFieldDataBase<?>> getFields() {
        return new ArrayList<FilterFieldDataBase<?>>(filterVars);
    }

    @Override
    public Map<String, String> getFieldNames() {
        return new HashMap<String, String>(filterNames);
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        return new HashMap<String, String>(filterNames);
    }

    @Override
    public boolean hasFieldName(final String fieldName) {
        final String fixedFieldName = cleanFieldName(fieldName);
        return getFieldNames().containsKey(fixedFieldName);
    }

    @Override
    public String getFieldDesc(final String fieldName) {
        final String fixedFieldName = cleanFieldName(fieldName);
        final String retValue = getFieldNames().get(fixedFieldName);
        return retValue == null ? "" : retValue;
    }

    protected void addFilterVar(final FilterFieldDataBase<?> filterVar) {
        filterVars.add(filterVar);
    }

    protected List<FilterFieldDataBase<?>> getFilterVars() {
        return Collections.unmodifiableList(filterVars);
    }

    @Override
    public String getFieldValue(final String fieldName) {
        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars) {
            if (fieldName.equals(uiField.getName())) {
                return uiField.getDataString();
            }
        }

        return null;
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue) {
        final String fixedFieldName = cleanFieldName(fieldName);

        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars) {
            if (fixedFieldName.equals(uiField.getBaseName())) {
                uiField.setData(fieldValue);

                // Set the search logic for string data
                if (uiField instanceof FilterFieldStringData) {
                    final FilterStringLogic logic = getStringSearchLogic(fieldName);
                    if (logic != null) {
                        ((FilterFieldStringData) uiField).setSearchLogic(logic);
                    }
                }
            }
        }
    }

    protected String cleanFieldName(final String fieldName) {
        String retValue = fieldName;

        for (final FilterStringLogic logic : FilterStringLogic.values()) {
            retValue = retValue.replace(logic.toString(), "");
        }

        return retValue;
    }

    protected FilterStringLogic getStringSearchLogic(final String fieldName) {
        for (final FilterStringLogic logic : FilterStringLogic.values()) {
            if (fieldName.endsWith(logic.toString())) {
                return logic;
            }
        }

        return null;
    }

    @Override
    public void syncWithFilter(final Filter filter) {
        for (final FilterField field : filter.getFilterFields())
            setFieldValue(field.getField(), field.getValue());
    }

    public FilterFieldStringData getLogic() {
        return logic;
    }
}
