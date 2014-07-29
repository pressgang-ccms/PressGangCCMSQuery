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

import org.jboss.pressgang.ccms.filter.UserFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class UserFilterQueryBuilder extends BaseFilterQueryBuilder<User> {

    public UserFilterQueryBuilder(final EntityManager entityManager) {
        super(User.class, new UserFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.USER_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("userId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.USER_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "userName");
        } else if (fieldName.equals(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "userDescription");
        } else {
            super.processField(field);
        }
    }
}
