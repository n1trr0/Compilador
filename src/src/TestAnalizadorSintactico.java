package CompiladorPDL.src.src;
//Raul Garcia & Gonzalo Sepulveda
public class TestAnalizadorSintactico {

    public static void main (String[] args) {

        boolean mostrarComponentesLexicos = true; //poner a false y no se quieren mostrar los tokens <id, a> ...

        String expresion = "void main {\n" +
                " int a = 1, b = 2, c = (25 * (2 + a)) / (2 * b), d = a + 2*b + c;\n" +
                " if (d >= 10 and d <= 20 or d < 5) {\n" +
                " c = d - 5;\n" +
                " } else {\n" +
                " c = d + 5;\n" +
                " }\n" +
                " int i = 0;\n" +
                " while (i <=10 and c >= 0) {\n" +
                " c = c - 1;\n" +
                " i = i + 1;\n" +
                " }\n" +
                " if (a <= 10)\n" +
                " c = 1;\n" +
                " else {\n" +
                " c = 2;\n" +
                " do {\n" +
                " c = c + 2;\n" +
                " a = a - 1;\n" +
                " } while (a >= 0);\n" +
                " }\n" +
                " print(a);\n" +
                " print(b);\n" +
                " print(c);\n" +
                " print(d);\n" +
                "}\n";

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

        System.out.println("Tabla de símbolos: " );
        String simbolos = compilador.tablaSimbolos();
        System.out.println(simbolos);

    }

}