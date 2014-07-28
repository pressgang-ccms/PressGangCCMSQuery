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
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

import org.jboss.pressgang.ccms.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseTopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

/**
 * Provides the query elements required by Filter.buildQuery() to get a list of Topic elements
 */
public class TopicFilterQueryBuilder extends BaseTopicFilterQueryBuilder<Topic> {

    public TopicFilterQueryBuilder(final EntityManager entityManager) {
        super(Topic.class, new TopicFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.TOPIC_MIN_HASH_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final String[] components = fieldStringValue.split(":");
                if (components.length == 2) {
                    try {
                        final Integer sourceTopicId = Integer.parseInt(components[0]);
                        final Float threshold = Float.parseFloat(components[1]);

                        if (threshold < org.jboss.pressgang.ccms.model.constants.Constants.MIN_DOCUMENT_SIMILARITY ||
                                threshold > org.jboss.pressgang.ccms.model.constants.Constants.MAX_DOCUMENT_SIMILARITY) {
                            throw new IllegalArgumentException("The threshold measurement on the " + CommonFilterConstants.TOPIC_MIN_HASH_VAR +
                                    " filter option must be between " + org.jboss.pressgang.ccms.model.constants.Constants.MIN_DOCUMENT_SIMILARITY +
                                    " and " + org.jboss.pressgang.ccms.model.constants.Constants.MAX_DOCUMENT_SIMILARITY);
                        }

                        final List<Integer> matchingTopics = TopicUtilities.getMatchingMinHash(getEntityManager(), sourceTopicId, threshold);
                        if (matchingTopics != null) {
                            addIdInCollectionCondition("topicId", matchingTopics);
                        }
                    } catch (final NumberFormatException ex) {

                    }
                }
            }
        } else if (fieldName.equals(CommonFilterConstants.CREATED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getCreatedBy(getEntityManager(), Topic.class, "topicId", fieldStringValue);
                addIdInCollectionCondition("topicId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.NOT_CREATED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getCreatedBy(getEntityManager(), Topic.class, "topicId", fieldStringValue);
                addIdNotInCollectionCondition("topicId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.EDITED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getEditedBy(getEntityManager(), Topic.class, "topicId", fieldStringValue);
                addIdInCollectionCondition("topicId", ids);
            }
        } else if (fieldName.equals(CommonFilterConstants.NOT_EDITED_BY_VAR)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final List<Integer> ids = EntityUtilities.getEditedBy(getEntityManager(), Topic.class, "topicId", fieldStringValue);
                addIdNotInCollectionCondition("topicId", ids);
            }
        } else {
            super.processField(field);
        }
    }

    @Override
    public Predicate getMatchTagString(final Integer tagId) {
        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subQuery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subQuery.from(TopicToTag.class);
        final Predicate topic = queryBuilder.equal(from.get("topic"), getRootPath());
        final Predicate tag = queryBuilder.equal(from.get("tag").get("tagId"), tagId);
        subQuery.select(from);
        subQuery.where(queryBuilder.and(topic, tag));

        return queryBuilder.exists(subQuery);
    }

    @Override
    public Predicate getNotMatchTagString(final Integer tagId) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TopicToTag> subQuery = getCriteriaQuery().subquery(TopicToTag.class);
        final Root<TopicToTag> from = subQuery.from(TopicToTag.class);
        final Predicate topic = criteriaBuilder.equal(from.get("topic"), getRootPath());
        final Predicate tag = criteriaBuilder.equal(from.get("tag").get("tagId"), tagId);
        subQuery.select(from);
        subQuery.where(criteriaBuilder.and(topic, tag));

        return criteriaBuilder.not(criteriaBuilder.exists(subQuery));
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = ApplicationConfig.getInstance().getDefaultLocale();

        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Predicate localePredicate = queryBuilder.equal(getRootPath().get("topicLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return queryBuilder.or(localePredicate, queryBuilder.isNull(getRootPath().get("topicLocale")));

        return localePredicate;
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = ApplicationConfig.getInstance().getDefaultLocale();

        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Predicate localePredicate = queryBuilder.notEqual(getRootPath().get("topicLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return queryBuilder.and(localePredicate, queryBuilder.isNotNull(getRootPath().get("topicLocale")));

        return localePredicate;
    }


}
