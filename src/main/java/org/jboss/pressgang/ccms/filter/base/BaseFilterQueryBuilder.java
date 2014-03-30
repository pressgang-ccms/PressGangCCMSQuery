package org.jboss.pressgang.ccms.filter.base;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.pressgang.ccms.filter.constants.FilterConstants;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.filter.structures.FilterStringLogic;
import org.jboss.pressgang.ccms.filter.utils.JPAUtils;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFilterQueryBuilder<T> implements IFilterQueryBuilder<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseFilterQueryBuilder.class);

    protected String filterFieldsLogic = FilterConstants.LOGIC_FILTER_VAR_DEFAULT_VALUE;
    private final List<Predicate> fieldConditions = new ArrayList<Predicate>();
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> criteriaQuery;
    private final Root<T> from;
    private final Class<T> clazz;
    private final EntityManager entityManager;
    private final BaseFieldFilter fieldFilter;
    private boolean processed = false;

    protected BaseFilterQueryBuilder(final Class<T> clazz, final BaseFieldFilter fieldFilter, final EntityManager entityManager) {
        criteriaBuilder = entityManager.getCriteriaBuilder();
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.fieldFilter = fieldFilter;
        criteriaQuery = criteriaBuilder.createQuery(clazz);
        from = criteriaQuery.from(clazz);
        criteriaQuery.select(from);
    }

    protected void processField(final FilterFieldDataBase<?> field) {
        if (field.getName().equals(CommonFilterConstants.LOGIC_FILTER_VAR)) {
            filterFieldsLogic = field.getDataString();
        }
    }

    public void process() {
        // If this has been processed before then reset and start from scratch
        if (processed) {
            reset();
        }

        // Process the filter strings
        processed = true;
        for (final FilterFieldDataBase<?> field : getFieldFilter().getFields()) {
            // Only process fields that have assigned values
            if (field.getData() != null) {
                processField(field);
            }
        }
    }

    @Override
    public void addFilterField(final String fieldName, final String fieldValue) {
        if (getFieldFilter().hasFieldName(fieldName)) {
            getFieldFilter().setFieldValue(fieldName, fieldValue);
        } else {
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
        }
    }

    @Override
    public Predicate getFilterConditions() {
        if (!processed) {
            process();
        }

        if (fieldConditions.isEmpty()) return null;

        if (fieldConditions.size() > 1) {
            final Predicate[] predicates = fieldConditions.toArray(new Predicate[fieldConditions.size()]);
            if (filterFieldsLogic.equalsIgnoreCase(CommonFilterConstants.OR_LOGIC)) {
                return criteriaBuilder.or(predicates);
            } else {
                return criteriaBuilder.and(predicates);
            }
        } else {
            return fieldConditions.get(0);
        }
    }

    protected BaseFieldFilter getFieldFilter() {
        return fieldFilter;
    }

    @Override
    public CriteriaQuery<T> getBaseCriteriaQuery() {
        final CriteriaQuery<T> clone = criteriaBuilder.createQuery(clazz);
        JPAUtils.copyCriteria(criteriaQuery, clone);
        return clone;
    }

    protected CriteriaQuery<T> getCriteriaQuery() {
        return criteriaQuery;
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public Root<T> getCriteriaRoot() {
        return from;
    }

    @Override
    public void reset() {
        fieldConditions.clear();
        getFieldFilter().resetAllValues();
        processed = false;
    }

    protected Path<?> getRootPath() {
        return from;
    }

    protected List<Predicate> getFieldConditions() {
        return fieldConditions;
    }

    protected void addFieldCondition(final Predicate condition) {
        fieldConditions.add(condition);
    }

    protected void processStringField(final FilterFieldStringData field, final String propertyName) {
        final FilterFieldStringData stringField = (FilterFieldStringData) field;
        if (stringField.getSearchLogic() == FilterStringLogic.MATCHES) {
            addEqualsCondition(propertyName, stringField.getData());
        } else {
            if (stringField.isCaseInsensitive()) {
                addLikeCondition(propertyName, stringField.getData());
            } else {
                addLikeIgnoresCaseCondition(propertyName, stringField.getData());
            }
        }
    }

    protected void processNotStringField(final FilterFieldStringData field, final String propertyName) {
        final FilterFieldStringData stringField = (FilterFieldStringData) field;
        if (stringField.getSearchLogic() == FilterStringLogic.MATCHES) {
            addNotEqualsCondition(propertyName, stringField.getData());
        } else {
            if (stringField.isCaseInsensitive()) {
                addNotLikeCondition(propertyName, stringField.getData());
            } else {
                addNotLikeIgnoresCaseCondition(propertyName, stringField.getData());
            }
        }
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field LIKE '%value%'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addLikeCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getRootPath().get(propertyName).as(String.class);
        fieldConditions.add(getCriteriaBuilder().like(propertyNameField, "%" + cleanLikeCondition(value) + "%"));
    }

    /**
     * Add a Field Search Condition that will search a field for values that aren't like a specified value using the following
     * SQL logic: {@code field NOT LIKE '%value%'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addNotLikeCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getRootPath().get(propertyName).as(String.class);
        fieldConditions.add(getCriteriaBuilder().notLike(propertyNameField, "%" + cleanLikeCondition(value) + "%"));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code LOWER(field) LIKE LOWER('%value%')}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addLikeIgnoresCaseCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getCriteriaBuilder().lower(getRootPath().get(propertyName).as(String.class));
        fieldConditions.add(getCriteriaBuilder().like(propertyNameField, "%" + cleanLikeCondition(value).toLowerCase() + "%"));
    }

    /**
     * Add a Field Search Condition that will search a field for values that aren't like a specified value using the following
     * SQL logic: {@code LOWER(field) NOT LIKE LOWER('%value%')}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addNotLikeIgnoresCaseCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getCriteriaBuilder().lower(getRootPath().get(propertyName).as(String.class));
        fieldConditions.add(getCriteriaBuilder().notLike(propertyNameField, "%" + cleanLikeCondition(value).toLowerCase() + "%"));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is greater than or equal to the
     * specified size.
     *
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size         The size that the collection should be greater than or equal to.
     */
    protected void addSizeGreaterThanOrEqualToCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().ge(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is greater than the specified size.
     *
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size         The size that the collection should be greater than.
     */
    protected void addSizeGreaterThanCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().gt(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is less than or equal to the
     * specified size.
     *
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size         The size that the collection should be less than or equal to.
     */
    protected void addSizeLessThanOrEqualToCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().le(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the size of a collection in an entity is less than the specified size.
     *
     * @param propertyName The name of the collection as defined in the Entity mapping class.
     * @param size         The size that the collection should be less than.
     */
    protected void addSizeLessThanCondition(final String propertyName, final Integer size) {
        final Expression<Integer> propertySizeExpression = getCriteriaBuilder().size(getRootPath().get(propertyName).as(Set.class));
        fieldConditions.add(getCriteriaBuilder().lt(propertySizeExpression, size));
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of values. eg. {@code field IN (values)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values       The List of Ids to be compared to.
     */
    protected void addIdInCollectionCondition(final String propertyName, final Collection<?> values) {
        addIdInCollectionCondition(getRootPath().get(propertyName), values);
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of values. eg. {@code field IN (values)}
     *
     * @param property The field id as defined in the Entity mapping class.
     * @param values   The List of Ids to be compared to.
     */
    protected void addIdInCollectionCondition(final Expression<?> property, final Collection<?> values) {
        if (values == null || values.isEmpty()) {
            fieldConditions.add(getCriteriaBuilder().equal(property, -1));
        } else {
            fieldConditions.add(property.in(values));
        }
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of id values that are represented as a
     * String. eg. {@code field IN (values)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values       The array of Ids to be compared to.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */

    protected void addIdInArrayCondition(final String propertyName, final String[] values) throws NumberFormatException {
        addIdInArrayCondition(getRootPath().get(propertyName).as(Integer.class), values);
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in an array of id values that are represented as a
     * String. eg. {@code field IN (values)}
     *
     * @param property The field id as defined in the Entity mapping class.
     * @param values   The array of Ids to be compared to.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdInArrayCondition(final Expression<?> property, final String[] values) throws NumberFormatException {
        final Set<Integer> idValues = new HashSet<Integer>();
        for (final String value : values) {
            idValues.add(Integer.parseInt(value.trim()));
        }
        addIdInCollectionCondition(property, idValues);
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in a comma separated list of ids. eg.
     * {@code field IN (value)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param value        The comma separated list of ids.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdInCommaSeparatedListCondition(final String propertyName, final String value) throws NumberFormatException {
        addIdInArrayCondition(propertyName, value.split(","));
    }

    /**
     * Add a Field Search Condition that will check if the id field exists in a comma separated list of ids. eg.
     * {@code field IN (value)}
     *
     * @param property The name of the field id as defined in the Entity mapping class.
     * @param value        The comma separated list of ids.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdInCommaSeparatedListCondition(final Expression<?> property, final String value) throws NumberFormatException {
        addIdInArrayCondition(property, value.split(","));
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in an array of values. eg.
     * {@code field NOT IN (values)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values       The List of Ids to be compared to.
     */
    protected void addIdNotInCollectionCondition(final String propertyName, final Collection<Integer> values) {
        if (values != null && !values.isEmpty()) {
            fieldConditions.add(getCriteriaBuilder().not(getRootPath().get(propertyName).in(values)));
        }
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in an array of id values that are represented
     * as a String. eg. {@code field NOT IN (values)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param values       The array of Ids to be compared to.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdNotInArrayCondition(final String propertyName, final String[] values) throws NumberFormatException {
        final Set<Integer> idValues = new HashSet<Integer>();
        for (final String value : values) {
            idValues.add(Integer.parseInt(value.trim()));
        }
        addIdNotInCollectionCondition(propertyName, idValues);
    }

    /**
     * Add a Field Search Condition that will check if the id field does not exist in a comma separated list of ids. eg.
     * {@code field NOT IN (value)}
     *
     * @param propertyName The name of the field id as defined in the Entity mapping class.
     * @param value        The comma separated list of ids.
     * @throws NumberFormatException Thrown if one of the Strings cannot be converted to an Integer.
     */
    protected void addIdNotInCommaSeparatedListCondition(final String propertyName, final String value) throws NumberFormatException {
        addIdNotInArrayCondition(propertyName, value.split(","));
    }

    /**
     * Add a Field Search Condition that will check if the result of a subquery exists.
     *
     * @param subquery The subquery to check if it's result exists.
     */
    protected void addExistsCondition(final Subquery<?> subquery) {
        addFieldCondition(getCriteriaBuilder().exists(subquery));
    }

    /**
     * Add a Field Search Condition that will check if the result of a subquery doesn't exist.
     *
     * @param subquery The subquery to check if it's result doesn't exist.
     */
    protected void addNotExistsCondition(final Subquery<?> subquery) {
        addFieldCondition(getCriteriaBuilder().not(getCriteriaBuilder().exists(subquery)));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field = 'value'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addEqualsCondition(final String propertyName, final String value) {
        addEqualsCondition(getRootPath().get(propertyName).as(String.class), value);
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field = 'value'}
     *
     * @param property The field property.
     * @param value    The value to search against.
     */
    protected void addEqualsCondition(final Expression<String> property, final String value) {
        fieldConditions.add(getCriteriaBuilder().equal(property, value));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field != 'value'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addNotEqualsCondition(final String propertyName, final String value) {
        fieldConditions.add(getCriteriaBuilder().notEqual(getRootPath().get(propertyName).as(String.class), value));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code lower(field) = 'value'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addEqualsIgnoreCaseCondition(final String propertyName, final String value) {
        final Expression<String> propertyNameField = getCriteriaBuilder().lower(getRootPath().get(propertyName).as(String.class));
        fieldConditions.add(getCriteriaBuilder().equal(propertyNameField, value.toString()));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field = 'value'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addEqualsCondition(final String propertyName, final Integer value) {
        addEqualsCondition(getRootPath().get(propertyName).as(Integer.class), value);
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field = 'value'}
     *
     * @param property The field property.
     * @param value    The value to search against.
     */
    protected void addEqualsCondition(final Expression<Integer> property, final Integer value) {
        fieldConditions.add(getCriteriaBuilder().equal(property, value));
    }

    /**
     * Add a Field Search Condition that will search a field for a specified value using the following SQL logic:
     * {@code field != 'value'}
     *
     * @param propertyName The name of the field as defined in the Entity mapping class.
     * @param value        The value to search against.
     */
    protected void addNotEqualsCondition(final String propertyName, final Integer value) {
        fieldConditions.add(getCriteriaBuilder().notEqual(getRootPath().get(propertyName).as(Integer.class), value));
    }

    /**
     * Cleans a Condition Value to escape any characters that could disrupt a like query. ie the percent character (%)
     *
     * @param value The value to be cleaned/escaped.
     * @return The cleaned/escaped value.
     */
    protected String cleanLikeCondition(final String value) {
        return value == null ? null : value.replaceAll("%", "\\%");
    }
}
