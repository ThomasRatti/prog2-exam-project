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

import classes.Binario;
import classes.exceptions.CapacityExceededException;
import classes.exceptions.ItemMismatchException;
import classes.exceptions.SizeExceededException;

public class CaricaBinario {
   
    private CaricaBinario() {}

    public static void main(String[] args) {        
        Binario binario = Binario.fromString(args[0] + "|" + args[1]);
        System.out.println(binario);

        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                ParseUtils.QuantitaProdotto qp = ParseUtils.parseQuantitaProdotto(line);
                try {
                    binario.carica(qp.prodotto(), qp.quantita());
                    System.out.println(binario);
                } catch (SizeExceededException e) {
                    System.out.println("size");
                } catch (CapacityExceededException e) {
                    System.out.println("capacity");
                } catch (ItemMismatchException e) {
                    System.out.println("item");
                }
            }
        }
    }
}