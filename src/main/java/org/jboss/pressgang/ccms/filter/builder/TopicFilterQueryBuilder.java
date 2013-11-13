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
                        addExistsCondition(getMatchingMinHash(Integer.parseInt(components[0]), Float.parseFloat(components[1])));
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

    /**
     * Matching the minhash signature of a document relies on a process known as locality sensitive hashing.
     * A good explaination of this process can be found at http://infolab.stanford.edu/~ullman/mmds/ch3.pdf.
     *
     * To implement this feature, we need to group the minhash signatures into bands. When two topics share
     * the same minhash in a single band, they are considered a candidate pair for further testing.
     *
     * The documentation above suggests hashing the minhash values that fall into a band, and then comparing
     * these hashed band values to find candidates. Our implementation will defer this to the database by
     * finding the number of topics that have matching minhash values in all rows in a band.
     *
     * The number of rows and bands is calculated such that the threshold is approximately Math.pow(1/b, 1/r). This
     * formula means that increasing the threshold results in an increased number of rows and a decreased number
     * of bands. We get a close approximation by running through a bunch of combinations and seeing what fits best.
     *
     * @param topicId The topic whose minhash signature we will be matching to.
     * @param threshold How similar we want two documents to be to be considered a match. This will be forced to a value
     *                  between 0.6 and 0.9.
     * @return
     */
    public Subquery<Topic> getMatchingMinHash(final Integer topicId, final Float threshold) {
        try {
            Float fixedThreshold = Constants.MIN_DOCUMENT_SIMILARITY;
            if (threshold > Constants.MAX_DOCUMENT_SIMILARITY) {
                fixedThreshold = Constants.MAX_DOCUMENT_SIMILARITY;
            } else if (threshold >= Constants.MIN_DOCUMENT_SIMILARITY) {
                fixedThreshold = threshold;
            }

            Double lastThreshold = null;
            int lhsRows = 0;
            for (int rows = Constants.LSH_SIXTY_PERCENT_ROWS; rows < Constants.LSH_NINETY_PERCENT_ROWS; ++rows) {
                final int bands = Constants.NUM_MIN_HASHES / rows;
                final double thisThreshold = Math.pow(1/bands, 1/rows);

                if (lastThreshold == null) {
                    lastThreshold = thisThreshold;
                } else if (thisThreshold > fixedThreshold) {
                    lhsRows = rows - 1;
                    break;
                }
            }

            // The number of full bands that we be used to generate the sql query
            int bands = Constants.NUM_MIN_HASHES / lhsRows;
            // Any remaining topics that don't fall into a band
            if(Constants.NUM_MIN_HASHES % lhsRows != 0) {
                ++bands;
            }

            // get the source topic
            final Topic sourceTopic = getEntityManager().find(Topic.class, topicId);

            final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
            final CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
            final Root<MinHash> minHashRoot = criteriaQuery.from(MinHash.class);

            final Set<Integer> candidates = new HashSet<Integer>();

            for (int band = 0; band < bands; ++band) {
                final List<Predicate> rowMatches = new ArrayList<Predicate>();
                for (int row = band * lhsRows; row < Constants.NUM_MIN_HASHES; ++row) {
                    MinHash sourceMinHash = null;
                    for (final MinHash minHash : sourceTopic.getMinHashes()) {
                        if (row == minHash.getMinHashFuncID()) {
                            sourceMinHash = minHash;
                            break;
                        }
                    }

                    rowMatches.add(criteriaBuilder.and(
                        criteriaBuilder.equal(minHashRoot.<Integer>get("MinHashFuncID"), sourceMinHash.getMinHashFuncID()),
                        criteriaBuilder.equal(minHashRoot.<Integer>get("MinHash"), sourceMinHash.getMinHash())
                    ));
                }

                final Predicate minHashOrs = criteriaBuilder.or(rowMatches.toArray(new Predicate[]{}));

                final CriteriaQuery<Integer> query = criteriaQuery
                        .select(minHashRoot.<Integer>get("TopicID"))
                        .distinct(true)
                        .where(minHashOrs)
                        .groupBy(minHashRoot.<Integer>get("TopicID"))
                        .having(criteriaBuilder.equal(criteriaBuilder.count(criteriaBuilder.count(minHashRoot.<Integer>get("TopicID"))), rowMatches.size()));

                candidates.addAll(getEntityManager().createQuery(query).getResultList());
            }

            // at this point candidates should now list topic ids that are a potential match to the source topic.
            final CriteriaQuery<Topic> topicCQ = criteriaBuilder.createQuery(Topic.class);
            final Root<Topic> topicRoot = criteriaQuery.from(Topic.class);

            final CriteriaBuilder.In<Integer> in = criteriaBuilder.in(topicRoot.<Integer>get("TopicID"));
            for (final Integer candidate : candidates) {
                in.value(candidate);
            }

            final CriteriaQuery<Topic> topicQuery = topicCQ.select(topicRoot).where(in);

            final List<Topic> topics = getEntityManager().createQuery(topicQuery).getResultList();

            // we now have a list of topics that are possible candidates for a match
            final CriteriaBuilder.In<Integer> inSubQuery = criteriaBuilder.in(topicRoot.<Integer>get("TopicID"));
            for (final Topic topic : topics) {
                int matches = 0;
                for (final MinHash minHash : sourceTopic.getMinHashes()) {
                    for (final MinHash otherMinHash : topic.getMinHashes()) {
                        if (minHash.getMinHashFuncID().equals(otherMinHash.getMinHashFuncID())) {
                            if (minHash.getMinHash().equals(otherMinHash.getMinHash())) {
                                ++matches;
                            }
                            break;
                        }
                    }
                }

                if (matches / Constants.NUM_MIN_HASHES >= fixedThreshold) {
                    inSubQuery.value(topic.getId());
                }
            }

            final Subquery<Topic> subQuery = getCriteriaQuery().subquery(Topic.class);
            subQuery.where(inSubQuery);

            return subQuery;

        } catch (final Exception ex) {
            return null;
        }
    }
}
