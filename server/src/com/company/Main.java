package com.company;

import com.sun.nio.sctp.AbstractNotificationHandler;

import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static int imageLength = 0;
    //the following vars are used to get the new msg in the chat room
    public static DataOutputStream user1 = null;
    public static DataOutputStream user2 = null;
    public static DataOutputStream addFriend1;
    public static DataOutputStream addFriend2;
    public static boolean addFriend1boolean = false;
    public static boolean addFriend2boolean = false;
    public static boolean user1boolean = false;
    public static boolean user2boolean = false;
    static HashMap<String, User> clients = new HashMap<>();
    public static Map<String, ArrayList<Message>> chats = new HashMap<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<Room> rankedrooms = new ArrayList<>();
    //Mohammad path Files :
    public static String chatsPath = "C:\\Users\\Mohammad\\IdeaProjects\\Plato_Main_Server\\src\\com\\company\\Chats.txt";
    public static String clientsPath = "C:\\Users\\Mohammad\\IdeaProjects\\Plato_Main_Server\\src\\com\\company\\Clients.txt";
    public static String roomsPath = "C:\\Users\\Mohammad\\IdeaProjects\\Plato_Main_Server\\src\\com\\company\\Rooms.txt";
    public static String randedPath = "C:\\Users\\Mohammad\\IdeaProjects\\Plato_Main_Server\\src\\com\\company\\RankedRooms.txt";
    public static String imagePath = "C:\\Users\\Mohammad\\IdeaProjects\\Plato_Main_Server\\src\\com\\company\\Image.txt";
    //Amin path Files :
    /*
    public static String chatsPath = "C:\\Users\\aminsaveh\\IdeaProjects\\untitled23\\src\\com\\company\\Chats.txt";
    public static String clientsPath = "C:\\Users\\aminsaveh\\IdeaProjects\\untitled23\\src\\com\\company\\Clients.txt";
    public static String roomsPath = "C:\\Users\\aminsaveh\\IdeaProjects\\untitled23\\src\\com\\company\\Rooms.txt";
    public static String randedPath = "C:\\Users\\aminsaveh\\IdeaProjects\\untitled23\\src\\com\\company\\RankedRooms.txt";
    public static String imagePath = " C:\\Users\\aminsaveh\\IdeaProjects\\untitled23\\src\\com\\company\\Image.txt";
     */

    public static void main(String[] args) throws IOException {
        /////////////////
        ServerSocket serverSocket = new ServerSocket(9999);
        //the following codes are used to get the data from files
        File chats_file = new File(Main.chatsPath);
        File clients_file = new File(Main.clientsPath);
        File rooms_file = new File(Main.roomsPath);
        File rankedrooms_file = new File(Main.randedPath);
        File image_file = new File(imagePath);
        if (image_file.length() > 5 ){
            Scanner scanner = new Scanner(image_file);
            while(scanner.hasNext()){
                scanner.next();
                Main.imageLength++;
            }
        }
        image_file = new File(imagePath);
        byte[] image = new byte[Main.imageLength];
        if (image_file.length() >= 5) {
            Scanner scanner = new Scanner(image_file);
            int a = 0;
            while (scanner.hasNext()) {
                image[a++] = (byte) Integer.parseInt(scanner.next());
            }
        }

        if (chats_file.length() > 5) {
            Scanner scanner = new Scanner(chats_file);
            String key = "";
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
//                System.out.println(input);
                if (input.indexOf("->") >= 0) {
                    key = input;
                    Main.chats.put(key, new ArrayList<>());
                } else {
                    String[] outputStrings = input.split("['|']");
                    for (int i = 0; i < outputStrings.length; i++) {
                        outputStrings[i] = outputStrings[i].trim();
                    }
                    for (int i = 0; i < outputStrings.length - 1; i++) {
                        String[] inputStrings = outputStrings[i].split("['^']");
                        for (int j = 0; j < inputStrings.length; j++) {
                            inputStrings[j] = inputStrings[j].trim();
                        }
                        Main.chats.get(key).add(new Message(inputStrings[0], inputStrings[1], inputStrings[2]));
                    }
                }
            }
        }
        if (clients_file.length() > 5) {
            Scanner scanner = new Scanner(clients_file);
            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();
                String[] splitStrings = newLine.split("['^']");
                User user1 = new User();
                user1.username = splitStrings[0].trim();
                user1.password = splitStrings[1].trim();
                user1.Image = image;
                user1.bio = splitStrings[2].trim();
                String[] userScores = splitStrings[4].split("['#']");
                for (int i = 0; i < userScores.length; i++) {
                    if (userScores[i].indexOf("{") >= 0) {
                        StringBuilder str = new StringBuilder(userScores[i]);
                        str.deleteCharAt(str.indexOf("{"));
                        userScores[i] = str.toString();
                    }
                    if (userScores[i].indexOf("}") >= 0) {
                        StringBuilder str = new StringBuilder(userScores[i]);
                        str.deleteCharAt(str.indexOf("}"));
                        userScores[i] = str.toString();
                    }
                    userScores[i] = userScores[i].trim();
                }
                int scores[] = new int[4];
                for (int i = 0; i < scores.length; i++) {
                    scores[i] = Integer.valueOf(userScores[i]);
                }
                user1.game.scores = scores;
                Main.clients.put(user1.username, user1);
            }
        }
        if (clients_file.length() > 5) {
            Scanner scanner = new Scanner(clients_file);
            while (scanner.hasNextLine()) {
                String newLine = scanner.nextLine();
                String[] splitStrings = newLine.split("['^']");
                String[] userFriends = new String[0];
                splitStrings[3] = splitStrings[3].trim();
                if (splitStrings[3].length() > 2) {
                    userFriends = splitStrings[3].split("['#']");
                    for (int i = 0; i < userFriends.length; i++) {
                        if (userFriends[i].indexOf("{") >= 0) {
                            StringBuilder stringBuilder = new StringBuilder(userFriends[i]);
                            stringBuilder.deleteCharAt(stringBuilder.indexOf("{"));
                            userFriends[i] = stringBuilder.toString();
                        }
                        if (userFriends[i].indexOf("}") >= 0) {
                            StringBuilder stringBuilder = new StringBuilder(userFriends[i]);
                            stringBuilder.deleteCharAt(stringBuilder.indexOf("}"));
                            userFriends[i] = stringBuilder.toString();
                        }
                        userFriends[i] = userFriends[i].trim();
                        if (userFriends[i].length() > 0){
                            System.out.println(userFriends[i]);
                            Main.clients.get(splitStrings[0].trim()).friends.add(Main.clients.get(userFriends[i]));
                        }
                    }
                }
            }
        }
        if (rooms_file.length() > 5) {
            Scanner scanner = new Scanner(rooms_file);
            while (scanner.hasNextLine()) {
                String[] strings = scanner.nextLine().split("['^']");
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = strings[i].trim();
                }
                Main.rooms.add(new Room(strings[0], strings[1], strings[2]));
            }
        }
        if (rankedrooms_file.length() > 5) {
            Scanner scanner = new Scanner(rankedrooms_file);
            while (scanner.hasNextLine()) {
                String[] strings = scanner.nextLine().split("['^']");
                for (int i = 0; i < strings.length; i++) {
                    strings[i] = strings[i].trim();
                }
                Main.rankedrooms.add(new Room(strings[0], strings[1], strings[2]));
            }
        }
        while (true) {
            (new Thread(new ClientHandler(serverSocket.accept()))).start();
        }
    }

    //the following vars are used to get players for games
    public static DataOutputStream player1;
    public static DataOutputStream player2;
    public static boolean xoisFinished = false;
    public static boolean dotsisFinished = false;
    public static boolean connectisFinished1 = false;
    public static boolean connectisFinished2 = false;
}

