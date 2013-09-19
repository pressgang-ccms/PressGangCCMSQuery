package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.ProjectFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ProjectFilterQueryBuilder extends BaseFilterQueryBuilder<Project> {
    public ProjectFilterQueryBuilder(final EntityManager entityManager) {
        super(Project.class, new ProjectFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROJECT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("projectId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROJECT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "projectName");
        } else if (fieldName.equals(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "projectDescription");
        } else {
            super.processField(field);
        }
    }
}
