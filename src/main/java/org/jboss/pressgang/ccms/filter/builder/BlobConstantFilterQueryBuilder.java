package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;
import java.util.List;

import org.jboss.pressgang.ccms.filter.BlobConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class BlobConstantFilterQueryBuilder extends BaseFilterQueryBuilder<BlobConstants> {

    public BlobConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(BlobConstants.class, new BlobConstantFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("blobConstantsId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantName");
        } else {
            super.processField(field);
        }
    }

}
