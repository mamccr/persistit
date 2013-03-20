/**
 * Copyright © 2011-2012 Akiban Technologies, Inc.  All rights reserved.
 * 
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * This program may also be available under different license terms.
 * For more information, see www.akiban.com or contact licensing@akiban.com.
 * 
 * Contributors:
 * Akiban Technologies, Inc.
 */

package com.persistit.stress.unit;

import com.persistit.stress.unit.StressRecovery.TransactionType;

public class StressRecoveryTxn1 implements TransactionType {

    @Override
    public long performTransaction(final long ticketId) throws Exception {
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    public void verifyTransaction(final long ticketId) throws Exception {
        // TODO Auto-generated method stub

    }

}