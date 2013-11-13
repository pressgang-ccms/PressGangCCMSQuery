package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;

import org.jboss.pressgang.ccms.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseTopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.MinHash;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.model.constants.Constants;
import org.jboss.pressgang.ccms.model.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        if (fieldName.equals(CommonFilterConstants.TOPIC_MIN_HASH)) {
            final String fieldStringValue = (String) field.getData();
            if (fieldStringValue != null) {
                final String[] components = fieldStringValue.split(":");
                if (components.length == 2) {
                    try {
                        final List<Integer> matchingTopics = TopicUtilities.getMatchingMinHash(getEntityManager(), Integer.parseInt(components[0]), Float.parseFloat(components[1]));
                         if (matchingTopics != null) {
                             addIdInCollectionCondition("topicId", matchingTopics);
                         }
                    } catch (final NumberFormatException ex) {

                    }
                }
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

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Predicate localePredicate = queryBuilder.equal(getRootPath().get("topicLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return queryBuilder.or(localePredicate, queryBuilder.isNull(getRootPath().get("topicLocale")));

        return localePredicate;
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale) {
        if (locale == null) return null;

        final String defaultLocale = System.getProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY);

        final CriteriaBuilder queryBuilder = getCriteriaBuilder();
        final Predicate localePredicate = queryBuilder.notEqual(getRootPath().get("topicLocale"), locale);

        if (defaultLocale != null && defaultLocale.toLowerCase().equals(locale.toLowerCase()))
            return queryBuilder.and(localePredicate, queryBuilder.isNotNull(getRootPath().get("topicLocale")));

        return localePredicate;
    }


}
