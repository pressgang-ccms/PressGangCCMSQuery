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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

import org.jboss.pressgang.ccms.filter.ProcessFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.joda.time.DateTime;

public class ProcessFilterQueryBuilder extends BaseFilterQueryBuilder<Process> {
    private Date startDate;
    private Date endDate;

    public ProcessFilterQueryBuilder(final EntityManager entityManager) {
        super(Process.class, new ProcessFieldFilter(), entityManager);
    }

    @Override
    public void reset() {
        super.reset();
        startDate = null;
        endDate = null;
    }

    @Override
    public Predicate getFilterConditions() {
        if (startDate != null || endDate != null) {
            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            Predicate thisRestriction = null;

            if (startDate != null) {
                thisRestriction = criteriaBuilder.greaterThanOrEqualTo(getRootPath().get("startTime").as(Date.class), startDate);
            }

            if (endDate != null) {
                final Predicate restriction = criteriaBuilder.lessThanOrEqualTo(getRootPath().get("startTime").as(Date.class),
                        endDate);

                if (startDate != null) {
                    thisRestriction = criteriaBuilder.and(thisRestriction, restriction);
                } else {
                    thisRestriction = restriction;
                }
            }

            if (thisRestriction != null) {
                addFieldCondition(thisRestriction);
            }
        }

        return super.getFilterConditions();
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROCESS_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("uuid", (List<String>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROCESS_STATUS_FILTER_VAR)) {
            addEqualsCondition("status", (String) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROCESS_TYPE_FILTER_VAR)) {
            addEqualsCondition("type", (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.STARTDATE_FILTER_VAR)) {
            startDate = ((DateTime) field.getData()).toDate();
        } else if (fieldName.equals(CommonFilterConstants.ENDDATE_FILTER_VAR)) {
            endDate = ((DateTime) field.getData()).toDate();
        } else {
            super.processField(field);
        }
    }

}
