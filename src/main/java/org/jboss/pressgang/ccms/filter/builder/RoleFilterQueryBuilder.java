package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.RoleFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class RoleFilterQueryBuilder extends BaseFilterQueryBuilder<Role> {
    public RoleFilterQueryBuilder(final EntityManager entityManager) {
        super(Role.class, new RoleFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.ROLE_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("roleId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.ROLE_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "roleName");
        } else if (fieldName.equals(CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "description");
        } else {
            super.processField(field);
        }
    }
}
