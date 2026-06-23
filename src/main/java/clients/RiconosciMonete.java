/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/

package clients;

import java.util.Scanner;
import java.util.Set;

import classes.Importo;
import classes.Moneta;

public class RiconosciMonete {
    
    private RiconosciMonete() {}

    public static void main(String[] args) {
        try(final Scanner sc = new Scanner(System.in)) {
            final Set<Importo> moneteValide = Moneta.getValues();

            while (sc.hasNextLine()) {
                try {
                    final Importo importo = Importo.fromString(sc.nextLine().trim());
                    if (moneteValide.contains(importo)) {
                        System.out.println(importo);
                    } else {
                        System.out.println("invalid");
                    }
                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.println("invalid");
                }
            }
        }
    }
}

