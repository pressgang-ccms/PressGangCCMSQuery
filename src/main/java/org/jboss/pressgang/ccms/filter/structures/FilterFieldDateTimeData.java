/*
  Copyright 2011-2014 Red Hat

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

package org.jboss.pressgang.ccms.filter.structures;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldDateTimeData extends FilterFieldDataBase<DateTime> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldDateTimeData.class);

    public FilterFieldDateTimeData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public DateTime getData() {
        return data == null ? null : data;
    }

    public Date getDateData() {
        return data == null ? null : data.toDate();
    }

    @Override
    public void setData(final DateTime data) {
        this.data = data;
    }

    public void setData(final Date data) {
        this.data = data == null ? null : new DateTime(data);
    }

    @Override
    public void setData(final String value) {
        try {
            if (isNullOrEmpty(value)) {
                data = null;
            } else {
                data = new DateTime(ISODateTimeFormat.dateTime().parseDateTime(value));
            }
        } catch (final Exception ex) {
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", name, value);
        }
    }

    @Override
    public String getDataString() {
        return data == null ? null : ISODateTimeFormat.dateTime().print(data);
    }
}
