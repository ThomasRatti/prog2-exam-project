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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import classes.Prodotto;

public class OrdinaProdotti {
    
    private OrdinaProdotti() {}

    public static void main(String[] args) {
        List<Prodotto> prodotti = new ArrayList<>();
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    prodotti.add(Prodotto.fromString(line));
                }
            }
        }
        Collections.sort(prodotti);
        for (Prodotto p : prodotti) {
            System.out.println(p);
        }
    }
}
