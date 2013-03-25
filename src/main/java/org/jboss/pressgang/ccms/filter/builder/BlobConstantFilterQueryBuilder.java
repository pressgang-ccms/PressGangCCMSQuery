package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class BlobConstantFilterQueryBuilder extends BaseFilterQueryBuilder<BlobConstants> {

    public BlobConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(BlobConstants.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("blobConstantsId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("constantName", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

}
