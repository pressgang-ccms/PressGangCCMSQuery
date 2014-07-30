/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldIntegerListData;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class ContentSpecNodeFieldFilter extends BaseFieldFilter {
    /**
     * A map of the base filter field names that can not have multiple
     * mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>() {
        private static final long serialVersionUID = 4454656533723964663L;

        {
            put(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_ID_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_ID_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_REVISION_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_REVISION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_ID_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_ID_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_IDS_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_IDS_FILTER_VAR,
                    CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);
        }
    });

    private FilterFieldIntegerListData csNodeIds;
    private FilterFieldStringData csNodeTitle;
    private FilterFieldIntegerListData csNodeType;
    private FilterFieldIntegerData csNodeEntityId;
    private FilterFieldIntegerData csNodeEntityRev;
    private FilterFieldIntegerData csInfoNodeTopicId;
    private FilterFieldIntegerData csInfoNodeTopicRev;
    private FilterFieldIntegerListData csNodeEntityIds;
    private FilterFieldIntegerListData csInfoNodeTopicIds;
    private FilterFieldIntegerListData contentSpecIds;

    public ContentSpecNodeFieldFilter() {
        resetAllValues();
    }

    @Override
    protected void resetAllValues() {
        super.resetAllValues();

        csNodeIds = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR_DESC);
        csNodeTitle = new FilterFieldStringData(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR_DESC);
        csNodeType = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_TYPE_FILTER_VAR_DESC);
        csNodeEntityId = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_ID_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_ID_FILTER_VAR_DESC);
        csNodeEntityRev = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_REVISION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_REVISION_FILTER_VAR_DESC);
        csInfoNodeTopicId = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_ID_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_ID_FILTER_VAR_DESC);
        csInfoNodeTopicRev = new FilterFieldIntegerData(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_REVISION_FILTER_VAR_DESC);
        csNodeEntityIds = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_ENTITY_IDS_FILTER_VAR_DESC);
        csInfoNodeTopicIds = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_NODE_INFO_TOPIC_IDS_FILTER_VAR_DESC);
        contentSpecIds = new FilterFieldIntegerListData(CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR,
                CommonFilterConstants.CONTENT_SPEC_IDS_FILTER_VAR_DESC);

        addFilterVar(csNodeIds);
        addFilterVar(csNodeTitle);
        addFilterVar(csNodeType);
        addFilterVar(csNodeEntityId);
        addFilterVar(csNodeEntityRev);
        addFilterVar(csInfoNodeTopicId);
        addFilterVar(csInfoNodeTopicRev);
        addFilterVar(csNodeEntityIds);
        addFilterVar(csInfoNodeTopicIds);
        addFilterVar(contentSpecIds);
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
