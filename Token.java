package pruebaa;
public class Token {
    final TipoToken tipo;//variable de la clase
    final String lexema;//cadenas
    final Object literal;//numeros`
    
    //Constructores

    public Token(TipoToken tipo, String lexema){//Constructor para cadenas
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;//se pone null para que la clase TipoToken 
        //se lea el valor de la cadena
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }
    
    public String toString(){
        return "<<"+tipo+" "+lexema+" "+literal+">>";
    }  
}
