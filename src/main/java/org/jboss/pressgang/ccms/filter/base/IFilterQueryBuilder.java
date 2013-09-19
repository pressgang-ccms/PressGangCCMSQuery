package org.jboss.pressgang.ccms.filter.base;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 * A set of methods that are the Bare Requirements to create a Filter Query Builder, that will be able to construct a JPA Query
 * from a filter.
 *
 * @param <T> The Entity Type that the query builder will return values for.
 */
public interface IFilterQueryBuilder<T> {
    /**
     * Processes the assigned filter filters and converts them into conditions that can be used in a query.
     */
    void process();

    /**
     * Adds a Filter Key/Value Pair string so that the relevant condition is added to the query builder.
     *
     * @param fieldName
     * @param fieldValue
     */
    void addFilterField(String fieldName, String fieldValue);

    /**
     * Get the where JPA predicate for the filter.
     *
     * @return A Predicate Object that represents the conditions set on the QUery Builder or null if there are no conditions.
     */
    Predicate getFilterConditions();

    /**
     * Get the Base Select All query for the Query.
     *
     * @return A CriteriaQuery object that will select all of the content for the Query Entity Type.
     */
    CriteriaQuery<T> getBaseCriteriaQuery();

    /**
     * Get the JPA Criteria Builder used by the Query Builder.
     *
     * @return The JPA CriteriaBuilder object used by the Query Builder.
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * Reset the Query Builder to its base state.
     */
    void reset();
}
