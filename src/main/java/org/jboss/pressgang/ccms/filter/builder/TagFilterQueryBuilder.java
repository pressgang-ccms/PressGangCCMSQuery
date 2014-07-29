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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

import org.jboss.pressgang.ccms.filter.TagFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilderWithProperties;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToProject;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class TagFilterQueryBuilder extends BaseFilterQueryBuilderWithProperties<Tag, TagToPropertyTag> {
    public TagFilterQueryBuilder(final EntityManager entityManager) {
        super(Tag.class, new TagFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.TAG_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("tagId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.TAG_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "tagName");
        } else if (fieldName.equals(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "tagDescription");
        } else if (fieldName.equals(CommonFilterConstants.PROJECT_IDS_FILTER_VAR)) {
            final List<Integer> projectIds = (List<Integer>) field.getData();
            addExistsCondition(getTagInProjectSubquery(projectIds));
        } else if (fieldName.equals(CommonFilterConstants.NOT_PROJECT_IDS_FILTER_VAR)) {
            final List<Integer> projectIds = (List<Integer>) field.getData();
            addNotExistsCondition(getTagInProjectSubquery(projectIds));
        } else {
            super.processField(field);
        }
    }

    @Override
    protected Subquery<TagToPropertyTag> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TagToPropertyTag> subQuery = getCriteriaQuery().subquery(TagToPropertyTag.class);
        final Root<TagToPropertyTag> root = subQuery.from(TagToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate tagIdMatch = criteriaBuilder.equal(getRootPath(), root.get("tag"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        final Predicate propertyTagValueMatch = criteriaBuilder.equal(root.get("value"), propertyTagValue);
        subQuery.where(criteriaBuilder.and(tagIdMatch, propertyTagIdMatch, propertyTagValueMatch));

        return subQuery;
    }

    @Override
    protected Subquery<TagToPropertyTag> getPropertyTagExistsSubquery(final Integer propertyTagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TagToPropertyTag> subQuery = getCriteriaQuery().subquery(TagToPropertyTag.class);
        final Root<TagToPropertyTag> root = subQuery.from(TagToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate tagIdMatch = criteriaBuilder.equal(getRootPath(), root.get("tag"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        subQuery.where(criteriaBuilder.and(tagIdMatch, propertyTagIdMatch));

        return subQuery;
    }

    protected Subquery<TagToProject> getTagInProjectSubquery(final List<Integer> projectIds) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TagToProject> subQuery = getCriteriaQuery().subquery(TagToProject.class);
        final Root<TagToProject> root = subQuery.from(TagToProject.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate tagIdMatch = criteriaBuilder.equal(getRootPath(), root.get("tag"));
        final Predicate projectIdMatch = root.get("project").get("projectId").in(projectIds);
        subQuery.where(criteriaBuilder.and(tagIdMatch, projectIdMatch));

        return subQuery;
    }
}
