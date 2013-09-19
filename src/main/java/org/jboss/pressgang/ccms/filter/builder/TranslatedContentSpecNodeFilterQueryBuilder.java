package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import java.util.List;

import org.jboss.pressgang.ccms.filter.TranslatedContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class TranslatedContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<TranslatedCSNode> {

    public TranslatedContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(TranslatedCSNode.class, new TranslatedContentSpecNodeFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_TRANSLATED_NODE_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("translatedCSNodeId", (List<Integer>) field.getData());
        } else {
            super.processField(field);
        }
    }
}
