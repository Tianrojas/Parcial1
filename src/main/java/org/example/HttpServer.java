package org.example;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.io.*;

import static java.util.EnumSet.range;

public class HttpServer {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while(running) {

            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            URI uri = null;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if(inputLine.split(" ")[0].equals("GET")){
                    String method = inputLine.split(" ")[0];
                    String uriStr = inputLine.split(" ")[1];
                    try {
                        uri = new URI(uriStr);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                } else if (inputLine.equals("")) {
                    break;
                } else if (inputLine.equals("")) {break; }
            }

            outputLine = "";

            if(uri.getPath().equals("/cliente")){
                outputLine = buildHTTP(httpForm());
            } else if(uri.getPath().equals("/consulta")){
                String calling = uri.getQuery().split("=")[1];
                System.out.println("Calling: " + calling);
                if (calling.startsWith("Class")){
                    outputLine = buildHTTP(ReflectiveResolver.getHTTPClass(getParams(calling)[0]));
                }else if(calling.startsWith("invoke")){
                    //outputLine = buildHTTP(ReflectiveResolver.getHTTPInvoke(getParams(calling)[0]));
                }else if(calling.startsWith("unaryInvoke")){

                }else if(calling.startsWith("binaryInvoke")){
                    outputLine = buildHTTP(ReflectiveResolver.getHTTPBinaryInvoke(getParams(calling)[0],getParams(calling)[1], getParams(calling)[3], getParams(calling)[4]));
                }else {
                    new IOException("Function not supported");
                }
            }



            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();

        }


        serverSocket.close();
    }

    public static String[] getParams(String in){
        String[] param = in.split(",");

        int cont = 0;
        int index = 0;

        param[0] = param[0].substring(param[0].indexOf("(")+1);

        for (int i=1 ; i<param.length; i++){
            param[i] = param[i].substring(1);
        }

        param[param.length-1] = param[param.length-1].substring(0, param[param.length-1].length() -1);
        return param;
    }

    public static String buildHTTP(String body){
        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>Main</title>\n"
                + "</head>\n"
                + "<body>\n"
                + body
                + "</body>\n"
                + "</html>\n";
        System.out.println(outputLine);
        return outputLine;
    }

    public static String httpForm(){
        return  "        <h1>Form with GET</h1>\n" +
                "        <form action=\"/hello\">\n" +
                "            <label for=\"name\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "\n";
    }

}