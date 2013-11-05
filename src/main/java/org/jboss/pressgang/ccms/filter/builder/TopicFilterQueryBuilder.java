package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseTopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.MinHash;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
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

        if (fieldName.equals(CommonFilterConstants.TOPIC_MIN_HASH)) {
            final Integer fieldIntegerValue = (Integer) field.getData();
            if (fieldIntegerValue != null) {
                addExistsCondition(getMatchingMinHash(fieldIntegerValue));
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

    public Subquery<MinHash> getMatchingMinHash(final Integer minHash) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<MinHash> subQuery = getCriteriaQuery().subquery(MinHash.class);
        final Root<MinHash> root = subQuery.from(MinHash.class);
        subQuery.select(root);

        // Create the condition
        final Predicate minHashMatch = criteriaBuilder.equal(root.get("minHash"), minHash);
        final Predicate topicIdMatch = criteriaBuilder.equal(getRootPath(), root.get("topic"));
        subQuery.where(criteriaBuilder.and(topicIdMatch, minHashMatch));

        return subQuery;
    }
}
