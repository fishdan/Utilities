/*
 * An echo handler for a chat program
 * Written by Dan Fishman 12/4/97
 * Thanks to Tim Brady for the challenge.
 * This is a simple chat server that requires no client.
 * You can connect with telnet to whatever port you designate,
 * And everything will work fine.  At the moment the default port
 * is set to 8255 (spells talk on your phone)
 * Improvements to come...send solutions to bugs to dfish@cs.umb.edu
 */
//----------------------------------------------------------------------
/*
 * The ChatHandler does all the work.
 * The thread allows for multiple connections simultaneously
 */
import java.io.*;
import java.net.*;
import java.util.*;

class ChatHandler extends Thread{
  boolean done=false;
  ConnectedUser usr;
  Socket incoming;
  int counter;
  String str;
  String name;
  ChatHandler(Socket i, int c) {
   System.out.println("I, #"+c+", am alive!");
   incoming = i;
   counter = c;
   usr=new ConnectedUser(incoming,this);
  }
  public void parseCommand(String s,PrintStream ps){
   if(s.equals(""))return;
   System.out.println("parseCommand entered with "+s);
   StringTokenizer st=new StringTokenizer(s,"|");
   String cmd=st.nextToken().trim();
   System.out.println("cmd:"+cmd+" parsed");
   if(cmd.equals("reg"))reg(st,ps);
   else if(cmd.equals("login"))login(st,ps);
   else if(cmd.equals("private")){
      System.out.println("Trying to execute private chat");
      Admin.tellOne(st,this);
   }
   else if(cmd.equals("whois"))whois(st,ps);
   else if(cmd.equals("chat")){
      System.out.println("trying to chat");
      String line="";
      while(st.hasMoreTokens())
         line+=st.nextToken();
      Admin.tellAll("chat|"+usr.username+"::"+line);
   }
   //else if(cmd.equals("kill"))kill(st);
   else if(cmd.equals("BYE")){
      Admin.chatters.removeElementAt(Admin.findUserNumber(usr.username));
      Admin.tellAll("chat|"+usr.username+" has left the building.");
      Admin.tellAll("list|-|"+usr.username);
      done=true;
   }
  }


  public void run(){
   System.out.println("Running");

    try{
      DataInputStream in = new DataInputStream(incoming.getInputStream());
      PrintStream ps=new PrintStream(incoming.getOutputStream(),true);
      usr.driver=this;
      while(!done){
         System.out.println("Waiting to read");
         str = in.readLine();
         System.out.println("read: "+str);
         parseCommand(str,ps);
      }
      sleep(100);//MAN!! this line is so important.
    }
    catch(Exception e){
      System.out.println(e+" Caught at safety net.");
      parseCommand("BYE",System.out);//just needed a PrintStream to pass
    }
    System.out.println("Exiting"+usr.username);
  }



