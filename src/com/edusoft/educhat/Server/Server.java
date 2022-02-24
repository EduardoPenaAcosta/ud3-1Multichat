package com.edusoft.educhat.Server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Server {

    public static void main(String[] args) {
        ServerSocket serversocket = null;
        Socket socket = null;

        try{
            serversocket = new ServerSocket(8080);
            while(true){

                socket = serversocket.accept();

                ServerHandler serverhandler = new ServerHandler(socket);
                serverhandler.start();

            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(serversocket != null){
                try {
                    serversocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if(socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ServerHandler extends Thread{

        Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run(){

            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            ArrayList<String> listaMensajes = new ArrayList<String>();


            try{

                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());



                String username = (String) ois.readObject();

                String message = "";

                while(!message.equals("bye")){

                    Boolean isMessage = message.startsWith("message:");
                    message = (String) ois.readObject();


                    if(isMessage){
                        // Generar hora y pasar a un formato que se puede imprimir
                        Long currentTimeMillis = System.currentTimeMillis();
                        Date horaActual = new Date(currentTimeMillis);
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateString = formatter.format(horaActual);
                        String sendMessage = "<" + username + ">" + "[ " + dateString + " ] escribió: <" + message + ">";

                        oos.writeObject(sendMessage);


                    }else{
                        if(message.equals("bye")){
                            oos.writeObject("Good bye");
                        }else{
                            oos.writeObject("Error!");
                        }
                    }



                }



            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if(ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        if(oos != null)oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        }

    }


}
