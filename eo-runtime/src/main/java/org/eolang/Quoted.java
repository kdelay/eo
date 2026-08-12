/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2026 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/**
 * A text rendered as an EO string literal.
 *
 * <p>The text lands between double quotes, with every glyph spelled by
 * {@link Escaped}. Without the escaping, a quote inside the text closes
 * the literal early and a line feed splits it across lines, so the
 * printed φ-term denotes a string other than the one it came from.</p>
 *
 * @since 0.73.3
 */
final class Quoted implements Supplier<String> {

    /**
     * The text.
     */
    private final String text;

    /**
     * Ctor.
     * @param data The UTF-8 bytes of the text
     */
    Quoted(final byte[] data) {
        this(new String(data, StandardCharsets.UTF_8));
    }

    /**
     * Ctor.
     * @param txt The text
     */
    Quoted(final String txt) {
        this.text = txt;
    }

    @Override
    public String get() {
        final StringBuilder out = new StringBuilder("\"");
        for (final char glyph : this.text.toCharArray()) {
            out.append(new Escaped(glyph).get());
        }
        return out.append('"').toString();
    }
}
