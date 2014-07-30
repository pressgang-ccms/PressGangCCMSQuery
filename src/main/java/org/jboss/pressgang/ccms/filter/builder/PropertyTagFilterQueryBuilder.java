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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.PropertyTagFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class PropertyTagFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTag> {
    public PropertyTagFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTag.class, new PropertyTagFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("propertyTagId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagName");
        } else if (fieldName.equals(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagDescription");
        } else {
            super.processField(field);
        }
    }
}
