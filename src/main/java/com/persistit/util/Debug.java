/**
 * Copyright © 2005-2012 Akiban Technologies, Inc.  All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 (only) of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This program may also be available under different license terms. For more
 * information, see www.akiban.com or contact licensing@akiban.com.
 */

package com.persistit.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Internal helper methods used to help verify code correctness.
 * 
 * @author peter
 * 
 */
public class Debug {

    public final static boolean ENABLED = false;

    public final static boolean VERIFY_PAGES = false;

    public final static Random RANDOM = new Random(123);

    private final static AtomicLong PAUSES = new AtomicLong();

    public interface Dbg {
        void t(boolean b);
    }

    private static class Null implements Dbg {
        public void t(final boolean b) {
        }
    }

    private static class Assert implements Dbg {
        private final String _name;

        private Assert(final String name) {
            _name = name;
        }

        public void t(final boolean b) {
            if (!b) {
                logDebugMessage(_name);
                setSuspended(true);
                //
                // Put a breakpoint on the next statement.
                //
                setSuspended(false); // <-- BREAKPOINT HERE
            }
        }
    }

    public static Dbg $assert0 = ENABLED ? new Assert("assert0") : new Null();
    public static Dbg $assert1 = new Assert("assert1");

    private static int _suspendedCount;

    private static ArrayList<Thread> _brokenThreads = new ArrayList<Thread>();

    private static long _startTime;

    public static void setStartTime(final long startTime) {
        _startTime = startTime;
    }

    public static long elapsedTime() {
        return now() - _startTime;
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    private static void logDebugMessage(String msg) {
        RuntimeException exception = new RuntimeException();
        exception.fillInStackTrace();
        String s = asString(exception).replace('\r', ' ');
        StringTokenizer st = new StringTokenizer(s, "\n");
        StringBuilder sb = new StringBuilder(msg);
        sb.append(Util.NEW_LINE);
        while (st.hasMoreTokens()) {
            sb.append("    ");
            sb.append(st.nextToken());
            sb.append(Util.NEW_LINE);
        }
        System.err.println("Debug " + sb.toString());
    }

    /**
     * Set the suspend flag so that callers to the suspend method either do or
     * do not suspend.
     * 
     * @param b
     */
    synchronized static void setSuspended(boolean b) {
        if (b) {
            _suspendedCount++;
            _brokenThreads.add(Thread.currentThread());
        } else {
            _suspendedCount--;
            _brokenThreads.remove(Thread.currentThread());
            if (_suspendedCount == 0) {
                $assert1.t(_brokenThreads.size() == _suspendedCount);
            }
        }
    }

    /**
     * @return The state of the suspend flag.
     */
    synchronized static boolean isSuspended() {
        return _suspendedCount > 0;
    }

    /**
     * Assert this method invocation anywhere you want to suspend a thread. For
     * example, add this to cause execution to be suspended:
     * 
     * assert(Debug.suspend());
     * 
     * This method always returns true so there will never be an AssertionError
     * thrown.
     * 
     * @return <i>true</i>
     */
    public static boolean suspend() {
        if (ENABLED) {
            // Never suspend the AWT thread when. The AWT thread is now
            // a daemon thread when running the diagnostic GUI utility.
            //
            long time = -1;
            while (isSuspended() && !Thread.currentThread().isDaemon()) {
                if (time < 0)
                    time = elapsedTime();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
        return true;
    }

    public static String asString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Debugging aid: code can invoke this method to introduce a pause.
     * 
     * @param probability
     *            Probability of pausing: 0 - 1.0f
     * @param millis
     *            time interval in milliseconds
     */
    public static void debugPause(final float probability, final long millis) {
        if (RANDOM.nextInt(1000000000) < (int) (1000000000f * probability)) {
            try {
                Thread.sleep(millis);
                PAUSES.incrementAndGet();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}