package pruebaa;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class PRUEBAA {
    static boolean existenErrores = false;
    public static void main(String[] args) throws IOException{
        if(args.length>1){
            System.out.println("Uso correcto: interprete [archivo.txt]");
            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        }else if(args.length==1){
            ejecutarArchivo(args[0]);
        }else{
            ejecutarPrompt();
        }
    }
    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));
        // Se indica que existe un error
        if(existenErrores) System.exit(65);
    }
    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }
    private static void ejecutar(String source){
        try{//
            verifica scanner= new verifica(source);//scanner guarda la informacion que se envia
            List<Token> tokens=scanner.scan();
            for(Token token: tokens){//for-each tokens se mete en token para recorrerlo 
                System.out.println(token);//se llama el metodo de toString de la clase Token
            }
        }
        catch(Exception ex){//variable ex que se utiliza para una excepcion
            ex.printStackTrace();//imprime donde fue la excepcion
        }
    }
}
