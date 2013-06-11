package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilderWithProperties;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class TagFilterQueryBuilder extends BaseFilterQueryBuilderWithProperties<Tag, TagToPropertyTag> {
    public TagFilterQueryBuilder(final EntityManager entityManager) {
        super(Tag.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.TAG_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("tagId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TAG_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagDescription", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
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
}
