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

package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.IntegerConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class IntegerConstantFilterQueryBuilder extends BaseFilterQueryBuilder<IntegerConstants> {
    public IntegerConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(IntegerConstants.class, new IntegerConstantFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("integerConstantsId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantName");
        } else if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR)) {
            addEqualsCondition("constantValue", (Integer) field.getData());
        } else {
            super.processField(field);
        }
    }
}
