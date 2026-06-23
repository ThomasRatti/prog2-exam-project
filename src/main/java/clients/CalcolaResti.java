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

import classes.*;

public class CalcolaResti {

    private CalcolaResti() {}

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)){        
            StrategiaResto strategia = args[0].equals("H") ? new StrategiaRestoHigher() : new StrategiaRestoLower();
            Importo importoResto = Importo.fromString(args[1]);
            while (sc.hasNextLine()) {
                String linea = sc.nextLine();
                if (linea.isEmpty()) continue;
                Aggregato fondoCassa = Aggregato.fromString(linea);
                    
                if (fondoCassa.toImporto().compareTo(importoResto) < 0) System.out.println("value");
                else {
                    Aggregato resto = strategia.calcolaResto(fondoCassa, importoResto);
                    if (resto == null) System.out.println("change");
                    else System.out.println(resto.isEmpty() ? "<>" : resto);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("null");
         } catch (IllegalArgumentException e) {
            System.out.println("invalid");
        }
    }
}

