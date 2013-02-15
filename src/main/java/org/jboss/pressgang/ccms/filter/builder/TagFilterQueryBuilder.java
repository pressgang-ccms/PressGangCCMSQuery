package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToPropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagFilterQueryBuilder extends BaseFilterQueryBuilder<Tag> {
    private static final Logger log = LoggerFactory.getLogger(TagFilterQueryBuilder.class);

    public TagFilterQueryBuilder(final EntityManager entityManager) {
        super(Tag.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.TAG_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeparatedListCondition("tagId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.TAG_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.TAG_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("tagDescription", fieldValue);
        } else if (fieldName.startsWith(CommonFilterConstants.PROPERTY_TAG)) {
            try {
                final NamedPattern pattern = NamedPattern.compile(CommonConstants.PROPERTY_TAG_SEARCH_RE);
                final NamedMatcher matcher = pattern.matcher(fieldName);

                while (matcher.find()) {
                    final String propertyTagIdString = matcher.group("PropertyTagID");

                    if (propertyTagIdString != null && fieldValue != null) {
                        final Integer propertyTagIdInt = Integer.parseInt(propertyTagIdString);
                        final Subquery<TagToPropertyTag> subquery = getPropertyTagSubquery(propertyTagIdInt, fieldValue);
                        addFieldCondition(getCriteriaBuilder().exists(subquery));
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
     * Create a Subquery to check if a tag has a property tag with a specific value.
     *
     * @param propertyTagId    The ID of the property tag to be checked.
     * @param propertyTagValue The Value that the property tag should have.
     * @return A subquery that can be used in an exists statement to see if a topic has a property tag with the specified value.
     */
    private Subquery<TagToPropertyTag> getPropertyTagSubquery(final Integer propertyTagId, final String propertyTagValue) {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<TagToPropertyTag> subQuery = getCriteriaQuery().subquery(TagToPropertyTag.class);
        final Root<TagToPropertyTag> root = subQuery.from(TagToPropertyTag.class);
        subQuery.select(root);

        // Create the Condition for the subquery
        final Predicate tagIdMatch = criteriaBuilder.equal(getRootPath(), root.get("tag"));
        final Predicate propertyTagIdMatch = criteriaBuilder.equal(root.get("propertyTag").get("propertyTagId"), propertyTagId);
        final Predicate propertyTagValueMatch = criteriaBuilder.equal(root.get("value"), propertyTagValue);
        subQuery.where(criteriaBuilder.and(tagIdMatch, propertyTagIdMatch, propertyTagValueMatch));

        return subQuery;
    }
}
