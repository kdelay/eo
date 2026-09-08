/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2026 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.maven;

import com.github.lombrozo.xnav.Xnav;
import com.jcabi.xml.XMLDocument;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link Tallied}.
 * @since 0.76.0
 */
final class TalliedTest {

    @Test
    void addsUpWhatItsPassRewroteEverywhere() throws IOException {
        final Tallied tallied = new Tallied("folded", doc -> 5);
        tallied.rewrite(TalliedTest.empty());
        tallied.rewrite(TalliedTest.empty());
        MatcherAssert.assertThat(
            "the tally must name the pass and say how much it rewrote, but it doesnt",
            tallied.toString(),
            Matchers.equalTo("10 folded")
        );
    }

    @Test
    void givesBackWhatItsPassRewrote() throws IOException {
        MatcherAssert.assertThat(
            "counting must not change what the pass says it rewrote, but it did",
            new Tallied("lowered", doc -> 3).rewrite(TalliedTest.empty()),
            Matchers.equalTo(3)
        );
    }

    private static Xnav empty() {
        return new Xnav(new XMLDocument("<object/>").inner());
    }
}
