package datos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class Bloques {
    private String[] bloques;

    public void inicializarBloques(int total)
    {
        bloques = new String[total];
        // Inicializar los bloques con su valor por defecto
        for (int i=0; i < bloques.length; i++)
        {
            bloques[i] = " "+i;
        }
    }

    public boolean esNumero(String ab)
    {
        try
        {
            Integer.parseInt(ab);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Verificar si el comando es valido
     */
    public boolean verificar(String comando)
    {
        String tokenActual;
        String a;
        // Dividimos por partes
        StringTokenizer tokens = new StringTokenizer(comando);
        // El total de partes divididas deben de ser 4 para ser comando valido
        if(tokens.countTokens() == 4)
        {
            tokenActual = tokens.nextToken();
            // Checar si no es numero el siguiente token
            a = tokens.nextToken();
            if(!esNumero(a))
            {
                return false;
            }
            // Checar por 'move' o 'pile'
            if(tokenActual.equals("move"))
            {
                return verificarParteDos(tokens, a);
            }
            else if(tokenActual.equals("pile"))
            {
                return verificarParteDos(tokens, a);
            }
        }
        // Si llegó hasta acá es que los tokens totales no son 4
        // Así que checa que sea 1 y que contenga la palabra 'end'
        else if(tokens.countTokens() == 1 && tokens.nextToken().equals("end"))
        {
            return true;
        }
        // Retorna false porque no es un comando valido
        return false;
    }

    public boolean verificarParteDos(StringTokenizer tokens, String a) {
        String tokenActual = tokens.nextToken();
        // Checar si no es numero el siguiente token
        String b = tokens.nextToken();
        if(!esNumero(b))
        {
            return false;
        }

        // Checa que si se encuentran en la misma pila del bloque
        // Y también sirve para checar que no sea el mismo numero
        int[] posi_a = obtenerPosicion(a);
        int[] posi_b = obtenerPosicion(b);
        if(posi_a[0] == posi_b[0])
        {
            return false;
        }

        // La siguiente palabra debe ser 'onto' u 'over'
        if (tokenActual.equals("onto"))
        {
            if(tokens.countTokens() == 0)
            {
                return true;
            }
        }
        else if(tokenActual.equals("over"))
        {
            if(tokens.countTokens() == 0)
            {
                return true;
            }
        }
        return false;
    }

    public void realizar(String comando)
    {
        String tokenActual;
        String a,b;
        // Dividimos por partes
        StringTokenizer tokens = new StringTokenizer(comando);
        tokenActual = tokens.nextToken();
        if(tokenActual.equals("end"))
        {
            System.exit(0);
        }

        a = tokens.nextToken();
        // Checar por 'move' o 'pile'
        if(tokenActual.equals("move"))
        {
            tokenActual = tokens.nextToken();
            // Checar si no es numero el siguiente token
            b = tokens.nextToken();
            // La siguiente palabra debe ser 'onto' u 'over'
            if (tokenActual.equals("onto"))
            {
                moveOnto(a,b);
            }
            else if(tokenActual.equals("over"))
            {
                moveOver(a,b);
            }
        }
        else if(tokenActual.equals("pile"))
        {
            tokenActual = tokens.nextToken();
            // Checar si no es numero el siguiente token
            b = tokens.nextToken();
            // La siguiente palabra debe ser 'onto' u 'over'
            if (tokenActual.equals("onto"))
            {
                pileOnto(a,b);
            }
            else if(tokenActual.equals("over"))
            {
                pileOver(a,b);
            }
        }
    }

    /**
     * Mueve toda la pila que está por encima del numero string ab dado
     */
    public void moverAInicial(String ab)
    {
        int[] posicion = obtenerPosicion(ab);
        int bloque = posicion[0];
        String aux = bloques[bloque].substring(0, posicion[1]+1);
        int num;

        String[] pila = bloques[bloque].split(" ");
        for(int i=posicion[1]+1; i<pila.length; i++)
        {
            num = Integer.parseInt(pila[i]);
            if(num == posicion[0])
            {
                aux += " " + pila[i];
            }
            else
            {
                bloques[num] += " " + pila[i];
            }
        }
        bloques[bloque] = aux;
    }

    public void moveOnto(String a, String b)
    {
        // Se mueve lo que esta encima de 'a' a su estado inicial
        moverAInicial(a);
        // Se mueve lo que esta encima de 'b' a su estado inicial
        moverAInicial(b);

        agregarA(a,b);
    }

    public void moveOver(String a, String b)
    {
        // Se mueve lo que esta encima de 'a' a su estado inicial
        moverAInicial(a);

        agregarA(a,b);
    }

    public void agregarA(String a, String b)
    {
        // Obtener posiciones de los numeros
        int[] posicion_a = obtenerPosicion(a);
        int[] posicion_b = obtenerPosicion(b);
        // Remover 'a'
        bloques[posicion_a[0]] = bloques[posicion_a[0]].substring(0, posicion_a[1]-1);
        // Agregar 'a' al final de 'b'
        bloques[posicion_b[0]] += " "+a;
    }

    public void pileOnto(String a, String b)
    {
        // Se mueve lo que esta encima de 'b' a su estado inicial
        moverAInicial(b);

        agregarPilaA(a,b);
    }

    public void pileOver(String a, String b)
    {
        agregarPilaA(a,b);
    }

    public void agregarPilaA(String a, String b)
    {
        int[] posicion_a = obtenerPosicion(a);
        int[] posicion_b = obtenerPosicion(b);
        StringTokenizer bloqueAux = new StringTokenizer(bloques[posicion_a[0]]);

        int i=0;
        // Agregar toda la pila de 'a' en 'b'
        while(bloqueAux.hasMoreTokens())
        {
            if(i >= posicion_a[1]-1)
            {
                bloques[posicion_b[0]] += " " + bloqueAux.nextToken();
            }
            else {
                bloqueAux.nextToken();
            }
            i++;
        }
        // Remover toda la pila de 'a'
        bloques[posicion_a[0]] = bloques[posicion_a[0]].substring(0, posicion_a[1]+1);
    }

    private int[] obtenerPosicion(String ab)
    {
        int[] posicion = new int[2];
        StringTokenizer bloqueActual;
        for(int i=0; i < bloques.length; i++)
        {
            bloqueActual = new StringTokenizer(bloques[i]);

            int j=1;
            while(bloqueActual.hasMoreTokens())
            {
                if(bloqueActual.nextToken().equals(ab))
                {
                    posicion[0] = i;
                    posicion[1] = j;
                    return posicion;
                }
                j++;
            }
        }
        posicion[0] = -1;
        posicion[1] = -1;
        return posicion;
    }

    public void imprimir()
    {
        for(int i=0; i < bloques.length; i++)
        {
            System.out.println(i+":"+bloques[i]);
        }
    }

    public String leer(String archivo)
    {
        String cadena, comandos = "";
        FileReader f;
        BufferedReader b;
        try
        {
            f = new FileReader(archivo);
            b = new BufferedReader(f);
            while((cadena = b.readLine()) != null)
            {
                comandos += cadena+"\n";
            }
            b.close();
        }
        catch (Exception e)
        {
            System.out.println("No se puede leer el archivo");
            System.exit(0);
        }
        return comandos;
    }

    public static void main(String[] args)
    {
        int total;
        Bloques bloques = new Bloques();
        String[] comandos = bloques.leer("comandos.txt").split("\n");
        total = Integer.parseInt(comandos[0]);
        bloques.inicializarBloques(total);
        for(int i=1; i<comandos.length; i++)
        {
            if(bloques.verificar(comandos[i]))
            {
                bloques.realizar(comandos[i]);
            }
        }
        bloques.imprimir();
    }
}
