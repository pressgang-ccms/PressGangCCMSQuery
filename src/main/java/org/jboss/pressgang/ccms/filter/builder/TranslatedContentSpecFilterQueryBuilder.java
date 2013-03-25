package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslatedContentSpecFilterQueryBuilder extends BaseFilterQueryBuilder<TranslatedContentSpec> {
    private static Logger log = LoggerFactory.getLogger(TranslatedContentSpecFilterQueryBuilder.class);

    public TranslatedContentSpecFilterQueryBuilder(final EntityManager entityManager) {
        super(TranslatedContentSpec.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.TRANSLATED_CONTENT_SPEC_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("translatedContentSpecId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0) {
                final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
                final List<Predicate> conditions = new ArrayList<Predicate>();

                final String[] zanataIds = fieldValue.split(",");
                for (final String zanataId : zanataIds) {
                    try {
                        conditions.add(getZanataIdCondition(zanataId));
                    } catch (NumberFormatException ex) {
                        log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                    }
                }

                // Only add the query if we found valid zanata ids
                if (conditions.size() > 1) {
                    final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                    addFieldCondition(criteriaBuilder.or(predicates));
                } else if (conditions.size() == 1) {
                    addFieldCondition(conditions.get(0));
                }
            }
        } else if (fieldName.equals(CommonFilterConstants.ZANATA_IDS_NOT_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0) {
                final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
                final List<Predicate> conditions = new ArrayList<Predicate>();

                final String[] zanataIds = fieldValue.split(",");
                for (final String zanataId : zanataIds) {
                    try {
                        conditions.add(getZanataIdCondition(zanataId));
                    } catch (NumberFormatException ex) {
                        log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
                    }
                }

                // Only add the query if we found valid zanata ids
                if (conditions.size() > 1) {
                    final Predicate[] predicates = conditions.toArray(new Predicate[conditions.size()]);
                    addFieldCondition(criteriaBuilder.not(criteriaBuilder.or(predicates)));
                } else if (conditions.size() == 1) {
                    addFieldCondition(conditions.get(0));
                }
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

    /**
     * Gets a JPA Predicate condition, to find TranslatedContentSpecs that match a Zanata Document ID in the form of
     * {@code "CS<ID>-<REVISION>"}.
     *
     * @param zanataId The Zanata Document ID.
     * @return A Predicate object that contains the SQL WHERE logic to find TranslatedContentSpecs that match the Zanata ID.
     * @throws NumberFormatException Thrown if the ZanataID, when broken down can't be transformed into an Integer.
     */
    protected Predicate getZanataIdCondition(final String zanataId) throws NumberFormatException {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        String[] zanataVars = zanataId.split("-");

        final Integer contentSpecId = Integer.parseInt(zanataVars[0].replaceAll("^CS", ""));
        final Integer contentSpecRevision = Integer.parseInt(zanataVars[1]);

        final Predicate contentSpecIdCondition = criteriaBuilder.equal(getRootPath().get("contentSpecId"), contentSpecId);
        final Predicate contentSpecRevisionCondition = criteriaBuilder.equal(getRootPath().get("contentSpecRevision"),
                contentSpecRevision);

        return criteriaBuilder.and(contentSpecIdCondition, contentSpecRevisionCondition);
    }
}
