package CompiladorPDL.src.src;
//Raul Garcia & Gonzalo Sepulveda
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class AnalizadorSintactico {

    private Lexico lexico;
    private ComponenteLexico componenteLexico;
    private Hashtable<String,String> simbolos;
    private String tipo;
    private int tamano;
    private String verificar;
    private String verificarAux;
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
        if(this.componenteLexico.getEtiqueta().equals("int")||
                this.componenteLexico.getEtiqueta().equals("float")||
                this.componenteLexico.getEtiqueta().equals("boolean") ||
                this.componenteLexico.getEtiqueta().equals("id")||
                this.componenteLexico.getEtiqueta().equals("if")||
                this.componenteLexico.getEtiqueta().equals("while")||
                this.componenteLexico.getEtiqueta().equals("do") ||
                this.componenteLexico.getEtiqueta().equals("print") ||
                this.componenteLexico.getEtiqueta().equals("open_bracket") ||
                this.componenteLexico.getEtiqueta().equals("comment") ||
                this.componenteLexico.getEtiqueta().equals("comments")){
            instruccion();
            instrucciones();
        }
    }
    public void instruccion(){
        switch (this.componenteLexico.getEtiqueta()) {
            case "int", "float", "boolean" -> declaracionVariables();
            case "id" -> {
                verificar = verificarDatos(this.componenteLexico.getValor());
                if(verificar == null){System.out.println("Error en la linea "+lexico.getLineas() +", identificador '"+this.componenteLexico.getValor()+"' no declarado");}
                variable();
                compara("assignment");
                expresionLogica();
                compara("semicolon");
            }
            case "if" -> {
                compara("if");
                compara("open_parenthesis");
                expresionLogica();
                compara("closed_parenthesis");
                instruccion();
                if (this.componenteLexico.getEtiqueta().equals("else")) {
                    compara("else");
                    instruccion();
                }
            }
            case "while" -> {
                compara("while");
                compara("open_parenthesis");
                expresionLogica();
                compara("closed_parenthesis");
                instruccion();
            }
            case "do" -> {
                compara("do");
                instruccion();
                compara("while");
                compara("open_parenthesis");
                expresionLogica();
                compara("closed_parenthesis");
                compara("semicolon");
            }
            case "print" -> {
                compara("print");
                compara("open_parenthesis");
                variable();
                compara("closed_parenthesis");
                compara("semicolon");
            }
            case "open_bracket" -> {
                compara("open_bracket");
                instrucciones();
                compara("closed_bracket");
            }
            case "comment" -> {
                compara("comment");
                this.lexico.shortComments();
                this.componenteLexico = this.lexico.getComponenteLexico();
            }
            case "comments" -> {
                compara("comments");
                comentarioInfinito();
            }
            default -> System.out.println("Instruccion invalida");
        }
    }
    public void comentarioInfinito(){
        while (true){
            if (this.componenteLexico.getEtiqueta().equals("comments_")){
                compara("comments_");
                return;
            }else {
                this.componenteLexico = this.lexico.getComponenteLexico();
            }
        }
    }
    public void declaraciones() {
        if(this.componenteLexico.getEtiqueta().equals("int") ||
                this.componenteLexico.getEtiqueta().equals("float") ||
                this.componenteLexico.getEtiqueta().equals("boolean")) {
            declaracionVariables();
            declaraciones();
        }

        //if(this.lexico.checkComments()){this.componenteLexico = this.lexico.getComponenteLexico();}
    }
    public void declaracionVariables() {
        tipoPrimitivo();
        if (this.componenteLexico.getEtiqueta().equals("open_square_bracket")){
            tipoVector();
            verificar = verificarDatos(this.componenteLexico.getValor());
            if(verificar != null){System.out.println("Error en la linea "+lexico.getLineas() +", identificador '"+this.componenteLexico.getValor()+"' ya declarado");}
            simbolos.put(this.componenteLexico.getValor(), tipo);
            compara("id");
            compara("semicolon");
        }
        else{
            listaIdentificadores();
            compara("semicolon");
        }

    }
    public void variable(){
        if(this.componenteLexico.getEtiqueta().equals("id")){
            compara("id");
            if(this.componenteLexico.getEtiqueta().equals("open_square_bracket")){
                compara("open_square_bracket");
                expresion();
                compara("closed_square_bracket");
            }
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
        verificar = verificarDatos(this.componenteLexico.getValor());
        if(verificar != null){System.out.println("Error en la linea "+lexico.getLineas() +", identificador '"+this.componenteLexico.getValor()+"' ya declarado");}
        simbolos.put(this.componenteLexico.getValor(), tipo);
        compara("id");
        asignacionDeclarion();
        masIdentificadores();
    }
    public void asignacionDeclarion(){
        if (this.componenteLexico.getEtiqueta().equals("assignment")) {
            compara("assignment");
            expresionLogica();
        }
    }
    public void masIdentificadores() {
        //Para leer las comas
        if (this.componenteLexico.getEtiqueta().equals("comma")) {
            compara("comma");
            verificar = verificarDatos(this.componenteLexico.getValor());
            if(verificar != null){System.out.println("Error en la linea "+lexico.getLineas() +", identificador '"+this.componenteLexico.getValor()+"' ya declarado");}
            simbolos.put(this.componenteLexico.getValor(), tipo);
            compara("id");
            asignacionDeclarion();
            masIdentificadores();
        }
    }
    public void expresionLogica(){
        terminoLogico();
        expresionLogica_();
    }
    public void expresionLogica_(){
        if(this.componenteLexico.getEtiqueta().equals("or")){
            compara("or");
            terminoLogico();
            expresionLogica_();
        }
    }
    public void terminoLogico(){
        factorLogico();
        terminoLogico_();
    }
    public void terminoLogico_(){
        if(this.componenteLexico.getEtiqueta().equals("and")) {
            compara("and");
            factorLogico();
            terminoLogico_();
        }
    }
    public void factorLogico(){
        switch (this.componenteLexico.getEtiqueta()) {
            case "not" -> {
                compara("not");
                factorLogico();
            }
            case "true" -> compara("true");
            case "false" -> compara("false");
            default -> expresionRelacional();
        }
    }
    public void expresionRelacional(){
        expresion();
        if(this.componenteLexico.getEtiqueta().equals("less_than")||
                this.componenteLexico.getEtiqueta().equals("less_equals")||
                this.componenteLexico.getEtiqueta().equals("greater_than") ||
                this.componenteLexico.getEtiqueta().equals("greater_equals")||
                this.componenteLexico.getEtiqueta().equals("equals")||
                this.componenteLexico.getEtiqueta().equals("not_equals")){
            operadorRelacional();
            expresion();
        }
    }
    public void expresion(){
        termino();
        expresion_();
    }
    public void expresion_(){
        if(this.componenteLexico.getEtiqueta().equals("add")) {
            compara("add");
            termino();
            expresion_();
        } else if (this.componenteLexico.getEtiqueta().equals("subtract")) {
            compara("subtract");
            termino();
            expresion_();
        }
    }
    public void termino(){
        factor();
        termino_();
    }
    public void termino_(){
        switch (this.componenteLexico.getEtiqueta()) {
            case "multiply" -> {
                compara("multiply");
                factor();
                termino_();
            }
            case "divide" -> {
                compara("divide");
                factor();
                termino_();
            }
            case "remainder" -> {
                compara("remainder");
                factor();
                termino_();
            }
        }
    }
    public void factor(){
        if(this.componenteLexico.getEtiqueta().equals("open_parenthesis")) {
            compara("open_parenthesis");
            expresion();
            compara("closed_parenthesis");
        } else if (this.componenteLexico.getEtiqueta().equals("id")) {
            verificarAux = verificarDatos(this.componenteLexico.getValor());
            if(verificar != null){
                if(!verificar.equals(verificarAux)){
                    System.out.println("Error en la linea "+lexico.getLineas() +", incompatibilidad de tipos en la instrucción de asignación");
                }
            }
            variable();
        }else{
            //tamano = Integer.parseInt(this.componenteLexico.getValor());
            this.componenteLexico = this.lexico.getComponenteLexico();
        }
    }
    public void operadorRelacional(){
        switch (this.componenteLexico.getEtiqueta()) {
            case "less_than" -> compara("less_than");
            case "less_equals" -> compara("less_equals");
            case "greater_than" -> compara("greater_than");
            case "greater_equals" -> compara("greater_equals");
            case "equals" -> compara("equals");
            case "not_equals" -> compara("not_equals");
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
    public String verificarDatos(String variable){
        String tipos;
        Set<Map.Entry<String, String>> s = this.simbolos.entrySet();
        if(s.isEmpty()){
            return tipos = null;
        }else {
            for (Map.Entry<String, String> m : s) {
                if (variable.equals(m.getKey())) {
                    tipos = m.getValue();
                    return tipos;
                }
            }
        }
        return tipos = null;
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