  public void reg(StringTokenizer st,PrintStream ps){
   System.out.println("entered reg");
   String name,username,email,password;
   String wholeFile="";
   try{
      username=st.nextToken();
      name=st.nextToken();
      email=st.nextToken();
      password=st.nextToken();
      System.out.println("Kosher up to here");
   }
   catch(Exception e){
      ps.println("illegal");
      ps.flush();
      done=true;
      return;
   }
   try{
         FileInputStream file=new FileInputStream("user.log");
         DataInputStream fileIn=new DataInputStream(file);
         String line;
         while((line=fileIn.readLine())!=null){
               StringTokenizer lst=new StringTokenizer(line,"|\n");
               String s;
               if((s=lst.nextToken()).equals(username)){
                  ps.println("duplicate");
                  ps.flush();
                  done=true;
                  return;
               }
               wholeFile+=line+"\n";
         }
   }
   catch(Exception e){
      System.out.println(e+" How did this happen?");
   }
   if(Admin.checkName(username)==1){
      ps.println("dirty");
      done=true;
      return;
   }
   try{
       FileOutputStream file=new FileOutputStream ("user.log");
       PrintStream pst=new PrintStream(file);
       pst.print(wholeFile);
       pst.println(username+"|"+name+"|"+email+"|"+password);
       pst.flush();
       ps.println("ok");
       ps.flush();
       done=true;
   }
   catch(IOException e){
         System.out.println("Failure to save data!");
   }
   System.out.println("Exiting reg");
  }
  public void login(StringTokenizer st,PrintStream ps){
   String name="";
   String password="Aliens invade the Earth";
   boolean nameOk=false;
   boolean legit=false;
   try{
      name=st.nextToken();
      password=st.nextToken();
      System.out.println("Name=="+name+"  Passwd=="+password);
   }
   catch(Exception e){
      ps.println("illegal user");
      done=true;
      return;
   }
   try{

         FileInputStream file=new FileInputStream("user.log");
         DataInputStream fileIn=new DataInputStream(file);
         String line;
         verify:
         while((line=fileIn.readLine())!=null){
               StringTokenizer lst=new StringTokenizer(line,"|\n");
               String s;
               if((s=lst.nextToken()).equals(name)){
                  System.out.println("Matched name");
                  nameOk=true;
                  s=lst.nextToken();
                  s=lst.nextToken();
                  s=lst.nextToken();
                  if(s.equals(password)){
                     System.out.println("Matched password");
                     legit=true;
                     break verify;
                  }
               }
         }
   }
   catch(Exception e){
      System.out.println("Damnit..how'd this happen!");
   }
   if(nameOk && legit){
      System.out.println("everything checks out");
      //usr.socket=incoming;
      usr.username=name;
      //usr.server=this;
      //Admin.chatters.addElement(usr);
      usr.connected=true;
      ps.println("ok");
      Admin.tellAll("chat|--"+name+" has entered the room--");
      Admin.tellAll("list|+|"+name);
      for(int x=0;x<Admin.chatters.size();x++){
         ConnectedUser cu=(ConnectedUser)Admin.chatters.elementAt(x);
         if(!cu.username.equals(name)&&cu.connected==true)
            ps.println("list|+|"+cu.username);
      }
   }
   else{
      if(nameOk)
         ps.println("illegal password");
      else ps.println("illegal user");
      ps.flush();
      done=true;
   }
 }

 public void whois(StringTokenizer st,PrintStream ps){
   String name=st.nextToken();
   try{
         FileInputStream file=new FileInputStream("user.log");
         DataInputStream fileIn=new DataInputStream(file);
         String line;
         hunt:
         while((line=fileIn.readLine())!=null){
               StringTokenizer lst=new StringTokenizer(line,"|\n");
               String s;
               if((s=lst.nextToken()).equals(name)){
                  ps.println("chat|"+name);
                  ps.println("chat|Real Name=="+lst.nextToken());
                  ps.println("chat|Email=="+lst.nextToken());
                  break hunt;
               }
         }
   }
   catch(Exception e){
      System.out.println(e+" How did this happen?");
   }

 }
}


/*
 * ChatServer waits for people to connect and then starts
 * a new thread for each connection and adds them to the
 * administration list
 */
class ChatServer{
  public static void main(String[] args ) {
    int i = 1;
    try {
      FileInputStream file=new FileInputStream("user.log");
    }
    catch(FileNotFoundException e){
      try{
      FileOutputStream f=new FileOutputStream("user.log");
      PrintStream out=new PrintStream(f);
      //String name=System.getProperty("user.name");
      out.println("Administrator|root|fix later|toor");
      out.flush();
      out.close();
      }
      catch(Exception et){}
    }
    try {
      ServerSocket s = new ServerSocket(8255);
      for (;;){   //I think there should be a for(ever) command
         Socket incoming = s.accept( );
         System.out.println("Spawning " + i);
         i=Admin.chatters.size();

         //ConnectUser cu =new ConnectUser(incoming);
         ChatHandler c=new ChatHandler(incoming, i);
         ConnectedUser cu=c.usr;
         Admin.chatters.addElement(cu);
         c.start();
   //Start the ChatHandler
   //**   new ChatHandler(incoming, i).start();
   //Give each user a new number
   //No longer needed, but a mapping to more
         i++;
      }
    }
    catch (Exception e){
      System.out.println(e+" in CServer");
    }
  }
}
class ConnectedUser{
   Socket connection;
   String username;
   ChatHandler driver;
   boolean connected;
   public ConnectedUser(Socket s,ChatHandler boss){
      connection=s;
      username="-X-";
      driver=boss;
      connected=false;
   }
}
class Admin{
  //static Vector allOn=new Vector();
  static Vector chatters=new Vector();
  static String[] badWords={
    "shit","fuck","cock","bastard","penis","damn","ass",
    "dick","prick","pussy","cunt","dildo","jerkoff",
    "jism","sperm","vagina","bloody","fart","queef",
    "blowjob","suck","lick","blow","douche","slut",
    "whore","fag","semen","twat","hell","anal","sloppy"

  };

