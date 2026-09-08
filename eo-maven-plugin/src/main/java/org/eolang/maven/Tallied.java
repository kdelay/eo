/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2026 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.maven;

import com.github.lombrozo.xnav.Xnav;
import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;
import org.eolang.lowering.Rewrite;

/**
 * One lowering pass, keeping a tally of what it rewrote.
 *
 * <p>The pass is asked once per XMIR and the files are walked in
 * parallel, so the tally is a {@link LongAdder}. The
 * {@link #toString()} is what the pass did across the whole build,
 * ready for the log.</p>
 *
 * @since 0.76.0
 */
final class Tallied implements Rewrite {

    /**
     * How many fragments the pass rewrote so far.
     */
    private final LongAdder count;

    /**
     * The verb that names what the pass does.
     */
    private final String verb;

    /**
     * The pass itself.
     */
    private final Rewrite origin;

    /**
     * Ctor.
     * @param name The verb that names what the pass does
     * @param pass The pass itself
     */
    Tallied(final String name, final Rewrite pass) {
        this.count = new LongAdder();
        this.verb = name;
        this.origin = pass;
    }

    @Override
    public int rewrite(final Xnav doc) throws IOException {
        final int done = this.origin.rewrite(doc);
        this.count.add(done);
        return done;
    }

    @Override
    public String toString() {
        return String.format("%d %s", this.count.sum(), this.verb);
    }
}
