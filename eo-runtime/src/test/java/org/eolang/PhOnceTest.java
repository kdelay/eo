/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2026 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link PhOnce}.
 * @since 0.60
 */
final class PhOnceTest {

    @Test
    void delegatesTermToWrappedObjectByDefault() {
        MatcherAssert.assertThat(
            "PhOnce without explicit term must delegate φ-term to the wrapped object, but it didnt",
            new PhOnce(() -> new PhDefault(new byte[] {(byte) 0x01})).φTerm(),
            Matchers.equalTo("[D> 01]")
        );
    }

    @Test
    void doesNotEvaluateWrappedObjectForTerm() {
        MatcherAssert.assertThat(
            "PhOnce with explicit term must render it without evaluating the wrapped object, but it didnt",
            new PhOnce(
                () -> {
                    throw new IllegalStateException("must not be evaluated");
                },
                () -> "x.foo"
            ).φTerm(),
            Matchers.equalTo("x.foo")
        );
    }

    @Test
    void keepsWrapperAfterNormalization() {
        MatcherAssert.assertThat(
            "normalized() must stay wrapped in PhOnce, so the object is fetched once, but it didnt",
            new PhOnce(() -> new PhDefault(new byte[] {(byte) 0x01})).normalized(),
            Matchers.instanceOf(PhOnce.class)
        );
    }

    @Test
    void keepsTermAfterNormalization() {
        MatcherAssert.assertThat(
            "normalized() must carry the φ-term of the wrapper over, but it didnt",
            new PhOnce(
                () -> new PhDefault(new byte[] {(byte) 0x01}),
                () -> "x.foo"
            ).normalized().φTerm(),
            Matchers.equalTo("x.foo")
        );
    }

    @Test
    void revealsBottomWithoutWrappingIt() {
        MatcherAssert.assertThat(
            "a wrapped bottom must surface as a PhTerminator, not as a PhOnce, but it didnt",
            new PhOnce(() -> new PhDispatch(new PhDefault(), "missing")).normalized(),
            Matchers.instanceOf(PhTerminator.class)
        );
    }

    @Test
    void staysRecoverableWhenWrappingABottom() {
        final Phi recovered = new EOrecovered();
        recovered.put("value", new PhOnce(() -> new PhDispatch(new PhDefault(), "missing")));
        recovered.put("alternative", new Data.ToPhi(42L));
        MatcherAssert.assertThat(
            "a bottom behind PhOnce must still be intercepted by recovered, but it wasnt",
            new Dataized(recovered).asNumber(),
            Matchers.equalTo(42.0)
        );
    }
}
