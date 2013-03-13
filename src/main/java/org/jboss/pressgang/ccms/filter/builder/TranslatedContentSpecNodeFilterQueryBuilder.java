package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TranslatedContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<TranslatedCSNode> {
    private static Logger log = LoggerFactory.getLogger(TranslatedContentSpecNodeFilterQueryBuilder.class);

    public TranslatedContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(TranslatedCSNode.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_TRANSLATED_NODE_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeparatedListCondition("translatedCSNodeId", fieldValue);
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
