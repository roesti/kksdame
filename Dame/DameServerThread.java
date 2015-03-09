import java.net.*;
import java.io.*;

public class DameServerThread extends Thread
{
   private DameServer       server    = null;
   private Socket           socket    = null;
   private int              ID        = -1;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;
   private String           color_string = null;

   public DameServerThread(DameServer _server, Socket _socket)
   {
      super();
      server = _server;
      socket = _socket;
      ID     = socket.getPort();
      
      boolean color_found = true;
      
      while (color_found == true)
      {
          String color = Tools.getRandomColor();
          
          boolean not_found = true;
          
          for (DameServerThread client : server.getClients())
          {
              if (client.getColorString().equals(color))
              {
                  not_found = false;
              }
          }
          
          if (not_found)
          {
              color_found = false;
              this.color_string = color;
          }
      }
   }
   
   public String getColorString()
   {
       return this.color_string;
   }
   
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   {  return ID;
   }
   public void run()
   {  System.out.println("Server Thread " + ID + " running.");
      while (true)
      {  try
         {  server.handle(ID, streamIn.readUTF());
         }
         catch(IOException ioe)
         {  System.out.println(ID + " ERROR reading: " + ioe.getMessage());
            server.remove(ID);
            stop();
         }
      }
   }
   public void open() throws IOException
   {  streamIn = new DataInputStream(new 
                        BufferedInputStream(socket.getInputStream()));
      streamOut = new DataOutputStream(new
                        BufferedOutputStream(socket.getOutputStream()));
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
      if (streamOut != null) streamOut.close();
   }
}