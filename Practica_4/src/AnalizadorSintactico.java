package Practica_3.Practica_4.src;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class AnalizadorSintactico {

    private Lexico lexico;
    private ComponenteLexico componenteLexico;
    private Hashtable<String,String> simbolos;
    private String tipo;
    private int tamano;

    public AnalizadorSintactico(Lexico lexico) {
        this.lexico = lexico;
        this.componenteLexico = this.lexico.getComponenteLexico();
        this.simbolos = new Hashtable<String,String>();
    }

    public void analisisSintactico() {
        compara("void");
        compara("main");
        compara("open_bracket");
        declaraciones();
        instrucciones();
        compara("closed_bracket");
    }
    public void instrucciones(){

    }
    public void declaraciones() {
        if(this.componenteLexico.getEtiqueta().equals("int") ||
                this.componenteLexico.getEtiqueta().equals("float") ||
                this.componenteLexico.getEtiqueta().equals("boolean")) {
            declaracionVariables();
            declaraciones();
        }

        if(this.lexico.checkComments()){this.componenteLexico = this.lexico.getComponenteLexico();}
        if(this.lexico.checkComments()){this.componenteLexico = this.lexico.getComponenteLexico();}
        variable();
        variable();
    }
    public void declaracionVariables() {
        tipoPrimitivo();
        if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")){
            tipoVector();
            simbolos.put(this.componenteLexico.getValor(), tipo);
            compara("id");
        }
        else{listaIdentificadores();}
        compara("semicolon");
    }
    public void variable(){
        if(this.componenteLexico.getEtiqueta().equals("id")){
            compara("id");
            if(this.componenteLexico.getEtiqueta().equals("open_square_bracket")){
                compara("open_square_bracket");
                this.componenteLexico = this.lexico.getComponenteLexico();
                compara("closed_square_bracket");
            }
            compara("assignment");
            this.componenteLexico = this.lexico.getComponenteLexico();
            compara("semicolon");
        }
    }
    public void tipoVector(){
        compara("open_square_bracket");
        tamano = Integer.parseInt(this.componenteLexico.getValor());
        tipo = "array(" + tipo + "," + tamano + ")";
        this.componenteLexico = this.lexico.getComponenteLexico();
        compara("closed_square_bracket");
    }
    public void tipoPrimitivo() {
        /*if (this.componenteLexico.getEtiqueta().equals("int")){
            this.tipo = this.componenteLexico.getEtiqueta();
            compara("int");
        }else if (this.componenteLexico.getEtiqueta().equals("float")){
            this.tipo = this.componenteLexico.getEtiqueta();
            compara("float");
        }else if (this.componenteLexico.getEtiqueta().equals("boolean")){
            this.tipo = this.componenteLexico.getEtiqueta();
            compara("boolean");
        } else {
            System.out.println("Expected: int or float or boolean");
        }*/
        if (this.componenteLexico.getEtiqueta().equals("int") ||
                this.componenteLexico.getEtiqueta().equals("float") ||
                this.componenteLexico.getEtiqueta().equals("boolean")) {
            this.tipo = this.componenteLexico.getEtiqueta();
            this.componenteLexico = this.lexico.getComponenteLexico();

        } else {
            System.out.println("Expected: int or float or boolean");
        }
    }
    public void listaIdentificadores() {
        simbolos.put(this.componenteLexico.getValor(), tipo);
        compara("id");
        asignacionDeclarion();
        masIdentificadores();
    }
    public void asignacionDeclarion(){
        if (this.componenteLexico.getEtiqueta().equals("assignment")) {
            compara("assignment");
            this.componenteLexico = this.lexico.getComponenteLexico();
        }
    }
    public void masIdentificadores() {
        //Para leer las comas
        if (this.componenteLexico.getEtiqueta().equals("comma")) {
            compara("comma");
            compara("id");
            asignacionDeclarion();
            masIdentificadores();
        }
    }
    public void compara(String token) {
        if(this.componenteLexico.getEtiqueta().equals(token)) {
            this.componenteLexico = this.lexico.getComponenteLexico();
        }else {
            System.out.println("Expected: " + token);
            System.out.println(this.componenteLexico.getEtiqueta());
        }
    }
    public String tablaSimbolos() {
        String simbolos = "";

        Set<Map.Entry<String, String>> s = this.simbolos.entrySet();
        if(s.isEmpty()) System.out.println("La tabla de simbolos esta vacia\n");
        for(Map.Entry<String, String> m : s) {
            simbolos = simbolos + "<'" + m.getKey() + "', " +
                    m.getValue() + "> \n";
        }
        return simbolos;
    }
}