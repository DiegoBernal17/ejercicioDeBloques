import java.io.BufferedReader;
import java.io.FileReader;

public class Bloques2 {
    private String[] bloques;
    private int[] posicion_a;
    private int[] posicion_b;

    public Bloques2(int total) {
        bloques = new String[total];

        for(int i=0; i<bloques.length; i++) {
            bloques[i] = " "+i;
        }
    }

    public void realizarAccion(String comando) {
        String[] tokens = comando.split(" ");
        int a,b;

        if(tokens.length == 4) {
            // Validación 1: Validar que a y b sean numeros
            try {
                a = Integer.parseInt(tokens[1]);
                b = Integer.parseInt(tokens[3]);
            } catch(NumberFormatException e) {
                return;
            }
            // Validación 2: a diferente a b
            if(a == b)
                return;
            // Validación 3: a y b no deben estar en la misma pila
            if(this.pilaAB(a,b))
                return;
            // Validación 4: Que la primer palabra sea move o pile
            switch (tokens[0]) {
                case "move":
                    // Validación 5: que la segunda palabra sea onto u over
                    switch (tokens[2]) {
                        case "onto":
                            this.moveOnto();
                            break;
                        case "over":
                            this.moveOver();
                            break;
                    }
                    break;
                case "pile":
                    // Validación 5: que la segunda palabra sea onto u over
                    switch (tokens[2]) {
                        case "onto":
                            this.pileOnto();
                            break;
                        case "over":
                            this.pileOver();
                            break;
                    }
                    break;
            }
            // Si el comando introducido no tiene espacios
        } else if(tokens.length == 1) {
            // Si contiene la palabra end termina
            if(tokens[0].equals("end"))
                System.exit(0);
        }
    }

    private void moveOnto() {
        this.moverAPosicionInicial(posicion_a);
        this.moverAPosicionInicial(posicion_b);
        this.agregarNumero(posicion_b[0], this.obtenerNum(posicion_a));
        this.removerNumero(posicion_a);
    }

    private void moveOver() {
        this.moverAPosicionInicial(posicion_a);
        this.agregarNumero(posicion_b[0], this.obtenerNum(posicion_a));
        this.removerNumero(posicion_a);
    }

    private void pileOnto() {
        this.moverAPosicionInicial(posicion_b);
        this.agregarPila(posicion_b[0], posicion_a);
        this.removerPila(posicion_a);
    }

    private void pileOver() {
        this.agregarPila(posicion_b[0], posicion_a);
        this.removerPila(posicion_a);
    }

    private void agregarNumero(int posicion, String numero) {
        bloques[posicion] += " "+numero;
    }

    private void removerNumero(int[] posicion) {
        bloques[posicion[0]] = bloques[posicion[0]].substring(0, posicion[1]-1);
    }

    private String obtenerNum(int[] posicion) {
        String[] num = bloques[posicion[0]].split(" ");
        return num[posicion[1]];
    }

    private void moverAPosicionInicial(int[] posicionActual) {
        int bloque = posicionActual[0];
        String actual = bloques[bloque].substring(0, posicionActual[1]+1);

        String[] pila = bloques[bloque].split(" ");
        for(int i=posicionActual[1]+1; i<pila.length; i++) {
            if(pila[i].equals(posicionActual[0]+""))
                actual += " "+pila[i];
            else
                this.agregarNumero(Integer.parseInt(pila[i]), pila[i]);
        }
        bloques[bloque] = actual;
    }

    private int[] getPosicion(int num) {
        String numString = num+"";
        int[] posicion = new int[2];
        for(int i=0; i<bloques.length; i++) {
            String[] bloqueActual = bloques[i].split(" ");
            for(int j=0; j<bloqueActual.length; j++) {
                if(numString.equals(bloqueActual[j])) {
                    posicion[0] = i;
                    posicion[1] = j;
                    return posicion;
                }
            }
        }
        posicion[0] = -1;
        posicion[1] = -1;
        return posicion;
    }

    private void agregarPila(int bloque, int[] posicion_a) {
        String[] bloque_aux = bloques[posicion_a[0]].split(" ");
        for(int i = posicion_a[1]; i < bloque_aux.length; i++) {
            this.agregarNumero(bloque, bloque_aux[i]);
        }
    }

    private void removerPila(int[] posicion) {
        bloques[posicion[0]] = bloques[posicion[0]].substring(0, posicion[1]+1);
    }

    private boolean pilaAB(int a, int b) {
        posicion_a = this.getPosicion(a);
        posicion_b = this.getPosicion(b);

        return (this.posicion_a[0] == this.posicion_b[0]);
    }

    public void mostrar() {
        for(int i=0; i<bloques.length; i++) {
            System.out.println(i+":"+bloques[i]);
        }
    }

    public static void main(String[] args) {
        int total;
        String[] lineasComando = Bloques2.leerContenido("comandos.txt").split("\n");
        total = Integer.parseInt(lineasComando[0]);
        Bloques2 robot = new Bloques2(total);
        for(int i=1; i<lineasComando.length; i++) {
            robot.realizarAccion(lineasComando[i]);
        }
        robot.mostrar();
    }

    public static String leerContenido(String archivo) {
        String cadena, contenido = "";
        FileReader f;
        BufferedReader b;
        try {
            f = new FileReader(archivo);
            b = new BufferedReader(f);
            while((cadena = b.readLine()) != null) {
                contenido += cadena+"\n";
            }
            b.close();
        } catch (Exception e) {
            System.out.println("Error con el archivo");
            System.exit(0);
        }
        return contenido;
    }
}
