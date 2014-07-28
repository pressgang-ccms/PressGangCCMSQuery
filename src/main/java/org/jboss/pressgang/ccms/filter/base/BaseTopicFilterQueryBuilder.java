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

package org.jboss.pressgang.ccms.filter.base;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToBugzillaBug;
import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.joda.time.DateTime;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 *
 * @param <T> The Type of topic that should be returned by the query builder.
 */
public abstract class BaseTopicFilterQueryBuilder<T> extends BaseFilterQueryBuilderWithProperties<T,
        TopicToPropertyTag> implements ITagFilterQueryBuilder, ILocaleFilterQueryBuilder {
    private final Subquery<Topic> topicQuery;
    private final Root<Topic> from;
    private final boolean useTopicSubquery;

    protected BaseTopicFilterQueryBuilder(final Class<T> clazz, final BaseFieldFilter fieldFilter, final EntityManager entityManager) {
        this(clazz, fieldFilter, entityManager, false);
    }

    protected BaseTopicFilterQueryBuilder(final Class<T> clazz, final BaseFieldFilter fieldFilter, final EntityManager entityManager,
            final boolean useTopicSubquery) {
        super(clazz, fieldFilter, entityManager);
        this.useTopicSubquery = useTopicSubquery;

        if (useTopicSubquery) {
            topicQuery = super.getCriteriaQuery().subquery(Topic.class);
            from = topicQuery.from(Topic.class);
        } else {
            topicQuery = null;
            from = null;
        }
    }

    private DateTime startEditDate;
    private DateTime endEditDate;
    private Date startCreateDate;
    private Date endCreateDate;

    @Override
    protected Path<?> getRootPath() {
        if (useTopicSubquery) {
            return from;
        } else {
            return super.getRootPath();
        }
    }

    protected Path<?> getOriginalRootPath() {
        return super.getRootPath();
    }

    protected Subquery<Topic> getTopicSubquery() {
        return topicQuery;
    }

    @Override
    public void reset() {
        super.reset();
        startCreateDate = null;
        endCreateDate = null;
        startEditDate = null;
        endEditDate = null;
    }

    @Override
    public Predicate getFilterConditions() {
        if (startCreateDate != null || endCreateDate != null) {
            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            Predicate thisRestriction = null;

            if (startCreateDate != null) {
                thisRestriction = criteriaBuilder.greaterThanOrEqualTo(getRootPath().get("topicTimeStamp").as(Date.class), startCreateDate);
            }

            if (endCreateDate != null) {
                final Predicate restriction = criteriaBuilder.lessThanOrEqualTo(getRootPath().get("topicTimeStamp").as(Date.class),
                        endCreateDate);

                if (startCreateDate != null) {
                    thisRestriction = criteriaBuilder.and(thisRestriction, restriction);
                } else {
                    thisRestriction = restriction;
                }
            }

            if (thisRestriction != null) {
                addFieldCondition(thisRestriction);
            }
        }

        if (startEditDate != null || endEditDate != null) {
            final List<Integer> ids = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", startEditDate,
                    endEditDate);
            addIdInCollectionCondition("topicId", ids);
        }

        return super.getFilterConditions();
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.TOPIC_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("topicId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IDS_NOT_FILTER_VAR)) {
            addIdNotInCollectionCondition("topicId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IS_INCLUDED_IN_SPEC)) {
            final List<Integer> contentSpecIds = (List<Integer>) field.getData();
            final Set<Integer> relatedTopicIds = new HashSet<Integer>();
            for (final Integer contentSpecId : contentSpecIds) {
                final List<Integer> topicIds = EntityUtilities.getTopicsInContentSpec(getEntityManager(), contentSpecId);
                if (topicIds != null) {
                    relatedTopicIds.addAll(topicIds);
                }
            }

            addIdInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_IS_NOT_INCLUDED_IN_SPEC)) {
            final List<Integer> contentSpecIds = (List<Integer>) field.getData();
            final Set<Integer> relatedTopicIds = new HashSet<Integer>();
            for (final Integer contentSpecId : contentSpecIds) {
                final List<Integer> topicIds = EntityUtilities.getTopicsInContentSpec(getEntityManager(), contentSpecId);
                if (topicIds != null) {
                    relatedTopicIds.addAll(topicIds);
                }
            }

            addIdNotInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_TITLE_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "topicTitle");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_TITLE_NOT_FILTER_VAR)) {
            processNotStringField((FilterFieldStringData) field, "topicTitle");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_XML_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "topicXML");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_XML_NOT_FILTER_VAR)) {
            processNotStringField((FilterFieldStringData) field, "topicXML");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "topicText");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_DESCRIPTION_NOT_FILTER_VAR)) {
            processNotStringField((FilterFieldStringData) field, "topicText");
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_RELATIONSHIPS)) {
            addSizeGreaterThanOrEqualToCondition("parentTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_INCOMING_RELATIONSHIPS)) {
            addSizeGreaterThanOrEqualToCondition("childTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_RELATIONSHIPS)) {
            addSizeLessThanCondition("parentTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_INCOMING_RELATIONSHIPS)) {
            addSizeLessThanCondition("childTopicToTopics", 1);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_RELATED_TO)) {
            final List<Integer> relatedTopicIds = EntityUtilities.getIncomingRelatedTopicIDs(getEntityManager(), (Integer) field.getData());
            addIdInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_RELATED_TO)) {
            final List<Integer> relatedTopicIds = EntityUtilities.getIncomingRelatedTopicIDs(getEntityManager(), (Integer) field.getData());
            addIdNotInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_RELATED_FROM)) {
            final List<Integer> relatedTopicIds = EntityUtilities.getOutgoingRelatedTopicIDs(getEntityManager(), (Integer) field.getData());
            addIdInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_RELATED_FROM)) {
            final List<Integer> relatedTopicIds = EntityUtilities.getOutgoingRelatedTopicIDs(getEntityManager(), (Integer) field.getData());
            addIdNotInCollectionCondition("topicId", relatedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_XML_ERRORS)) {
            final Boolean hasXMLErrors = (Boolean) field.getData();
            if (hasXMLErrors) {
                final Predicate notEmptyPredicate = getCriteriaBuilder().notEqual(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"), "");
                final Predicate notNullPredicate = getCriteriaBuilder().isNotNull(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"));
                addFieldCondition(getCriteriaBuilder().and(notEmptyPredicate, notNullPredicate));
            }

        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_XML_ERRORS)) {
            final Boolean hasNotXMLErrors = (Boolean) field.getData();
            if (hasNotXMLErrors) {
                final Predicate emptyPredicate = getCriteriaBuilder().equal(getRootPath().get("topicSecondOrderData").get("topicXMLErrors"),
                        "");
                final Predicate nullPredicate = getCriteriaBuilder().isNull(
                        getRootPath().get("topicSecondOrderData").get("topicXMLErrors"));
                addFieldCondition(getCriteriaBuilder().or(emptyPredicate, nullPredicate));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_EDITED_IN_LAST_DAYS)) {
            final Integer days = (Integer) field.getData();
            final DateTime date = new DateTime().minusDays(days);
            final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
            addIdInCollectionCondition("topicId", editedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_DAYS)) {
            final Integer days = (Integer) field.getData();
            final DateTime date = new DateTime().minusDays(days);
            final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
            addIdNotInCollectionCondition("topicId", editedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
            addIdInCollectionCondition("topicId", editedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_EDITED_IN_LAST_MINUTES)) {
            final Integer minutes = (Integer) field.getData();
            final DateTime date = new DateTime().minusMinutes(minutes);
            final List<Integer> editedTopicIds = EntityUtilities.getEditedEntities(getEntityManager(), Topic.class, "topicId", date, null);
            addIdNotInCollectionCondition("topicId", editedTopicIds);
        } else if (fieldName.equals(CommonFilterConstants.STARTDATE_FILTER_VAR)) {
            startCreateDate = ((DateTime) field.getData()).toDate();
        } else if (fieldName.equals(CommonFilterConstants.ENDDATE_FILTER_VAR)) {
            endCreateDate = ((DateTime) field.getData()).toDate();
        } else if (fieldName.equals(CommonFilterConstants.STARTEDITDATE_FILTER_VAR)) {
            startEditDate = (DateTime) field.getData();
        } else if (fieldName.equals(CommonFilterConstants.ENDEDITDATE_FILTER_VAR)) {
            endEditDate = (DateTime) field.getData();
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_OPEN_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().exists(getOpenBugzillaSubquery()));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_OPEN_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().not(getCriteriaBuilder().exists(getOpenBugzillaSubquery())));
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addSizeGreaterThanOrEqualToCondition("topicToBugzillaBugs", 1);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_HAS_NOT_BUGZILLA_BUGS)) {
            final Boolean fieldValueBoolean = (Boolean) field.getData();
            if (fieldValueBoolean) {
                addSizeLessThanCondition("topicToBugzillaBugs", 1);
            }
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_FORMAT_VAR)) {
            addEqualsCondition("xmlFormat", (Integer) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.TOPIC_NOT_FORMAT_VAR)) {
            addNotEqualsCondition("xmlFormat", (Integer) field.getData());
        } else {
            super.processField(field);
        }
    }

    @Override
    protected Subquery<TopicToPropertyTag> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToPropertyTag> subQuery = getCriteriaQuery().subquery(TopicToPropertyTag.class);
        final Root<TopicToPropertyTag> root = subQuery.from(TopicToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        final Predicate propertyTagValueMatch = criteriaBuilder.equal(root.get("value"), propertyTagValue);
        subQuery.where(criteriaBuilder.and(topicIdMatch, propertyTagIdMatch, propertyTagValueMatch));

        return subQuery;
    }

    @Override
    protected Subquery<TopicToPropertyTag> getPropertyTagExistsSubquery(final Integer propertyTagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToPropertyTag> subQuery = getCriteriaQuery().subquery(TopicToPropertyTag.class);
        final Root<TopicToPropertyTag> root = subQuery.from(TopicToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        subQuery.where(criteriaBuilder.and(topicIdMatch, propertyTagIdMatch));

        return subQuery;
    }

    /**
     * Create a Subquery to check if a topic has open bugs.
     *
     * @return A subquery that can be used in an exists statement to see if a topic has open bugs.
     */
    private Subquery<TopicToBugzillaBug> getOpenBugzillaSubquery() {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToBugzillaBug> subQuery = getCriteriaQuery().subquery(TopicToBugzillaBug.class);
        final Root<TopicToBugzillaBug> root = subQuery.from(TopicToBugzillaBug.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        final Predicate bugOpenMatch = criteriaBuilder.isTrue(root.get("bugzillaBug").get("bugzillaBugOpen").as(Boolean.class));
        subQuery.where(criteriaBuilder.and(topicIdMatch, bugOpenMatch));

        return subQuery;
    }
}
