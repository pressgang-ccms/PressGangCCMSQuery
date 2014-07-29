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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.StringConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class StringConstantFilterQueryBuilder extends BaseFilterQueryBuilder<StringConstants> {
    public StringConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(StringConstants.class, new StringConstantFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("stringConstantsId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantName");
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantValue");
        } else {
            super.processField(field);
        }
    }
}
