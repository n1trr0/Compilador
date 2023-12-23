package Practica_3.Practica_4.src;

public class TestAnalizadorSintactico {

    public static void main (String[] args) {

        boolean mostrarComponentesLexicos = true; //poner a false y no se quieren mostrar los tokens <id, a> ...

        String expresion = "void main {\n" +
                " int [10] f;\n" +
                " // sucesión de Fibonacci\n" +
                " f[0] = 0;\n" +
                " f[1] = 1;\n" +
                " int i = 2;\n" +
                " while (i < 10) {\n" +
                " f[i] = f[i-1] + f[i-2];\n" +
                " i = i + 1;\n" +
                " }\n" +
                " i = 0;\n" +
                " while (i < 10) {\n" +
                " print(f[i]);\n" +
                " i = i + 1;\n" +
                " }\n" +
                "}";

        ComponenteLexico etiquetaLexica;
        Lexico lexico = new Lexico(expresion);

        if(mostrarComponentesLexicos) {

            do {
                etiquetaLexica = lexico.getComponenteLexico();
                System.out.println("<" + etiquetaLexica.toString() + ">"); //System.out.println(etiquetaLexica.toString());

            }while(!etiquetaLexica.getEtiqueta().equals("end_program"));

            System.out.println("");
        }

        AnalizadorSintactico compilador = new AnalizadorSintactico (new Lexico(expresion));

        System.out.println("Compilación de sentencia de declaraciones de variables");
        System.out.println(expresion + "\n");

        compilador.analisisSintactico();

        System.out.println("Tabla de símbolos \n\n" );
        String simbolos = compilador.tablaSimbolos();
        System.out.println(simbolos);

    }

}