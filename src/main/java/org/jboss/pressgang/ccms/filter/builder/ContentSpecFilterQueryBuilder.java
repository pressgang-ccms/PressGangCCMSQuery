/*
  Copyright 2011-2014 Red Hat, Inc

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
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
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

        final String defaultLocale = ApplicationConfig.getInstance().getDefaultLocale();

        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Predicate localePredicate = criteriaBuilder.equal(getRootPath().get("locale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return criteriaBuilder.or(localePredicate, criteriaBuilder.isNull(getRootPath().get("locale")));

        return localePredicate;
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = ApplicationConfig.getInstance().getDefaultLocale();

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
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_BOOK_VERSION_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(
                    getMetaDataSubquery(CommonConstants.CS_BOOK_VERSION_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_ABSTRACT_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(getMetaDataSubquery(CommonConstants.CS_BRAND_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(
                    getMetaDataSubquery(CommonConstants.CS_COPYRIGHT_HOLDER_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_YEAR_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(
                    getMetaDataSubquery(CommonConstants.CS_COPYRIGHT_YEAR_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR)) {
            final FilterFieldStringData stringField = (FilterFieldStringData) field;
            addExistsCondition(
                    getMetaDataSubquery(CommonConstants.CS_PUBLICAN_CFG_TITLE, stringField.getData(), stringField.getSearchLogic()));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_FORMAT_FILTER_VAR)) {
            final Integer formatId = (Integer) field.getData();
            final String formatString;
            switch (formatId) {
                case 1:
                    formatString = CommonConstants.DOCBOOK_50_TITLE;
                    break;
                case 0:
                default:
                    formatString = CommonConstants.DOCBOOK_45_TITLE;
            }
            addFieldCondition(getFormatPredicate(formatString, false));
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NOT_FORMAT_FILTER_VAR)) {
            final Integer formatId = (Integer) field.getData();
            final String formatString;
            switch (formatId) {
                case 1:
                    formatString = CommonConstants.DOCBOOK_50_TITLE;
                    break;
                case 0:
                default:
                    formatString = CommonConstants.DOCBOOK_45_TITLE;
            }
            addFieldCondition(getFormatPredicate(formatString, true));
        } else if (fieldName.equals(CommonFilterConstants.EDITED_IN_LAST_DAYS)) {
            final DateTime date = new DateTime().minusDays((Integer) field.getData());
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), ContentSpec.class,
                    "contentSpecId", date, null);
            addIdInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS)) {
            final Integer days = (Integer) field.getData();
            final DateTime date = new DateTime().minusDays((Integer) field.getData());
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "contentSpecId",
                    date, null);
            addIdNotInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "contentSpecId",
                    date, null);
            addIdInCollectionCondition("contentSpecId", editedContentSpecIds);
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedContentSpecIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "contentSpecId",
                    date, null);
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
        } else if (fieldName.equals(CommonFilterConstants.CREATED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getCreatedBy(getEntityManager(), ContentSpec.class, "contentSpecId", fieldStringValue);
                addIdInCollectionCondition("contentSpecId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.NOT_CREATED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getCreatedBy(getEntityManager(), ContentSpec.class, "contentSpecId",
                        fieldStringValue);
                addIdNotInCollectionCondition("contentSpecId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.EDITED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getEditedBy(getEntityManager(), ContentSpec.class, "contentSpecId", fieldStringValue);
                addIdInCollectionCondition("contentSpecId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getEditedBy(getEntityManager(), ContentSpec.class, "contentSpecId", fieldStringValue);
                addIdNotInCollectionCondition("contentSpecId", ids);
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

    protected Predicate getFormatPredicate(final String format, boolean not) {
        final Subquery<CSNode> formatSubQuery = getMetaDataSubquery(CommonConstants.CS_FORMAT_TITLE, format, FilterStringLogic.MATCHES);
        final Predicate formatExists;
        if (not) {
            formatExists = getCriteriaBuilder().not(getCriteriaBuilder().exists(formatSubQuery));
        } else {
            formatExists = getCriteriaBuilder().exists(formatSubQuery);
        }

        // DocBook 4.5 is the default so find any content specs that don't have a "Format" metadata
        if (CommonConstants.DOCBOOK_45_TITLE.equals(format)) {
            final Predicate notExists = getCriteriaBuilder().exists(getMetaDataDoesntExistSubquery(CommonConstants.CS_FORMAT_TITLE));
            if (not) {
                return getCriteriaBuilder().and(formatExists, notExists);
            } else {
                return getCriteriaBuilder().or(formatExists, getCriteriaBuilder().not(notExists));
            }
        } else {
            return formatExists;
        }
    }

    /**
     * Create a Subquery to check if a Content Spec has a metadata field with the specified value.
     *
     * @param metaDataTitle The Title of the metadata to be checked.
     * @return A subquery that can be used in an exists statement to see if a Content Spec has a metadata field with the specified value.
     */
    private Subquery<CSNode> getMetaDataDoesntExistSubquery(final String metaDataTitle) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<CSNode> subQuery = getCriteriaQuery().subquery(CSNode.class);
        final Root<CSNode> root = subQuery.from(CSNode.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate contentSpecIdMatch = criteriaBuilder.equal(getRootPath(), root.get("contentSpec"));
        final Predicate isMetaData = criteriaBuilder.equal(root.get("CSNodeType").as(Integer.class), CommonConstants.CS_NODE_META_DATA);
        final Predicate metaDataTitleMatch = criteriaBuilder.equal((root.get("CSNodeTitle").as(String.class)), metaDataTitle);
        subQuery.where(criteriaBuilder.and(contentSpecIdMatch, isMetaData, metaDataTitleMatch));

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
