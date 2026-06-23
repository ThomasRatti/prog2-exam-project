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

import java.util.List;
import java.util.Scanner;

import classes.Aggregato;
import classes.Binario;
import classes.Distributore;
import classes.StrategiaResto;
import classes.StrategiaRestoHigher;
import classes.StrategiaRestoLower;

public class UsaDistributore {
    
    private UsaDistributore() {}

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)){    
            final List<Binario> binari = ParseUtils.parseBinari(sc.nextLine());
            final Aggregato fondoCassa = Aggregato.fromString(sc.nextLine());
            final StrategiaResto strategia = sc.nextLine().equals("H") 
                ? new StrategiaRestoHigher() 
                : new StrategiaRestoLower();
            final Distributore ds = new Distributore(binari, fondoCassa, strategia);
            
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                
                ParseUtils.Operazione op = ParseUtils.parseOperazione(line);            
                switch (op.operatore()) {  
                    case '+' -> System.out.println("+ " + ParseUtils.parseCarica(op.operando(), ds));
                    case '-' -> System.out.println("- " + ParseUtils.parseEroga(op.operando(), ds));
                    case '?' -> { for (String s : ds) System.out.println("? " + s); }
                    default -> throw new IllegalArgumentException("invalid");
                };
            }
        }
    }
}
 