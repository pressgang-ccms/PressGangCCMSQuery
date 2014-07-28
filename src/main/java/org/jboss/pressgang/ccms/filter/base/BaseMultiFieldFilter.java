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

package org.jboss.pressgang.ccms.filter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldMapDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseMultiFieldFilter extends BaseFieldFilter {
    private static final Logger log = LoggerFactory.getLogger(BaseMultiFieldFilter.class);

    private List<FilterFieldMapDataBase<?>> multipleFilterVars = new ArrayList<FilterFieldMapDataBase<?>>();

    @Override
    public List<FilterFieldDataBase<?>> getFields() {
        final List<FilterFieldDataBase<?>> fields = super.getFields();
        fields.addAll(multipleFilterVars);
        return fields;
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();
        multipleFilterVars.clear();
    }

    protected List<FilterFieldMapDataBase<?>> getMultiFilterVars() {
        return Collections.unmodifiableList(multipleFilterVars);
    }

    protected void addMultiFilterVar(final FilterFieldMapDataBase<?> filterVar) {
        multipleFilterVars.add(filterVar);
    }

    @Override
    public String getFieldValue(final String fieldName) {
        // Check the multiple filters
        for (final FilterFieldMapDataBase<?> field : multipleFilterVars) {
            if (fieldName.matches("^" + field.getName() + "\\d+$")) {
                final String index = fieldName.replace(field.getName(), "");

                /*
                 * index will be empty if the fieldName matches, which can happen when
                 * another object is looping over the getBaseFilterNames() keyset.
                 */
                if (!index.isEmpty()) {
                    if (field.getData() != null && field.getData().containsKey(index)) {
                        return field.getData().get(index).toString();
                    }
                }

                return null;
            }
        }

        // If a multi filter field wasn't found just look up the single filter fields
        return super.getFieldValue(fieldName);
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue) {
        // Check the multiple filters
        for (final FilterFieldMapDataBase<?> field : multipleFilterVars) {
            if (fieldName.matches("^" + field.getName() + "\\d+$")) {
                try {
                    final String index = fieldName.replace(field.getName(), "");

                    // Parse the ID
                    final Integer indexInt = Integer.parseInt(index);
                    field.put(indexInt, fieldValue);
                } catch (final NumberFormatException ex) {
                    // could not parse integer, so fail
                    log.warn("Probably a malformed URL query parameter for the " + field.getName() + " ID", ex);
                }

                return;
            }
        }

        // If a multi filter field wasn't found, than the field must be a single filter
        super.setFieldValue(fieldName, fieldValue);
    }

    @Override
    public boolean hasFieldName(final String fieldName) {
        boolean retValue = false;
        for (final String name : getFieldNames().keySet()) {
            if (fieldName.matches("^" + name + "$")) {
                return true;
            }
        }

        return super.hasFieldName(fieldName);
    }

    @Override
    public String getFieldDesc(final String fieldName) {
        for (final String name : getFieldNames().keySet()) {
            if (fieldName.matches("^" + name + "$")) {
                return getFieldNames().get(name);
            }
        }

        return super.getFieldDesc(fieldName);
    }
}
