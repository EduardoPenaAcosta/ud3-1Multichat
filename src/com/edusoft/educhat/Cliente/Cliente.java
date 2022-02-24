package com.edusoft.educhat.Cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket("localhost", 8080);
             oos = new ObjectOutputStream(socket.getOutputStream());
             ois = new ObjectInputStream(socket.getInputStream());

            System.out.print("Escribe tu nombre de usuario:");
            String username = scanner.nextLine();
            oos.writeObject(username);

            String message = "";

            while(!message.equals("bye")) {

                System.out.print("Escribe el mensaje -> ");
                message = scanner.nextLine();
                oos.writeObject("message: "+ message);

                String respuesta = (String) ois.readObject();
                System.out.println("Mensaje ->" + respuesta);
            }


        } catch (IOException | ClassNotFoundException e) {
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

                try {
                    if(socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




    }
}
