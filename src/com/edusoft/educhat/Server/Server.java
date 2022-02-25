package com.edusoft.educhat.Server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        ServerSocket serversocket = null;
        Socket socket = null;

        try{
            serversocket = new ServerSocket(8082);
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

    static ArrayList<String> listaMensajes = new ArrayList<String>();

    public static class ServerHandler extends Thread{

        Socket socket;

        public ServerHandler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run(){

            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;

            try{

                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(listaMensajes);

                String username = (String) ois.readObject();

                String message = "";
                int indexMessage = -1;

                while(!message.equals("message: bye")){

                    List<String> listaMensajesClientesActual = obtenerMensaje(indexMessage, listaMensajes);
                    oos.writeObject(listaMensajesClientesActual);

                    message = (String) ois.readObject();
                    Boolean isMessage = message.startsWith("message:");

                    if(isMessage){
                        // Generar hora y pasar a un formato que se puede imprimir
                        Long currentTimeMillis = System.currentTimeMillis();
                        Date horaActual = new Date(currentTimeMillis);
                        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                        String dateString = formatter.format(horaActual);
                        String sendMessage = "<" + username + ">" + "[ " + dateString + " ]: <" + message + ">";

                        listaMensajes.add(sendMessage);
                        oos.writeObject(sendMessage);

                        indexMessage =(int) ois.readObject();
                        System.out.println(indexMessage);
                    }else{
                        if(message.equals("message: bye")){
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

    public static List<String> obtenerMensaje (int index, ArrayList<String> mensajes){

        List<String> listamsg = new ArrayList<String>();

        if(!mensajes.isEmpty() && index != -1){
            for(int i = index; i < mensajes.size(); i++){
                listamsg.add(mensajes.get(i));
            }
        }

        return listamsg;

    }


}
