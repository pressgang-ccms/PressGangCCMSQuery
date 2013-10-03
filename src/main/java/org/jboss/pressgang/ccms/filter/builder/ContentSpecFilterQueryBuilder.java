package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

import org.jboss.pressgang.ccms.filter.ContentSpecFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilderWithProperties;
import org.jboss.pressgang.ccms.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterStringLogic;
import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToTag;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 */
public class ContentSpecFilterQueryBuilder extends BaseFilterQueryBuilderWithProperties<ContentSpec,
        ContentSpecToPropertyTag> implements ITagFilterQueryBuilder, ILocaleFilterQueryBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ContentSpecFilterQueryBuilder.class);

    private DateTime startEditDate;
    private DateTime endEditDate;

    public ContentSpecFilterQueryBuilder(final EntityManager entityManager) {
        super(ContentSpec.class, new ContentSpecFieldFilter(), entityManager);
    }

    @Override
    public void reset() {
        super.reset();
        startEditDate = null;
        endEditDate = null;
    }

    @Override
    public Predicate getMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<ContentSpecToTag> subquery = getCriteriaQuery().subquery(ContentSpecToTag.class);
        final Root<ContentSpecToTag> from = subquery.from(ContentSpecToTag.class);
        final Predicate contentSpec = criteriaBuilder.equal(from.get("contentSpec").get("contentSpecId"),
                getRootPath().get("contentSpecId"));
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        final Predicate notBookTag = criteriaBuilder.equal(from.get("bookTag"), false);
        subquery.select(from);
        subquery.where(criteriaBuilder.and(contentSpec, tag, notBookTag));

        return criteriaBuilder.exists(subquery);
    }

    @Override
    public Predicate getNotMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<ContentSpecToTag> subquery = getCriteriaQuery().subquery(ContentSpecToTag.class);
        final Root<ContentSpecToTag> from = subquery.from(ContentSpecToTag.class);
        final Predicate contentSpec = criteriaBuilder.equal(from.get("contentSpec").get("contentSpecId"),
                getRootPath().get("contentSpecId"));
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        final Predicate notBookTag = criteriaBuilder.equal(from.get("bookTag"), false);
        subquery.select(from);
        subquery.where(criteriaBuilder.and(contentSpec, tag, notBookTag));

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
    public Predicate getFilterConditions() {
        if (startEditDate != null || endEditDate != null) {
            final List<Integer> ids = EntityUtilities.getEditedEntities(getEntityManager(), ContentSpec.class, "contentSpecId",
                    startEditDate, endEditDate);
            addIdInCollectionCondition("contentSpecId", ids);
        }

        return super.getFilterConditions();
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("contentSpecId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_TYPE_FILTER_VAR)) {
            addEqualsCondition("contentSpecType", (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_TITLE_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_SUBTITLE_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_SUBTITLE_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_PRODUCT_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_VERSION_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_EDITION_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_EDITION_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_ABSTRACT_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_BRAND_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_COPYRIGHT_HOLDER_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_PUBLICAN_CFG_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.EDITED_IN_LAST_DAYS)) {
            final DateTime date = new DateTime().minusDays((Integer) field.getData());
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), ContentSpec.class,
                    "contentSpecId", date, null);
            addIdInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS)) {
                final Integer days = (Integer) field.getData();
            final DateTime date = new DateTime().minusDays((Integer) field.getData());
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class,
                    "contentSpecId", date, null);
            addIdNotInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class,
                    "contentSpecId", date, null);
            addIdInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class,
                    "contentSpecId", date, null);
            addIdNotInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.STARTEDITDATE_FILTER_VAR)) {
            startEditDate = (DateTime) field.getData();
        } else if (fieldName.equals(CommonFilterConstants.ENDEDITDATE_FILTER_VAR)) {
            endEditDate = (DateTime) field.getData();
        } else if (fieldName.equals(CommonFilterConstants.HAS_ERRORS_FILTER_VAR)) {
            final Boolean hasErrors = (Boolean) field.getData();
            if (hasErrors) {
                final Predicate notEmptyPredicate = getCriteriaBuilder().notEqual(getRootPath().get("failedContentSpec"), "");
                final Predicate notNullPredicate = getCriteriaBuilder().isNotNull(getRootPath().get("failedContentSpec"));
                addFieldCondition(getCriteriaBuilder().and(notEmptyPredicate, notNullPredicate));
            }
        } else {
            super.processField(field);
        }
    }

    @Override
    protected Subquery<ContentSpecToPropertyTag> getPropertyTagSubquery(Integer propertyTagId, String propertyTagValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<ContentSpecToPropertyTag> subQuery = getCriteriaQuery().subquery(ContentSpecToPropertyTag.class);
        final Root<ContentSpecToPropertyTag> root = subQuery.from(ContentSpecToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate contentSpecIdMatch = criteriaBuilder.equal(getRootPath(), root.get("contentSpec"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        final Predicate propertyTagValueMatch = criteriaBuilder.equal(root.get("value"), propertyTagValue);
        subQuery.where(criteriaBuilder.and(contentSpecIdMatch, propertyTagIdMatch, propertyTagValueMatch));

        return subQuery;
    }

    @Override
    protected Subquery<ContentSpecToPropertyTag> getPropertyTagExistsSubquery(Integer propertyTagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<ContentSpecToPropertyTag> subQuery = getCriteriaQuery().subquery(ContentSpecToPropertyTag.class);
        final Root<ContentSpecToPropertyTag> root = subQuery.from(ContentSpecToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate contentSpecIdMatch = criteriaBuilder.equal(getRootPath(), root.get("contentSpec"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        subQuery.where(criteriaBuilder.and(contentSpecIdMatch, propertyTagIdMatch));

        return subQuery;
    }

    /**
     * Create a Subquery to check if a Content Spec has a metadata field with the specified value.
     *
     * @param metaDataTitle The Title of the metadata to be checked.
     * @param metaDataValue The Value that the metadata should have.
     * @return A subquery that can be used in an exists statement to see if a Content Spec has a metadata field with the specified value.
     */
    private Subquery<CSNode> getMetaDataSubquery(final String metaDataTitle, final String metaDataValue,
            final FilterStringLogic searchLogic) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<CSNode> subQuery = getCriteriaQuery().subquery(CSNode.class);
        final Root<CSNode> root = subQuery.from(CSNode.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate contentSpecIdMatch = criteriaBuilder.equal(getRootPath(), root.get("contentSpec"));
        final Predicate isMetaData = criteriaBuilder.equal(root.get("CSNodeType").as(Integer.class), CommonConstants.CS_NODE_META_DATA);
        final Predicate metaDataTitleMatch = criteriaBuilder.equal((root.get("CSNodeTitle").as(String.class)), metaDataTitle);
        final Predicate metaDataValueMatch;
        if (searchLogic == FilterStringLogic.MATCHES) {
            metaDataValueMatch = criteriaBuilder.equal(root.get("additionalText").as(String.class), metaDataValue);
        } else {
            final Expression<String> field = criteriaBuilder.lower(root.get("additionalText").as(String.class));
            metaDataValueMatch = criteriaBuilder.like(field, "%" + cleanLikeCondition(metaDataValue.toLowerCase()) + "%");
        }
        subQuery.where(criteriaBuilder.and(contentSpecIdMatch, isMetaData, metaDataTitleMatch, metaDataValueMatch));

        return subQuery;
    }
}
