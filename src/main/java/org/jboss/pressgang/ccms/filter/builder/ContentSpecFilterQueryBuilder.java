package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 */
public class ContentSpecFilterQueryBuilder extends BaseFilterQueryBuilder<ContentSpec> implements ITagFilterQueryBuilder,
        ILocaleFilterQueryBuilder {

    public ContentSpecFilterQueryBuilder(final EntityManager entityManager) {
        super(ContentSpec.class, entityManager);
    }

    @Override
    public Predicate getMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subquery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subquery.from(TopicToTag.class);
        final Predicate topic = criteriaBuilder.equal(from.get("contentSpec").get("contentSpecId"), getRootPath().get("contentSpecId"));
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        subquery.select(from);
        subquery.where(criteriaBuilder.and(topic, tag));

        return criteriaBuilder.exists(subquery);
    }

    @Override
    public Predicate getNotMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subquery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subquery.from(TopicToTag.class);
        final Predicate topic = criteriaBuilder.equal(from.get("contentSpec").get("contentSpecId"), getRootPath().get("contentSpecId"));
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        subquery.select(from);
        subquery.where(criteriaBuilder.and(topic, tag));

        return criteriaBuilder.not(criteriaBuilder.exists(subquery));
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Predicate localePredicate = criteriaBuilder.equal(getRootPath().get("locale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return criteriaBuilder.or(localePredicate, criteriaBuilder.isNull(getRootPath().get("locale")));

        return localePredicate;
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Predicate localePredicate = criteriaBuilder.notEqual(getRootPath().get("locale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return criteriaBuilder.and(localePredicate, criteriaBuilder.isNotNull(getRootPath().get("locale")));

        return localePredicate;
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.LOGIC_FILTER_VAR)) {
            filterFieldsLogic = fieldValue;
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeparatedListCondition("contentSpecId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR)) {
            addExistsCondition(getMetaDataSubquery("Title", fieldValue));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR)) {
            addExistsCondition(getMetaDataSubquery("Product", fieldValue));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR)) {
            addExistsCondition(getMetaDataSubquery("Version", fieldValue));
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

    /**
     * Create a Subquery to check if a Content Spec has a metadata field with the specified value.
     *
     * @param metaDataTitle The Title of the metadata to be checked.
     * @param metaDataValue The Value that the metadata should have.
     * @return A subquery that can be used in an exists statement to see if a Content Spec has a metadata field with the specified value.
     */
    private Subquery<CSNode> getMetaDataSubquery(final String metaDataTitle, final String metaDataValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<CSNode> subQuery = getCriteriaQuery().subquery(CSNode.class);
        final Root<CSNode> root = subQuery.from(CSNode.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate contentSpecIdMatch = criteriaBuilder.equal(getRootPath(), root.get("contentSpec"));
        final Predicate isMetaData = criteriaBuilder.equal(root.get("CSNodeType").as(Integer.class), CommonConstants.CS_NODE_META_DATA);
        final Predicate metaDataTitleMatch = criteriaBuilder.equal((root.get("CSNodeTitle").as(String.class)), metaDataTitle);
        final Predicate metaDataValueMatch = criteriaBuilder.equal(root.get("value"), metaDataValue);
        subQuery.where(criteriaBuilder.and(contentSpecIdMatch, isMetaData, metaDataTitleMatch, metaDataValueMatch));

        return subQuery;
    }
}
