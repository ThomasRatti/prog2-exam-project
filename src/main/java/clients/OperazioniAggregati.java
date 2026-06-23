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

import classes.Aggregato;

public class OperazioniAggregati {

    private OperazioniAggregati() {}

    public static void main(String[] args) {
        Aggregato current = Aggregato.fromString("");
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                ParseUtils.Operazione op = ParseUtils.parseOperazione(sc.nextLine());
                Aggregato agg = Aggregato.fromString(op.operando());
                try {
                    current = (op.operatore() == '+' ? current.aggiungi(agg) : current.rimuovi(agg));
                    System.out.println(current);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage().equals("coins") ? "coins" : "value");
                }
            }
        } 
    }
}
