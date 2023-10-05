package pruebaa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class verifica {
    //Map es una estructura de datos de hashmap, TipoToken tiene una clase constructor
    //Para recuperar sus datos y ademas regresar un String con los resultados
    private static Map<String, TipoToken> reservado;
    static{//bloque de inicializacion estatica, carga todo lo que tenemos 
           //ahi cuando se llama la clase
        reservado= new HashMap<>();//se inicializa la variable con la estructura de datos
        reservado.put("and", TipoToken.AND);//put es metodo para agregar el par, key: and y el string que esta en otra clase
        reservado.put("else",   TipoToken.ELSE);
        reservado.put("false",  TipoToken.FALSE);
        reservado.put("for",    TipoToken.FOR);
        reservado.put("fun",    TipoToken.FUN);
        reservado.put("if",     TipoToken.IF);
        reservado.put("null",   TipoToken.NULL);
        reservado.put("or",     TipoToken.OR);
        reservado.put("print",  TipoToken.PRINT);
        reservado.put("return", TipoToken.RETURN);
        reservado.put("true",   TipoToken.TRUE);
        reservado.put("var",    TipoToken.VAR);
        reservado.put("while",  TipoToken.WHILE);
        reservado.put("=",  TipoToken.EQUAL);
        reservado.put("+", TipoToken.PLUS);
        reservado.put("-", TipoToken.MINUS);
        reservado.put("*", TipoToken.STAR);
        reservado.put("/",TipoToken.SLASH);
        reservado.put("<", TipoToken.LESS);
        reservado.put(">", TipoToken.GREATER);
        reservado.put("<=", TipoToken.LESS_EQUAL);
        reservado.put(">=", TipoToken.GREATER_EQUAL);
        reservado.put("==", TipoToken.EQUAL_EQUAL);
        reservado.put("!", TipoToken.BANG);
        reservado.put("!=",TipoToken.BANG_EQUAL);
        reservado.put("(", TipoToken.LEFT_PAREN);
        reservado.put(")", TipoToken.RIGHT_PAREN);
        reservado.put("{", TipoToken.RIGHT_BRACE);
        reservado.put("}", TipoToken.LEFT_BRACE);
        reservado.put(",", TipoToken.COMMA);
        reservado.put(".", TipoToken.DOT);
        reservado.put(";", TipoToken.SEMICOLON);
    }
    public boolean operador(char c){
    String operadores="+-*/<>=!";
    return operadores.indexOf(c)!=-1;
    }
    public boolean parentesis(char c){
        String parentesi="(){}";
        return parentesi.indexOf(c)!=-1;
    }
    public boolean puntos(char c){
        String punto=",.;";
        return punto.indexOf(c)!=-1;
    }
    private int con =0;
    private int p=0;
    String lex="";
    private final String source;//Variable tipo string constante y privada para la cadena a recolectar
    private final List<Token> tokens= new ArrayList<>();
    public verifica(String source){//constructor de la variable para recuperar la informacion y agregar un valor
        this.source = source+' ';
    }
    private boolean pa=false;
    //funcion para recolectar
    public List<Token> scan() throws Exception{
        String lexema="";
        int estado=0,i;
        char c;
        //funcion para verificar los valores
        for(i=0; i<source.length(); i++){
            c=source.charAt(i);
            switch(estado){
                case 0://Este caso verifica si son letras o numeros
                    if(operador(c)){//caso de operadores +-*/<>
                            estado=8;
                            lexema+=c;
                        }
                    else if(parentesis(c)){
                            estado=5;
                            i--;
                    }
                    else if(puntos(c)){
                        lexema+=c;
                        if(c==';'){
                            Token t= new Token(TipoToken.SEMICOLON, lexema,null);
                            estado=0;
                            lexema="";
                            tokens.add(t);  
                        }
                        else if(c=='.'){
                            Token t= new Token(TipoToken.DOT, lexema,null);
                            estado=0;
                            lexema="";
                            tokens.add(t);
                        }
                        else if(c==','){
                            Token t= new Token(TipoToken.COMMA, lexema,null);
                            estado=0;
                            lexema="";
                            tokens.add(t);
                        }
                    }
                    else if(Character.isLetter(c)){
                        estado=9;
                        lexema+=c; 
                    }
                    else if(Character.isDigit(c)){//12e+9 lexema=12e++
                        boolean digito=false,pun=false;
                        int sig=0;
                        while(Character.isDigit(c) || c=='.' || c=='E' || c=='e' || c=='-' || c=='+'){
                            if(c=='+' || c=='-'){
                                    if(((source.charAt(lexema.length()-1)=='e' || source.charAt(lexema.length()-1)=='E') && (c=='+' || c=='-') && pun==false)){
                                    lexema+=c;
                                    i++;
                                    c = source.charAt(i);
                                        if(c=='+' || c=='-'){
                                         pun=true; 
                                        }
                                    }else{
                                        break;
                                    }
                                }
                            if(c=='.'){
                                sig++;
                                if(sig>1){
                                    break;
                                }
                            }
                            lexema += c;
                            i++;
                            if (i < source.length()) {
                                c = source.charAt(i);
                                if ((Character.isLetter(c) && (c != 'E' && c != 'e' && c!='.'))||(pun==true)||(sig>1)){
                                    // Hay una letra que no es 'E' ni 'e' después de la secuencia de dígitos
                                    digito = true;
                                    while(c==' ' || !Character.isWhitespace(c)){
                                        lexema += c;
                                        i++;
                                        if (i < source.length()) {
                                            c = source.charAt(i);
                                        } else {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                        if(digito==true){
                            System.out.println("ERROR EN NUMEROS");
                            estado=0;
                            lexema="";
                            break;
                        }
                        else{
                            Token t= new Token(TipoToken.NUMBER, lexema,lexema);
                            estado=0;
                            lexema="";
                            i--;
                            tokens.add(t);  
                        }
                    }
                    else if(c=='"'){//caso de strings o comillas
                        estado=7;
                        lex+=c;
                        pa=true; 
                    }
                    else if(c=='/'){//caso de comentarios de una linea 
                        con+=1; //contador de /
                        estado=6;
                        lexema+=c;
                    }
                    break;
                case 5: // Caso de paréntesis
                if (c == '('){
                    lexema+=c;
                    Token t = new Token(TipoToken.LEFT_PAREN, lexema);
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                    p++;
                } else if (c == ')') {
                    lexema+=c;
                    Token t = new Token(TipoToken.RIGHT_PAREN, lexema);
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                    p--;
                } else if (c == '{') {
                    lexema+=c;
                    Token t = new Token(TipoToken.LEFT_BRACE, lexema);
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                    p++;
                } else if (c == '}'){
                    lexema+=c;
                    Token t = new Token(TipoToken.RIGHT_BRACE, lexema);
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                    p--;
                } else {
                    if (p != 0) {
                        System.out.println("ERROR EN PARENTESIS O LLAVES");
                    }
                }
                break;
                case 6://caso de comentarios
                    if(c=='/' && con==1){
                        con--;
                        lexema+=c;
                        i++;
                        c=source.charAt(i);
                        while(Character.isDigit(c) || Character.isLetter(c) || c==' ' || !Character.isWhitespace(c)){
                            lexema+=c;
                            i++;
                            if(i<source.length())
                            c=source.charAt(i);
                            else
                                break;
                        }
                        Token t= new Token(TipoToken.COMENT, lexema);
                         tokens.add(t);
                         lexema="";
                         estado=0;
                    }
                    else if(c=='*' && con==1){
                        boolean a=false;
                        lexema+=c;
                        i++;
                        c=source.charAt(i);
                        while(Character.isDigit(c) || Character.isLetter(c) || c==' ' || !Character.isWhitespace(c) || c=='\n'){
                            lexema+=c;///*hjs
                            i++;
                            if(i<source.length()){
                            c=source.charAt(i);
                            
                            if(c=='/' && a==true){
                                lexema+=c;
                                Token t= new Token(TipoToken.COMENT, lexema);
                                tokens.add(t);
                                estado=0;
                                lexema="";
                                con--;
                                break;
                            }
                            else if(c=='*'){
                                a=true;
                            }else{
                                a=false;
                            }
                        }
                        } 
                    }
                    else{
                        System.out.println("ERROR EN COMENTARIOS: "+c);
                    }
                    break;
                case 7://caso de cadenas "hols
                    if((c==' '||parentesis(c)||puntos(c) ||operador(c)|| Character.isDigit(c)|| Character.isLetter(c))&& pa==true && (i < source.length() - 1)){
                        lexema+=c;
                        lex+=c;
                        estado=7; 
                    }
                    else if(c=='"'&& pa==true ){
                        pa=false;
                        lex+="\"";
                        Token t= new Token(TipoToken.STRING, lex,lexema);
                         tokens.add(t);
                         lexema="";
                         lex="";
                         estado=0;
                    }
                    /*
                     else if ( pa == true && i == source.length() - 1) {
                        System.out.println("ERROR: Falta cerrar la cadena");
                    }*/
                    else{
                        System.out.println("ERROR AL CERRAR CADENA");
                    }
                    break;
                case 8://caso de operadores
                    if(c=='/'||c=='*'){
                        estado=6;
                        con+=1;
                        i--;
                    }
                    else if(lexema.charAt(lexema.length()-1)=='='&&c=='='){
                        lexema+=c;
                        Token t= new Token(TipoToken.EQUAL_EQUAL, lexema);
                        estado=0;
                        lexema="";
                        tokens.add(t);
                    }
                    else if(lexema.charAt(lexema.length()-1)=='<'&&c=='='){
                        lexema+=c;
                        Token t= new Token(TipoToken.LESS_EQUAL, lexema);
                        estado=0;
                        lexema="";
                        tokens.add(t);
                    }
                    else if(lexema.charAt(lexema.length()-1)=='>'&&c=='='){
                        lexema+=c;
                        Token t= new Token(TipoToken.GREATER_EQUAL, lexema);
                        estado=0;
                        lexema="";
                        tokens.add(t);
                    }
                    else if(lexema.charAt(lexema.length()-1)=='!'&&c=='='){
                        lexema+=c;
                        Token t= new Token(TipoToken.GREATER_EQUAL, lexema);
                        estado=0;
                        lexema="";
                        tokens.add(t);
                    }
                    else{
                        //creamos el tojen identificador o palabra reservada
                        TipoToken tt=reservado.get(lexema);
                        if(tt==null){//Quiere decir que no es palabra reservada
                            Token t= new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t=new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;
                case 9:
                    if(Character.isLetter(c) || Character.isDigit(c)){
                        estado=9;
                        lexema+=c;
                    }
                    else{
                        //creamos el tojen identificador o palabra reservada
                        TipoToken tt=reservado.get(lexema);
                        if(tt==null){//Quiere decir que no es palabra reservada
                            Token t= new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }
                        else{
                            Token t=new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado=0;
                        lexema="";
                        i--;
                    }
                    break;
            }
        }
        return tokens;
    }
}