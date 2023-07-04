package com.dashingdanstrains.dcstester;


import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws Exception {
        if (!tiuFound) {
            //Attempting to find TIU
            BonjourScanner bonjourScanner = new BonjourScanner();
            String tiuAddress = bonjourScanner.findTiuIpAddress();
            if (tiuAddress != null) {
                tiuFound = true;
                System.out.println("TIU found at " + tiuAddress);
            }
            Socket socket = new Socket(tiuAddress.split(":")[0], Integer.parseInt(tiuAddress.split(":")[1]));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            System.out.println("Handshaking");
            writer.println("H5");
            String response = reader.readLine();
            System.out.println("Received " + response);
            if(response.contains("okay")){
                System.out.println("Challenge received, answering challenge");
                String result = SpeckCipher.authenticate(response.split(" ")[1]);
                writer.println("H6"+result);
                response = reader.readLine();
                System.out.println("Received " + response);
                if(response.contains("okay")){
                    System.out.println("cipher accepted - completing");
                    writer.println("x");
                    response = reader.readLine();
                    System.out.println("Received " + response);
                    if(StringUtils.isNotEmpty(response)){
                        String command="";
                        Scanner one = new Scanner(System.in);
                        System.out.println("Enter command (quit to exit):");
                        while(!command.equals("quit")){
                            try {
                                while(one.hasNext()) {
                                    command = one.nextLine();
                                    writer.println(command);
                                    response = reader.readLine();
                                    System.out.println("Received " + response);
                                }
                            }catch (Exception ex){
                                System.out.println("Error: "+ex.getMessage());
                            }
                        }
                    }
                }

            }

        }
    }


   public static boolean tiuFound = false;

}
