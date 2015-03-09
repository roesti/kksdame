
/**
 * Write a description of class Tools here.
 * 
 * @author Yannik, Thorsten, Johannes, Robert
 * @version 0.1
 */
public class Tools
{
    // instance variables - replace the example below with your own
    public static int getRandomInt(int min, int max)
    {
        return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
    }
    
    public static String translateCoordsToString(int zeile, int spalte)
    {
        String coords = null;
        
        switch(spalte)
        {
            case 0:
                coords = "a";
                break;
                
            case 1:
                coords = "b";
                break;
                
            case 2:
                coords = "c";
                break;
                
            case 3:
                coords = "d";
                break;
                
            case 4:
                coords = "e";
                break;
                
            case 5:
                coords = "f";
                break;
                
            case 6:
                coords = "g";
                break;
                
            case 7:
                coords = "h";
                break;
        }
        
        coords += "," + (zeile + 1);
        
        return coords;
    }
    
    public static int[] translateCoordsToInt(String buchstabe, String zahl)
    {
        int coord_array[] = new int[2];
        
        coord_array[0] = Integer.parseInt(zahl) - 1;
        
        if (buchstabe.toLowerCase().equals("a"))
        {
            coord_array[1] = 0;
        }
        
        if (buchstabe.toLowerCase().equals("b"))
        {
            coord_array[1] = 1;
        }
        
        if (buchstabe.toLowerCase().equals("c"))
        {
            coord_array[1] = 2;
        }
        
        if (buchstabe.toLowerCase().equals("d"))
        {
            coord_array[1] = 3;
        }
        
        if (buchstabe.toLowerCase().equals("e"))
        {
            coord_array[1] = 4;
        }
        
        if (buchstabe.toLowerCase().equals("f"))
        {
            coord_array[1] = 5;
        }
        
        if (buchstabe.toLowerCase().equals("g"))
        {
            coord_array[1] = 6;
        }
        
        if (buchstabe.toLowerCase().equals("h"))
        {
            coord_array[1] = 7;
        }
        
        return coord_array;
    }
    
    public static String getRandomColor()
    {
        String[] letters = new String[15];
        letters = "0123456789ABCDEF".split("");
        String code ="#";
        
        for(int i = 0; i < 6; i++)
        {
            double ind = Math.random() * 15;
            int index = (int)Math.round(ind);
            code += letters[index];
        }
        
        return code;
    }
} 
