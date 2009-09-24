/*
 *  GreeklishConverter.java
 *
 *  Copyright (C) May 1, 2009 Nikosk <nikosk@dsigned.gr>
 *
 *  This module is free software: you can redistribute it and/or modify it under
 *  the terms of the GNU Lesser General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *  any later version. See http://www.gnu.org/licenses/lgpl.html.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package gr.dsigned.jmvc.libraries;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class GreeklishConverter {

    public static String toGreeklish(String target) {
        if (target != null) {
            target = target.trim();
            target = target.replace("ι", "i");
            target = target.replace("ϊ", "i");
            target = target.replace("Ι", "I");
            target = target.replace("η", "h");
            target = target.replace("Η", "H");
            target = target.replace("υ", "y");
            target = target.replace("Υ", "Y");
            target = target.replace("ε", "e");
            target = target.replace("Ε", "E");
            target = target.replace("α", "a");
            target = target.replace("Α", "A");
            target = target.replace("ο", "o");
            target = target.replace("Ο", "O");
            target = target.replace("ω", "w");
            target = target.replace("Ω", "W");
            target = target.replace("ί", "i");
            target = target.replace("Ί", "I");
            target = target.replace("ή", "h");
            target = target.replace("Ή", "H");
            target = target.replace("ύ", "u");
            target = target.replace("Ύ", "Y");
            target = target.replace("έ", "e");
            target = target.replace("Έ", "E");
            target = target.replace("ά", "a");
            target = target.replace("Ά", "A");
            target = target.replace("ό", "o");
            target = target.replace("Ό", "O");
            target = target.replace("ώ", "w");
            target = target.replace("Ώ", "W");
            target = target.replace("β", "v");
            target = target.replace("Β", "V");
            target = target.replace("γ", "g");
            target = target.replace("Γ", "G");
            target = target.replace("δ", "d");
            target = target.replace("Δ", "D");
            target = target.replace("ζ", "z");
            target = target.replace("Ζ", "Z");
            target = target.replace("θ", "th");
            target = target.replace("Θ", "Th");
            target = target.replace("κ", "k");
            target = target.replace("Κ", "K");
            target = target.replace("λ", "l");
            target = target.replace("Λ", "L");
            target = target.replace("μ", "m");
            target = target.replace("Μ", "M");
            target = target.replace("ν", "n");
            target = target.replace("Ν", "N");
            target = target.replace("ξ", "ks");
            target = target.replace("Ξ", "Ks");
            target = target.replace("π", "p");
            target = target.replace("Π", "P");
            target = target.replace("ρ", "r");
            target = target.replace("Ρ", "R");
            target = target.replace("σ", "s");
            target = target.replace("Σ", "S");
            target = target.replace("ς", "s");
            target = target.replace("τ", "t");
            target = target.replace("Τ", "T");
            target = target.replace("φ", "f");
            target = target.replace("Φ", "F");
            target = target.replace("χ", "x");
            target = target.replace("Χ", "X");
            target = target.replace("ψ", "ps");
            target = target.replace("Ψ", "Ps");
            target = target.replace(" ", "_");
            target = target.replace("\"", "_");
            target = target.replace("'", "_");
            target = target.replace("!", "_");
            target = target.replace("@", "_");
            target = target.replace("#", "_");
            target = target.replace("%", "_");
            target = target.replace("$", "_");
            target = target.replace("^", "_");
            target = target.replace("&", "_");
            target = target.replace("*", "_");
            target = target.replace("(", "_");
            target = target.replace(")", "_");
            target = target.replace("[", "_");
            target = target.replace("]", "_");
            target = target.replace("{", "_");
            target = target.replace("}", "_");
            target = target.replace("|", "_");
            target = target.replace("\\", "_");
            target = target.replace("/", "_");
            target = target.replace("?", "_");
            target = target.replace(">", "_");
            target = target.replace("<", "_");
            target = target.replace("'", "_");
            target = target.replace(":", "_");

        }
        return target;
    }
}

