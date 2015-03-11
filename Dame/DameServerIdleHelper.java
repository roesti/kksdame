import java.io.*;

class DameServerIdleHelper implements Runnable
{

    private DameServer server;

    public DameServerIdleHelper(DameServer server)
    {
        this.server = server;
    }

    public void run()
    {
        this.server.checkIdleClients();
        this.server.propagateUsers(); 
    }
}