/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.persistit.exception;

/**
 * Thrown when a method must back off and retry.
 * 
 * @version 2.1
 */
public class WWRetryException extends PersistitException {

    private static final long serialVersionUID = -8001684710494122369L;

    final long _versionHandle;

    public WWRetryException(final long handle) {
        _versionHandle = handle;
    }

    public long getVersionHandle() {
        return _versionHandle;
    }
}