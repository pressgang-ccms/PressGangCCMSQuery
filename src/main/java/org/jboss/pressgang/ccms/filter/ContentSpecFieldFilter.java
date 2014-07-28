/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilterWithProperties;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldBooleanData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDateTimeData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecFieldFilter extends BaseFieldFilterWithProperties {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.CONTENT_SPEC_TYPE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_TYPE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_SUBTITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_SUBTITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_EDITION_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_EDITION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_BOOK_VERSION_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_BOOK_VERSION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_PUBSNUMBER_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_PUBSNUMBER_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_YEAR_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_YEAR_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_FORMAT_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_FORMAT_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NOT_FORMAT_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NOT_FORMAT_FILTER_VAR_DESC);
            put(CommonFilterConstants.HAS_ERRORS_FILTER_VAR, CommonFilterConstants.HAS_ERRORS_FILTER_VAR_DESC);
            put(CommonFilterConstants.EDITED_IN_LAST_DAYS, CommonFilterConstants.EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS, CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS_DESC);
            put(CommonFilterConstants.EDITED_IN_LAST_MINUTES, CommonFilterConstants.EDITED_IN_LAST_MINUTES_DESC);
            put(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES, CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES_DESC);
            put(CommonFilterConstants.STARTEDITDATE_FILTER_VAR, CommonFilterConstants.STARTEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.ENDEDITDATE_FILTER_VAR, CommonFilterConstants.ENDEDITDATE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CREATED_BY_VAR, CommonFilterConstants.CREATED_BY_VAR_DESC);
            put(CommonFilterConstants.NOT_CREATED_BY_VAR, CommonFilterConstants.NOT_CREATED_BY_VAR_DESC);
            put(CommonFilterConstants.EDITED_BY_VAR, CommonFilterConstants.EDITED_BY_VAR_DESC);
            put(CommonFilterConstants.NOT_EDITED_BY_VAR, CommonFilterConstants.NOT_EDITED_BY_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData contentSpecIds;
    private FilterFieldIntegerData contentSpecType;
    private FilterFieldStringData contentSpecTitle;
    private FilterFieldStringData contentSpecSubtitle;
    private FilterFieldStringData contentSpecProduct;
    private FilterFieldStringData contentSpecVersion;
    private FilterFieldStringData contentSpecEdition;
    private FilterFieldStringData contentSpecBookVersion;
    private FilterFieldIntegerData contentSpecPubsnumber;
    private FilterFieldStringData contentSpecAbstract;
    private FilterFieldStringData contentSpecBrand;
    private FilterFieldStringData contentSpecCopyrightHolder;
    private FilterFieldStringData contentSpecCopyrightYear;
    private FilterFieldStringData contentSpecPublicanCfg;
    private FilterFieldIntegerData contentSpecFormat;
    private FilterFieldIntegerData contentSpecNotFormat;
    private FilterFieldIntegerData editedInLastDays;
    private FilterFieldIntegerData notEditedInLastDays;
    private FilterFieldIntegerData editedInLastMins;
    private FilterFieldIntegerData notEditedInLastMins;
    private FilterFieldDateTimeData startEditDate;
    private FilterFieldDateTimeData endEditDate;
    private FilterFieldBooleanData hasErrors;
    private FilterFieldStringData createdBy;
    private FilterFieldStringData notCreatedBy;
    private FilterFieldStringData editedBy;
    private FilterFieldStringData notEditedBy;

    public ContentSpecFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        contentSpecIds = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);
        contentSpecType = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_TYPE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR_DESC);
        contentSpecTitle = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_TITLE_FILTER_VAR_DESC);
        contentSpecSubtitle = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_SUBTITLE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_SUBTITLE_FILTER_VAR_DESC);
        contentSpecProduct = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_PRODUCT_FILTER_VAR_DESC);
        contentSpecVersion = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_VERSION_FILTER_VAR_DESC);
        contentSpecEdition = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_EDITION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_EDITION_FILTER_VAR_DESC);
        contentSpecBookVersion = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_BOOK_VERSION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_BOOK_VERSION_FILTER_VAR_DESC);
        contentSpecPubsnumber = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_PUBSNUMBER_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_PUBSNUMBER_FILTER_VAR_DESC);
        contentSpecAbstract = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_ABSTRACT_FILTER_VAR_DESC);
        contentSpecBrand = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_BRAND_FILTER_VAR_DESC);
        contentSpecCopyrightHolder = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_HOLDER_FILTER_VAR_DESC);
        contentSpecCopyrightYear = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_YEAR_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_COPYRIGHT_YEAR_FILTER_VAR_DESC);
        contentSpecPublicanCfg = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_PUBLICAN_CFG_FILTER_VAR_DESC);
        contentSpecFormat = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_FORMAT_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_FORMAT_FILTER_VAR_DESC);
        contentSpecNotFormat = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NOT_FORMAT_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NOT_FORMAT_FILTER_VAR_DESC);
        
        /* Edited in last days */
        editedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.EDITED_IN_LAST_DAYS,
                CommonFilterConstants.EDITED_IN_LAST_DAYS_DESC);
        notEditedInLastDays = new FilterFieldIntegerData(CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS,
                CommonFilterConstants.NOT_EDITED_IN_LAST_DAYS_DESC);

        /* Edited in last minutes */
        editedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.EDITED_IN_LAST_MINUTES_DESC);
        notEditedInLastMins = new FilterFieldIntegerData(CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES,
                CommonFilterConstants.NOT_EDITED_IN_LAST_MINUTES_DESC);

        startEditDate = new FilterFieldDateTimeData(CommonFilterConstants.STARTEDITDATE_FILTER_VAR,
                CommonFilterConstants.STARTEDITDATE_FILTER_VAR_DESC);
        endEditDate = new FilterFieldDateTimeData(CommonFilterConstants.ENDEDITDATE_FILTER_VAR,
                CommonFilterConstants.ENDEDITDATE_FILTER_VAR_DESC);

        hasErrors = new FilterFieldBooleanData(CommonFilterConstants.HAS_ERRORS_FILTER_VAR,
                CommonFilterConstants.HAS_ERRORS_FILTER_VAR_DESC);
        createdBy = new FilterFieldStringData(CommonFilterConstants.CREATED_BY_VAR, CommonFilterConstants.CREATED_BY_VAR_DESC);
        notCreatedBy = new FilterFieldStringData(CommonFilterConstants.NOT_CREATED_BY_VAR, CommonFilterConstants.NOT_CREATED_BY_VAR_DESC);
        editedBy = new FilterFieldStringData(CommonFilterConstants.EDITED_BY_VAR, CommonFilterConstants.EDITED_BY_VAR_DESC);
        notEditedBy = new FilterFieldStringData(CommonFilterConstants.NOT_EDITED_BY_VAR, CommonFilterConstants.NOT_EDITED_BY_VAR_DESC);

        setupSingleFilterVars();
    }

    private void setupSingleFilterVars() {
        addFilterVar(contentSpecIds);
        addFilterVar(contentSpecType);
        addFilterVar(contentSpecTitle);
        addFilterVar(contentSpecSubtitle);
        addFilterVar(contentSpecProduct);
        addFilterVar(contentSpecVersion);
        addFilterVar(contentSpecEdition);
        addFilterVar(contentSpecBookVersion);
        addFilterVar(contentSpecPubsnumber);
        addFilterVar(contentSpecAbstract);
        addFilterVar(contentSpecBrand);
        addFilterVar(contentSpecCopyrightHolder);
        addFilterVar(contentSpecCopyrightYear);
        addFilterVar(contentSpecPublicanCfg);
        addFilterVar(contentSpecFormat);
        addFilterVar(contentSpecNotFormat);
        addFilterVar(hasErrors);
        addFilterVar(editedInLastDays);
        addFilterVar(notEditedInLastDays);
        addFilterVar(editedInLastMins);
        addFilterVar(notEditedInLastMins);
        addFilterVar(startEditDate);
        addFilterVar(endEditDate);
        addFilterVar(createdBy);
        addFilterVar(notCreatedBy);
        addFilterVar(editedBy);
        addFilterVar(notEditedBy);
    }

    @Override
    public Map<String, String> getFieldNames() {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames() {
        final Map<String, String> retValue = super.getBaseFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }
}
