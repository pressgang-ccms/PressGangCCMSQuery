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

import javax.persistence.EntityManager;
import javax.persistence.criteria.Subquery;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanMapData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringMapData;
import org.jboss.pressgang.ccms.model.base.ToPropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Entities that have extended properties.
 *
 * @param <T> The Type of entity that should be returned by the query builder.
 * @param <U> The Type of the entity to PropertyTag mapping.
 */
public abstract class BaseFilterQueryBuilderWithProperties<T, U extends ToPropertyTag<U>> extends BaseFilterQueryBuilder<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseFilterQueryBuilderWithProperties.class);

    protected BaseFilterQueryBuilderWithProperties(final Class<T> clazz, final BaseFieldFilter fieldFilter, final EntityManager entityManager) {
        super(clazz, fieldFilter, entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROPERTY_TAG_EXISTS)) {
            final FilterFieldBooleanMapData mapField = (FilterFieldBooleanMapData) field;
                for (final Map.Entry<Integer, Boolean> entry : mapField.getData().entrySet()) {
                final Integer propertyTagId = entry.getKey();
                final Boolean fieldValueBoolean = entry.getValue();
                if (propertyTagId != null && fieldValueBoolean) {
                    addExistsCondition(getPropertyTagExistsSubquery(propertyTagId));
                }
            }
        } else if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG_NOT_EXISTS)) {
            final FilterFieldBooleanMapData mapField = (FilterFieldBooleanMapData) field;
            for (final Map.Entry<Integer, Boolean> entry : mapField.getData().entrySet()) {
                final Integer propertyTagId = entry.getKey();
                final Boolean fieldValueBoolean = entry.getValue();
                if (propertyTagId != null && fieldValueBoolean) {
                    addNotExistsCondition(getPropertyTagExistsSubquery(propertyTagId));
                }
            }
        } else if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG)) {
            final FilterFieldStringMapData mapField = (FilterFieldStringMapData) field;
            for (final Map.Entry<Integer, String> entry : mapField.getData().entrySet()) {
                final Integer propertyTagId = entry.getKey();
                final String fieldValue = entry.getValue();
                if (propertyTagId != null && fieldValue != null) {
                    addExistsCondition(getPropertyTagSubquery(propertyTagId, fieldValue));
                }
            }
        } else {
            super.processField(field);
        }
    }

    /**
     * Create a Subquery to check if a topic has a property tag with a specific value.
     *
     * @param propertyTagId    The ID of the property tag to be checked.
     * @param propertyTagValue The Value that the property tag should have.
     * @return A subquery that can be used in an exists statement to see if a topic has a property tag with the specified value.
     */
    protected abstract Subquery<U> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue);

    /**
     * Create a Subquery to check if a entity has a property tag exists.
     *
     * @param propertyTagId The ID of the property tag to be checked.
     * @return A subquery that can be used in an exists statement to see if a topic has a property tag.
     */
    protected abstract Subquery<U> getPropertyTagExistsSubquery(final Integer propertyTagId);
}