class Message {
    String username;
    String msg;
    String date;

    public Message(String username, String msg) {
        this.username = username;
        this.msg = msg;
        date = new SimpleDateFormat("HH:mm a").format(new Date());
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", msg='" + msg + '\'' +
                ", date=" + date +
                '}';
    }

    public Message(String username, String msg, String date) {
        this.username = username;
        this.msg = msg;
        this.date = date;
    }
}

class Room {
    String player1;
    String player2;
    String game;

    Room(String player1, String player2, String game) {
        this.player1 = player1;
        this.player2 = player2;
        this.game = game;
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    String username;
    String password;
    String finalusername;
    String finalpassword;
    byte[] profileimage;
    String guestusername;
    User user = new User();

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public void run() {
        String clientMessage = "";
        while (true) {
            try {
                clientMessage = dataInputStream.readUTF();
                if (clientMessage.startsWith("Guest")) {
                    int len = dataInputStream.readInt();
                    byte[] image = new byte[len];
                    System.out.println("Uploading....." + " Image Size : " + len / 1024 + "Kb");
                    dataInputStream.readFully(image);
                    System.out.println("Image Uploaded.");
                    user.Image = image;
                    for (int i = 0; i < 100; i++) {
                        if (!Main.clients.containsKey("Guest" + String.valueOf(i))) {
                            user.username = "Guest" + String.valueOf(i);
                            dataOutputStream.writeUTF(user.username);
                            user.password = "123321";
                            user.bio = "Say something...";
                            Main.clients.put("Guest" + i, user);
                            System.out.println(user.username);
                            break;
                        }
                    }
                }
                if (clientMessage.startsWith("Sign Up")) {
                    clientMessage = dataInputStream.readUTF();
                    if (clientMessage.startsWith("Guest:")) {
                        guestusername = clientMessage.substring(6);
                        System.out.println(guestusername);
                        user = Main.clients.get(guestusername);
                        clientMessage = dataInputStream.readUTF();
                    }
                    if (clientMessage.startsWith("Username:")) {
                        String username = clientMessage.substring(9);
                        if (Main.clients.containsKey(username)) {
                            System.out.println("Choosen Username :" + username + " Has Already Taken ");
                            dataOutputStream.writeUTF("Username Has Already Taken");
                        } else {
                            System.out.println("Choosen Username : " + username + " Is Ok");
                            dataOutputStream.writeUTF("OK");
                        }
                    }
                    if (clientMessage.startsWith("FinalUsername:")) {
                        finalusername = clientMessage.substring(14);
                        user.username = finalusername;
                    }
                    if (clientMessage.startsWith("FinalPassword:")) {
                        finalpassword = clientMessage.substring(14);
                        user.password = finalpassword;
                        Main.clients.put(user.username, user);
                        Main.clients.remove(guestusername);
                        System.out.println("User :  " + finalusername + " Password : " + finalpassword + " Conncetd!");
                        user.Image = profileimage;
                    }
                    if (clientMessage.startsWith("Profile")) {
                        int len = dataInputStream.readInt();
                        byte[] image = new byte[len];
                        System.out.println("Uploading....." + " Image Size : " + len / 1024 + "Kb");
                        dataInputStream.readFully(image);
                        System.out.println("Image Uploaded.");
                        user.Image = image;
                    }
//                    break;

                }
//                if (clientMessage.startsWith("Main")) {
//                    String username = dataInputStream.readUTF();
//                    System.out.println("[" + username + "] --- Logged In.");
//                    dataOutputStream.writeInt(Main.clients.get(username).Image.length);
//                    dataOutputStream.write(Main.clients.get(username).Image);
//                    dataOutputStream.flush();
//                    File file = new File(Main.chatsPath);
//                    FileWriter fileWriter = new FileWriter(file);
//                    for (String key : Main.chats.keySet()
//                    ) {
//                        fileWriter.write(key + "\n");
//                        ArrayList<Message> messages = Main.chats.get(key);
//                        for (int i = 0; i < messages.size(); i++) {
//                            fileWriter.write(messages.get(i).username + " ^ " + messages.get(i).msg + " ^ " + messages.get(i).date + " | ");
//                        }
//                        fileWriter.write("\n");
//                    }
//                    fileWriter.flush();
//                    fileWriter.close();
//
//
//                    file = new File(Main.clientsPath);
//                    fileWriter = new FileWriter(file);
//                    for (String key :
//                            Main.clients.keySet()) {
//                        User user = Main.clients.get(key);
//                        String ufs = "{";
//                        if (Main.clients.get(key).friends != null) {
//                            for (int i = 0; i < Main.clients.get(key).friends.size(); i++) {
//                                if (Main.clients.get(key).friends.get(i) != null) {
//                                    ufs += Main.clients.get(key).friends.get(i).username;
//                                    ufs += " # ";
//                                }
//                            }
//                            ufs += "}";
//                        }
//                        String gamesString = "{";
//                        for (int i = 0; i < Main.clients.get(key).game.scores.length; i++) {
//                            gamesString += Main.clients.get(key).game.scores[i] + " # ";
//                        }
//                        gamesString += "}";
//                        fileWriter.write(user.username + " ^ " + user.password + " ^ " + user.bio + " ^ " + ufs + " ^ " + gamesString + "\n");
//                    }
//                    fileWriter.flush();
//                    fileWriter.close();
//
//
//                    file = new File(Main.roomsPath);
//                    fileWriter = new FileWriter(file);
//                    for (Room room :
//                            Main.rooms) {
//                        fileWriter.write(room.player1 + " ^ " + room.player2 + " ^ " + room.game + "\n");
//                    }
//                    fileWriter.flush();
//                    fileWriter.close();
//
//
//                    file = new File(Main.randedPath);
//                    fileWriter = new FileWriter(file);
//                    for (Room room :
//                            Main.rankedrooms) {
//                        fileWriter.write(room.player1 + " ^ " + room.player2 + " ^ " + room.game + "\n");
//                    }
//                    fileWriter.flush();
//                    fileWriter.close();
//                }
                if (clientMessage.startsWith("Main")) {
                    String username = dataInputStream.readUTF();
                    System.out.println("[" + username + "] --- Logged In.");
                    try {
                        dataOutputStream.writeInt(Main.clients.get(username).Image.length);
                        dataOutputStream.write(Main.clients.get(username).Image);
                    }catch (NullPointerException n){
                        dataOutputStream.writeInt(-1);
                    }
                    dataOutputStream.flush();
                    File file = new File(Main.chatsPath);
                    FileWriter fileWriter = new FileWriter(file);
                    for (String key : Main.chats.keySet()
                    ) {
                        fileWriter.write(key + "\n");
                        ArrayList<Message> messages = Main.chats.get(key);
                        for (int i = 0; i < messages.size(); i++) {
                            fileWriter.write(messages.get(i).username + " ^ " + messages.get(i).msg + " ^ " + messages.get(i).date + " | ");
                        }
                        fileWriter.write("\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();


                    file = new File(Main.clientsPath);
                    fileWriter = new FileWriter(file);
                    for (String key :
                            Main.clients.keySet()) {
                        User user = Main.clients.get(key);
                        String ufs = "{";
                        if (Main.clients.get(key).friends != null) {
                            for (int i = 0; i < Main.clients.get(key).friends.size(); i++) {
                                if (Main.clients.get(key).friends.get(i) != null) {
                                    ufs += Main.clients.get(key).friends.get(i).username;
                                    ufs += " # ";
                                }
                            }
                            ufs += "}";
                        }
                        String gamesString = "{";
                        for (int i = 0; i < Main.clients.get(key).game.scores.length; i++) {
                            gamesString += Main.clients.get(key).game.scores[i] + " # ";
                        }
                        gamesString += "}";
                        fileWriter.write(user.username + " ^ " + user.password + " ^ " + user.bio + " ^ " + ufs + " ^ " + gamesString + "\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();


                    file = new File(Main.roomsPath);
                    fileWriter = new FileWriter(file);
                    for (Room room :
                            Main.rooms) {
                        if(room.player2.equals(""))
                            fileWriter.write(room.player1 + " ^ " + room.player2 + " ^ " + room.game + "\n");
                        else{
                            Main.rooms.remove(room);
                        }
                    }
                    fileWriter.flush();
                    fileWriter.close();


                    file = new File(Main.randedPath);
                    fileWriter = new FileWriter(file);
                    for (Room room :
                            Main.rankedrooms) {
                        fileWriter.write(room.player1 + " ^ " + room.player2 + " ^ " + room.game + "\n");
                    }
                    fileWriter.flush();
                    fileWriter.close();
                }
                if (clientMessage.startsWith("GetBio")) {
                    String username = dataInputStream.readUTF();
                    System.out.println("[" + username + "] --- Profile Page");
                    dataOutputStream.writeUTF(Main.clients.get(username).bio);
                    String bio = dataInputStream.readUTF();
                    break;
                }
                if (clientMessage.startsWith("Bio")) {
                    String username = dataInputStream.readUTF();
                    Main.clients.get(username).bio = dataInputStream.readUTF();
                    break;
                }
                if (clientMessage.startsWith("AddFriend")) {
                    String username = dataInputStream.readUTF();
                    String otherSideUsername = dataInputStream.readUTF();
                    User otherSideUser = Main.clients.get(otherSideUsername);
//                    User thisSideUser = Main.clients.get(username);
                    Main.clients.get(username).friends.add(otherSideUser);
//                    Main.clients.get(otherSideUsername).friends.add(thisSideUser);
                    break;
                }
                if (clientMessage.startsWith("DltFriend")){
                    String username = dataInputStream.readUTF();
                    String otherSideUsername = dataInputStream.readUTF();
                    Main.clients.get(otherSideUsername).friends.remove(Main.clients.get(username));
                    System.out.println("deleted");
                }
                if (clientMessage.startsWith("GetFriends")) {
                    String username = dataInputStream.readUTF();
                    if (Main.clients.size() >= 0) {
                        int size = Main.clients.get(username).friends.size();
                        dataOutputStream.writeUTF(String.valueOf(size));
                        System.out.println(size);
                        for (int i = 0; i < size; i++) {
                            dataOutputStream.writeUTF(Main.clients.get(username).friends.get(i).username);
                        }
                    }
                    break;
                }
                if (clientMessage.startsWith("GetImageFreinds")) {
                    String username = dataInputStream.readUTF();
                    dataOutputStream.writeInt(Main.clients.get(username).Image.length);
                    dataOutputStream.write(Main.clients.get(username).Image);
                    dataOutputStream.flush();
                    break;
                }
                if (clientMessage.startsWith("Login")) {
                    clientMessage = dataInputStream.readUTF();
                    if (clientMessage.startsWith("Username:")) {
                        username = clientMessage.substring(9);
                        if (!Main.clients.containsKey(username)) {
                            System.out.println(username);
                            dataOutputStream.writeUTF("Username Doesn't Exist");
                        } else {
                            dataOutputStream.writeUTF("OK");
                        }

                    }
                    if (clientMessage.startsWith("Password:")) {
                        password = clientMessage.substring(9);
                        User user = Main.clients.get(username);
                        if (user != null) {
                            if (!user.password.equals(password)) {
                                dataOutputStream.writeUTF("Password Doesn't Match");
                            } else {
                                dataOutputStream.writeUTF("Ok");
                            }
                        } else {
                            dataOutputStream.writeUTF("Password Doesn't Match");
                        }
                    }
                }
                if (clientMessage.startsWith("SendMSG")) {
                    String username = dataInputStream.readUTF();
                    String otherSide = dataInputStream.readUTF();
                    String msg = dataInputStream.readUTF();
                    Message newMSG = new Message(username, msg);
                    Main.chats.get(username + "->" + otherSide).add(newMSG);
                    Main.chats.get(otherSide + "->" + username).add(newMSG);
                    System.out.println(newMSG.toString());
                    if (Main.user1boolean) {
                        Main.user1.writeUTF(newMSG.username);
                        Main.user1.writeUTF(newMSG.msg);
                        Main.user1.writeUTF(newMSG.date);
                    }
                    if (Main.user2boolean) {
                        Main.user2.writeUTF(newMSG.username);
                        Main.user2.writeUTF(newMSG.msg);
                        Main.user2.writeUTF(newMSG.date);
                    }
                    break;
                }
                if (clientMessage.startsWith("SetSocket")) {
                    if (Main.user1boolean == false) {
                        Main.user1 = dataOutputStream;
                        Main.user1boolean = true;
                    } else {
                        if (Main.user2boolean == false) {
                            Main.user2 = dataOutputStream;
                            Main.user2boolean = true;
                        }
                    }
                }
                if (clientMessage.startsWith("SetFriendSocket")) {
                    if (Main.addFriend1boolean == false) {
                        Main.addFriend1 = dataOutputStream;
                        Main.addFriend1boolean = true;
                    } else {
                        if (Main.addFriend2boolean == false) {
                            Main.addFriend2 = dataOutputStream;
                            Main.addFriend2boolean = true;
                        }
                    }
                    break;
                }
                if (clientMessage.startsWith("SendNotification")) {
                    String toUsername = dataInputStream.readUTF();
                    String fromUsername = dataInputStream.readUTF();
                    if (Main.addFriend1boolean) {
                        Main.addFriend1.writeUTF("##");
                    }
                    if (Main.addFriend2boolean) {
                        Main.addFriend2.writeUTF(fromUsername);
                    }
                    break;
                }
                if (clientMessage.startsWith("GetMSG")) {
                    String usernameToOtherSide = dataInputStream.readUTF();
                    int size;
                    if (Main.chats.containsKey(usernameToOtherSide)) {
                        size = Main.chats.get(usernameToOtherSide).size();
                    } else {
                        size = 0;
                    }
                    dataOutputStream.writeUTF(String.valueOf(size));
                    for (int i = 0; i < size; i++) {
                        dataOutputStream.writeUTF(Main.chats.get(usernameToOtherSide).get(i).username);
                        dataOutputStream.writeUTF(Main.chats.get(usernameToOtherSide).get(i).msg);
                        dataOutputStream.writeUTF(Main.chats.get(usernameToOtherSide).get(i).date);
                    }
                    break;
                }
                if (clientMessage.startsWith("InitChat")) {
                    String username = dataInputStream.readUTF();
                    String otherSideUsername = dataInputStream.readUTF();
                    if (!Main.chats.containsKey(otherSideUsername + "->" + username)) {
                        Main.chats.put(otherSideUsername + "->" + username, new ArrayList<Message>());
                        Main.chats.put(username + "->" + otherSideUsername, new ArrayList<Message>());
                    }
                    break;
                }
                if (clientMessage.startsWith("Xo")) {
                    Main.xoisFinished = false;
                    String mode = dataInputStream.readUTF();
                    String username = dataInputStream.readUTF();
                    System.out.println(username);
                    System.out.println(mode);
                    int counter = 0;
                    boolean isfirst = false;
                    Room room = new Room("", "", "Xo");
                    if (mode.equals("Casual")) {
                        for (int i = 0; i < Main.rooms.size(); i++) {
                            if (Main.rooms.get(i).game.equals("Xo")) {
                                room = Main.rooms.get(i);
                                if (Main.rooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }

                    } else {
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            if (Main.rankedrooms.get(i).game.equals("Xo")) {
                                room = Main.rankedrooms.get(i);
                                if (Main.rankedrooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rankedrooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }


                    }

                    int player1i;
                    int player1j;
                    int player2i;
                    int player2j;
                    String message;
                    while (true) {
                        if (isfirst) {
                            player1i = dataInputStream.readInt();
                            player1j = dataInputStream.readInt();
                            System.out.println("[Ali] -> XO : X " + player1i + " " + player1j);
                            com.company.Main.player2.writeInt(player1i);
                            com.company.Main.player2.writeInt(player1j);
                            message = dataInputStream.readUTF();
                            if (!message.equals("continue")) {
                                System.out.println(message);
                                Main.xoisFinished = true;
                                break;
                            }

                        }
                        if (!isfirst) {
                            player2i = dataInputStream.readInt();
                            player2j = dataInputStream.readInt();
                            System.out.println("[Amin] -> XO : O " + player2i + " " + player2j);
                            com.company.Main.player1.writeInt(player2i);
                            com.company.Main.player1.writeInt(player2j);
                            message = dataInputStream.readUTF();
                            if (!message.equals("continue")) {
                                System.out.println(message);
                                break;
                            }
                            if (Main.xoisFinished) {
                                System.out.println(message);
                                break;
                            }

                        }
                        if (mode.equals("Ranked")) {
                            if (counter == 0) {
                                for (int i = 0; i < Main.rankedrooms.size(); i++) {
                                    if (room.game.equals(Main.rankedrooms.get(i).game)) {
                                        if (room.player1.equals(Main.rankedrooms.get(i).player1)) {
                                            if (room.player2.equals(Main.rankedrooms.get(i).player2)) {
                                                Main.rankedrooms.remove(i);
                                            }
                                        }
                                    }
                                }
                                counter++;
                            }
                        }

                    }
                    if (Main.xoisFinished = true) {
                        break;
                    }

                }
                if (clientMessage.startsWith("Guessword")) {
                    String mode = dataInputStream.readUTF();
                    String username = dataInputStream.readUTF();
                    System.out.println(mode);
                    System.out.println(username);
                    boolean isfirst = false;
                    String wordplayer1;
                    String wordplayer2;
                    String endplayer1;
                    String endplayer2;
                    Room room = new Room("", "", "Guessword");
                    if (mode.equals("Casual")) {
                        for (int i = 0; i < Main.rooms.size(); i++) {
                            if (Main.rooms.get(i).game.equals("Guessword")) {
                                room = Main.rooms.get(i);
                                if (Main.rooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            if (Main.rankedrooms.get(i).game.equals("Guessword")) {
                                room = Main.rankedrooms.get(i);
                                if (Main.rankedrooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rankedrooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    }
                    if (isfirst) {
                        wordplayer1 = dataInputStream.readUTF();
                        System.out.println(wordplayer1);
                        Main.player2.writeUTF(wordplayer1);
                        endplayer1 = dataInputStream.readUTF();
                        System.out.println(endplayer1);
                        Main.player2.writeUTF(endplayer1);
                    }
                    if (!isfirst) {
                        wordplayer2 = dataInputStream.readUTF();
                        System.out.println(wordplayer2);
                        Main.player1.writeUTF(wordplayer2);
                        endplayer2 = dataInputStream.readUTF();
                        System.out.println(endplayer2);
                        Main.player1.writeUTF(endplayer2);
                    }
                    if (mode.equals("Ranked")) {
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            if (room.game.equals(Main.rankedrooms.get(i).game)) {
                                if (room.player1.equals(Main.rankedrooms.get(i).player1)) {
                                    if (room.player2.equals(Main.rankedrooms.get(i).player2)) {
                                        Main.rankedrooms.remove(i);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                if (clientMessage.startsWith("DotsBoxes")) {
                    Main.dotsisFinished = false;
                    String mode = dataInputStream.readUTF();
                    String username = dataInputStream.readUTF();
                    System.out.println(username);
                    System.out.println(mode);
                    boolean isfirst = false;
                    Room room = new Room("", "", "Dots&Boxes");
                    if (mode.equals("Casual")) {
                        for (int i = 0; i < Main.rooms.size(); i++) {
                            if (Main.rooms.get(i).game.equals("Dots&Boxes")) {
                                room = Main.rooms.get(i);
                                if (Main.rooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            if (Main.rankedrooms.get(i).game.equals("Dots&Boxes")) {
                                room = Main.rankedrooms.get(i);
                                if (Main.rankedrooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rankedrooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    }
                    int loc1;
                    int loc2;
                    String message1;
                    String message2;
                    while (true) {
                        if (isfirst) {
                            loc1 = dataInputStream.readInt();
                            if (loc1 == 999) {
                                System.out.println(loc1);
                                Main.dotsisFinished = true;
                                break;
                            }
                            message1 = dataInputStream.readUTF();
                            System.out.println(message1);
                            Main.player2.writeUTF(message1);
                            Main.player2.writeInt(loc1);
                        } else {
                            loc2 = dataInputStream.readInt();
                            if (loc2 == 999) {
                                System.out.println(loc2);
                                Main.dotsisFinished = true;
                                break;
                            }
                            message2 = dataInputStream.readUTF();
                            System.out.println(message2);
                            Main.player1.writeUTF(message2);
                            Main.player1.writeInt(loc2);
                        }
                        if (mode.equals("Ranked")) {
                            for (int i = 0; i < Main.rankedrooms.size(); i++) {
                                if (room.game.equals(Main.rankedrooms.get(i).game)) {
                                    if (room.player1.equals(Main.rankedrooms.get(i).player1)) {
                                        if (room.player2.equals(Main.rankedrooms.get(i).player2)) {
                                            Main.rankedrooms.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Main.dotsisFinished) {
                        break;
                    }
                }
                if (clientMessage.startsWith("Connect4")) {
                    Main.connectisFinished1 = false;
                    Main.connectisFinished2 = false;
                    String mode = dataInputStream.readUTF();
                    String username = dataInputStream.readUTF();
                    System.out.println(username);
                    System.out.println(mode);
                    boolean isfirst = false;
                    Room room = new Room("", "", "Connect4");
                    if (mode.equals("Casual")) {
                        for (int i = 0; i < Main.rooms.size(); i++) {
                            if (Main.rooms.get(i).game.equals("Connect4")) {
                                room = Main.rooms.get(i);
                                if (Main.rooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            if (Main.rankedrooms.get(i).game.equals("Connect4")) {
                                room = Main.rankedrooms.get(i);
                                if (Main.rankedrooms.get(i).player1.equals(username)) {
                                    isfirst = true;
                                    com.company.Main.player1 = dataOutputStream;
                                    Main.player1.writeUTF("player1");
                                } else if (Main.rankedrooms.get(i).player2.equals(username)) {
                                    com.company.Main.player2 = dataOutputStream;
                                    Main.player2.writeUTF("player2");
                                }
                            }
                        }
                    }
                    int loc1;
                    int loc2;
                    while (true) {
                        if (isfirst) {
                            loc1 = dataInputStream.readInt();
                            if (loc1 == 999) {
                                System.out.println(loc1);
                                Main.connectisFinished1 = true;
                                break;
                            }
                            Main.player2.writeInt(loc1);
                        } else {
                            loc2 = dataInputStream.readInt();
                            if (loc2 == 999) {
                                System.out.println(loc2);
                                Main.connectisFinished2 = true;
                                break;
                            }
                            Main.player1.writeInt(loc2);
                        }
                        if (mode.equals("Ranked")) {
                            for (int i = 0; i < Main.rankedrooms.size(); i++) {
                                if (room.game.equals(Main.rankedrooms.get(i).game)) {
                                    if (room.player1.equals(Main.rankedrooms.get(i).player1)) {
                                        if (room.player2.equals(Main.rankedrooms.get(i).player2)) {
                                            Main.rankedrooms.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (Main.connectisFinished1 && Main.connectisFinished2) {
                        break;
                    }
                }
                if (clientMessage.startsWith("GetRooms")) {
                    String game = dataInputStream.readUTF();
                    for (int i = 0; i < Main.rooms.size(); i++) {
                        if (game.equals(Main.rooms.get(i).game)) {
                            if (Main.rooms.get(i).player2.equals("")) {
                                dataOutputStream.writeUTF(Main.rooms.get(i).player1);
                            }

                        }
                    }
                    dataOutputStream.writeUTF("finish");
                    while (true) {
                        String message = dataInputStream.readUTF();
                        if (message.equals("new")) {
                            String player1 = dataInputStream.readUTF();
                            String game1 = dataInputStream.readUTF();
                            System.out.println(player1);
                            System.out.println(game1);
                            Room room = new Room(player1, "", game1);
                            Main.rooms.add(room);
                        }
                        if (message.equals("joinroom")) {
                            String game2 = dataInputStream.readUTF();
                            String player2 = dataInputStream.readUTF();
                            String player1 = dataInputStream.readUTF();
                            for (int i = 0; i < Main.rooms.size(); i++) {
                                if (Main.rooms.get(i).game.equals(game2)) {
                                    if (Main.rooms.get(i).player1.equals(player1)) {
                                        if (Main.rooms.get(i).player2.equals("")) {
                                            dataOutputStream.writeUTF("ok");
                                            Main.rooms.get(i).player2 = player2;

                                        } else {
                                            dataOutputStream.writeUTF("full");
                                            Main.rooms.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                if (clientMessage.equals("Scores")) {
                    String player = dataInputStream.readUTF();
                    int xoScore = dataInputStream.readInt();
                    int wordguessScore = dataInputStream.readInt();
                    int dotsboxesScore = dataInputStream.readInt();
                    int connect3Score = dataInputStream.readInt();
                    for (int i = 0; i < Main.clients.size(); i++) {
                        if (Main.clients.containsKey(player)) {
                            Main.clients.get(player).game.scores[0] += xoScore;
                            Main.clients.get(player).game.scores[1] += wordguessScore;
                            Main.clients.get(player).game.scores[2] += dotsboxesScore;
                            Main.clients.get(player).game.scores[3] += connect3Score;
                            for (int j = 0; j < 4; j++) {
                                System.out.print(Main.clients.get(player).game.scores[j]);
                            }
                            break;
                        }
                    }


                }
                if (clientMessage.equals("Ranking")) {
                    String game = dataInputStream.readUTF();
                    int index = 0;
                    HashMap<String, Integer> tmp = new HashMap<>();
                    if (game.equals("Xo")) {
                        index = 0;
                    }
                    if (game.equals("Guessword")) {
                        index = 1;
                    }
                    if (game.equals("Dots&Boxes")) {
                        index = 2;
                    }
                    if (game.equals("Connect4")) {
                        index = 3;
                    }
                    for (Map.Entry<String, User> entry : Main.clients.entrySet()) {
                        tmp.put(entry.getKey(), entry.getValue().game.scores[index]);
                    }
                    for (Map.Entry<String, Integer> entry : tmp.entrySet()) {
                        String username = entry.getKey();
                        int score = entry.getValue();
                        for (Map.Entry<String, Integer> entry1 : tmp.entrySet()) {
                            if (entry1.getValue() > score) {
                                username = entry1.getKey();
                                score = entry1.getValue();
                            }
                        }
                        dataOutputStream.writeUTF(username);
                        User user = Main.clients.get(username);
                        dataOutputStream.writeInt(user.Image.length);
                        dataOutputStream.write(user.Image);
                        dataOutputStream.writeInt(user.game.scores[index]);
                        tmp.replace(username, -1);
                    }
                    dataOutputStream.writeUTF("finish");
                }
                if (clientMessage.equals("Ranked")) {
                    String game = dataInputStream.readUTF();
                    String player = dataInputStream.readUTF();
                    System.out.println(game);
                    System.out.println(player);
                    boolean breakloop = false;
                    if (Main.rankedrooms.size() == 0) {
                        System.out.println("000000000");
                        Room room = new Room(player, "", game);
                        Main.rankedrooms.add(room);
                        while (true) {
                            for (int i = 0; i < Main.rankedrooms.size(); i++) {
                                if (Main.rankedrooms.get(i).player1.equals(player)) {
                                    if (!Main.rankedrooms.get(i).player2.equals("")) {
                                        System.out.println("000000001");
                                        dataOutputStream.writeUTF("found");
                                        breakloop = true;
                                        break;
                                    }
                                }
                            }
                            if (breakloop == true) {
                                break;
                            }
                        }
                        if(breakloop){
                            break;
                        }
                    }else  if(Main.rankedrooms.size()>0 ) {
                        System.out.println("000000004");
                        System.out.println(Main.rankedrooms.size());
                        for (int i = 0; i < Main.rankedrooms.size(); i++) {
                            System.out.println("DDDDD");
                            if (Main.rankedrooms.get(i).game.equals(game)) {
                                if (Main.rankedrooms.get(i).player2.equals("")) {
                                    if (!Main.rankedrooms.get(i).player1.equals(player)) {
                                        Main.rankedrooms.get(i).player2 = player;
                                        System.out.println("000000002");
                                        dataOutputStream.writeUTF("found");
                                        breakloop=true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(breakloop){
                            break;
                        }
                        Room room = new Room(player, "", game);
                        Main.rankedrooms.add(room);
                        System.out.println("llllll");
                        while (true) {
                            for (int j = 0; j < Main.rankedrooms.size(); j++) {
                                if (Main.rankedrooms.get(j).player1.equals(player)) {
                                    if (!Main.rankedrooms.get(j).player2.equals("")) {
                                        System.out.println("000000003");
                                        dataOutputStream.writeUTF("found");
                                        breakloop = true;
                                        break;
                                    }
                                }
                            }
                            if (breakloop) {
                                break;
                            }
                        }

                    }
                }
                if (clientMessage.equals("GetScores")) {
                    String username = dataInputStream.readUTF();
                    System.out.println(username);
                    dataOutputStream.writeInt(Main.clients.get(username).game.scores[0]);
                    dataOutputStream.writeInt(Main.clients.get(username).game.scores[1]);
                    dataOutputStream.writeInt(Main.clients.get(username).game.scores[2]);
                    dataOutputStream.writeInt(Main.clients.get(username).game.scores[3]);
                }


            } catch (IOException e) {

            }
        }
    }
}

/*
On 21 July 2020 :
Mohammad Reza Eskini :
its 4:35 AM and we are almost done
this was a really hard project for both of us and we are happy that it is done
we did this for ap final project
I decided to write sth here for 20 year later
I have put the Amin's part blank (at this moment) to be filled by him later
#sag_to_AP
Amin Saveh :

 */