  /*
   * In order to keep the line from being blocked, we must
   * use an administrator to cycle outputs.
   */
  public static void tellOne(StringTokenizer st,ChatHandler driver){
     System.out.println("entered tellOne with "+st.countTokens()+" tokens");
     //if(st.countTokens()<2){
     // System.out.println("less than two tokens
     // return;
     String receiver=st.nextToken();
     String line=st.nextToken();
     //System.out.println("Receiver="+receiver);
     //while(st.hasMoreTokens())
     // line+=st;
     String sender=driver.usr.username;
     System.out.println("Private message from "+sender+" to "+receiver);
     for(int x=0;x<(chatters.size());x++){
      ConnectedUser user=(ConnectedUser)chatters.elementAt(x);
      String listener=user.username;
      if(listener.equals(receiver)){
         try{
            Socket s=(Socket)(user.connection);
            PrintStream out = new PrintStream(s.getOutputStream(),true);
            out.println("chat|Private Message from: "+sender);
            out.println("chat|<<"+line+">>");
            out.flush();
         }
         catch(Exception e){
            System.out.println("unattended socket...removing in Private");
            Admin.chatters.removeElementAt(x);
            //user.driver.kill();
         }
      }
      if(listener.equals(sender)){
         try{
            Socket s=(Socket)(user.connection);
            PrintStream out = new PrintStream(s.getOutputStream(),true);
            out.println("chat|Private Message sent to: "+receiver);
            out.println(line);
            out.flush();
         }
         catch(Exception e){
            System.out.println("unattended socket...removing in Private");
            Admin.chatters.removeElementAt(x);
            //user.driver.kill();
         }
      }
     }
  }
  public static void tellAll(String line){
    try{
      for(int x=0;x<(chatters.size());x++){
         System.out.println("Writing to "+x);
         try{
            ConnectedUser cu=(ConnectedUser)(chatters.elementAt(x));
            if(cu.connected){
               Socket s=cu.connection;
               PrintStream out = new PrintStream(s.getOutputStream(),true);
               out.println(line);
               out.flush();
            }
         }
         catch(Exception e){
            System.out.println("unattended socket...removing");
            Admin.chatters.removeElementAt(x);
            x--;
         }
      }
    }
    catch(Exception e){
      System.out.println(e+" in Admin.tellAll()");
    }
  }
  public static int findUserNumber(String name){
   for(int a=0;a<chatters.size();a++)
      if(name.equals(((ConnectedUser)chatters.elementAt(a)).username))return a;
   return -1;
  }
  /*
  public static void addName(String name){
    allOn.addElement(name);
  }
  public static int removeName(String name){
    for(int x=0;x<allOn.size();x++){
      if(name.equals((String)allOn.elementAt(x))){
   allOn.removeElementAt(x);
   return x;
      }
    }
    return 0;
  }
  */
  public static int checkName(String name){
    /*
     * To speed checking this should not make any System.out.println
     * calls, nor should it continue checking for bad words after the
     * first find.  This just allows funny statements for mulitple
     * swearing.
     * To fix this change the return value on the method to boolean
     * and remove numBad, replacing the line numBad++; with
     * return false; and return true if nothing bad is found.
     */
    int numBad = 0;
    for(int x=0;x<badWords.length;x++){
      System.out.println("Checking "+badWords[x]);
      /*
       * Check our list of badwords against all the characters in the
       * submited name.  If the badword is larger than the submitted name,
       * skip it.  Else check if bad word is contained in submitted
       * name.  If it is, break out.
       */
      for(int y=0;y<=(name.length()-badWords[x].length());y++){
   System.out.println(badWords[x]+
      " compared to "+name.substring(y,badWords[x].length()+y));
   if(name.regionMatches(true,y,badWords[x],0,badWords[x].length()-1))
     {
       System.out.println("match");
       numBad++;
     }
      }
    }
    if(numBad==0){
      for(int x=0;x<chatters.size();x++){
         if(name.equals(((ConnectedUser)chatters.elementAt(x)).username))
      return 463;
      }
    }

    return numBad;
  }
}








