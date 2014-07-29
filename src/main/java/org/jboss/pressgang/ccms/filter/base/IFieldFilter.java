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

import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.Filter;

/**
 * A Field Filter provides a mechanism to find what fields are valid for a filter and a place to temporarily store the data.
 */
public interface IFieldFilter {
    /**
     * Check if the field filter has a property field name.
     *
     * @param fieldName the name of the field to be checked.
     * @return True if the field filter contains the field name, otherwise false.
     */
    boolean hasFieldName(String fieldName);

    List<FilterFieldDataBase<?>> getFields();

    /**
     * Get the Regex value of the Field Names that exist for the filter.
     */
    Map<String, String> getFieldNames();

    /**
     * Get the unaltered Field names that exist for the filter.
     */
    Map<String, String> getBaseFieldNames();

    /**
     * Get the description of a filter field for a property field name.
     *
     * @param fieldName the name of the field to get the description for.
     * @return The Field's description if the field exists, otherwise null.
     */
    String getFieldDesc(String fieldName);

    /**
     * Get the value of a filter field for a property field name.
     *
     * @param fieldName the name of the field to get the value for.
     * @return The Field's value if the field exists, otherwise null.
     */
    String getFieldValue(String fieldName);

    /**
     * Set the value of a filter field for a property field name.
     *
     * @param fieldName  The name of the field.
     * @param fieldValue The value of the field.
     */
    void setFieldValue(String fieldName, String fieldValue);

    /**
     * Sync the values saved in a Filter with this Field Filter.
     *
     * @param filter The Filter Entity to be synced with.
     */
    void syncWithFilter(Filter filter);
}
