package org.jboss.pressgang.ccms.filter.base;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Subquery;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFilterQueryBuilderWithProperties<T> extends BaseFilterQueryBuilder<T> {
    private static final Logger log = LoggerFactory.getLogger(BaseFilterQueryBuilderWithProperties.class);

    protected BaseFilterQueryBuilderWithProperties(Class<T> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue){
        if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG)) {
            try {
                final NamedPattern pattern = NamedPattern.compile(CommonConstants.PROPERTY_TAG_SEARCH_RE);
                final NamedMatcher matcher = pattern.matcher(fieldName);

                while (matcher.find()) {
                    final String propertyTagIdString = matcher.group("PropertyTagID");

                    if (propertyTagIdString != null && fieldValue != null) {
                        final Integer propertyTagIdInt = Integer.parseInt(propertyTagIdString);
                        addExistsCondition(getPropertyTagSubquery(propertyTagIdInt, fieldValue));
                    }

                    /* should only match once */
                    break;
                }

            } catch (final NumberFormatException ex) {
                /*
                 * could not parse integer, so fail. this shouldn't happen though, as the string is matched by a regex that will
                 * only allow numbers
                 */
                log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

    /**
     * Create a Subquery to check if a entity has a property tag with a specific value.
     *
     * @param propertyTagId    The ID of the property tag to be checked.
     * @param propertyTagValue The Value that the property tag should have.
     * @return A subquery that can be used in an exists statement to see if a entity has a property tag with the specified value.
     */
    protected abstract Subquery<?> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue);
}
