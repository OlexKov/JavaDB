package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String URL ="jdbc:postgresql://ep-winter-tree-a5shuxk7.us-east-2.aws.neon.tech:5432/koval123db";
    private static final String USER ="koval123db_owner";
    private static final String PASSWORD ="m1Kk5QsfyMTj";

    public static void main(String[] args) throws Exception {
        boolean exit = false;
        try(PhoneDB phoneDB = new PhoneDB(USER,PASSWORD,URL)){
            while (!exit){
                int index = getMenuItemIndex();
                switch (index){
                    case 1:
                    {
                        ArrayList<Abonent> abonents = phoneDB.getAllAbonents();
                        if(!abonents.isEmpty()){
                            System.out.println("-= Список абонентів =-");
                            for(Abonent abonent:  abonents){
                                System.out.println(abonent.toString());
                            }
                        }
                        else{
                            System.out.println("Абоненти відсутні в базі даних");
                        }
                    }
                        break;
                    case 2:
                    {
                        System.out.println("-= Створення нового абонента =-");
                       int res =  phoneDB.addAbonent(
                               getString("Введіть ім'я абонента"),
                               getString("Введіть телефон абонента"),
                               getString("Введіть електронну пошту абонента"),
                               getString("Введіть адрес абонента"));
                       if(res != 0){
                           System.out.println("Абонент успішно доданий до бази даних");
                       }
                       else{
                           System.out.println("Абонент не був доданий до бази даних");
                       }
                    }
                        break;
                    case 3:
                    {
                        System.out.println("-= Видалення абонента =-");
                        int id = getInt("Введіть ID абонента",1,Integer.MAX_VALUE);
                        int res = phoneDB.deleteAdonent(id);
                        if(res != 0){
                            System.out.println("Абонент успішно видалений з бази даних");
                        }
                        else{
                            System.out.println("Абонент не був видалений з бази даних");
                        }
                    }
                        break;
                    case 4:
                        String name = getString("Введіть ім'я авбонента ");
                        ArrayList<Abonent> abonents = phoneDB.getAbonentsLikeName(name);
                        if(!abonents.isEmpty()){
                            System.out.println("-= Список абонентів =-");
                            for(Abonent abonent:  abonents){
                                System.out.println(abonent.toString());
                            }
                        }
                        else{
                            System.out.println("Відсутні абоненти з таким іменем");
                        }
                        break;
                    case 5:
                        int id = getInt("Введіть ID авбонента ",1,Integer.MAX_VALUE);
                        Abonent abonent = phoneDB.getAbonentById(id);
                        if(abonent != null){
                           System.out.println(abonent.toString());
                        }
                        else{
                            System.out.println("Абонента з таким ID не знайдено");
                        }
                        break;
                    case 6:
                        String phone = getString("Введіть телефон авбонента ");
                        abonent = phoneDB.getAbonentByPhone(phone);
                        if(abonent != null){
                             System.out.println(abonent.toString());
                        }
                        else{
                            System.out.println("Абонента з таким телефоном не знайдено");
                        }
                        break;
                    case 7:
                        exit = true;
                        break;
                    default:
                        throw new  Exception("Invalid menu item index");
                }
                if(index != 7)
                    waitAnyKey();
            }
        }
        catch(Exception ex){
            System.out.println("Error !!! "+ex.getMessage());
        }
    }

    public static int getMenuItemIndex(){
        return getInt("""
                -=  Телефонний довідник  =-
                 [1] Список всіх абонентів
                 [2] Додати абонента
                 [3] Видалити абонента
                 [4] Знайти абонента по імені
                 [5] Знайти абонента по ID
                 [6] Знайти абонента по телефону
                 [7] Завершити
            """,1,7);
    };

    public static void waitAnyKey(){
       try{System.in.read();}
       catch(Exception ex){}
    }

    public static int getInt(String title,int min,int max){
        int value = 0;
        Scanner cin = new Scanner(System.in);
        boolean validInput = false;
        while (!validInput) {
        System.out.print(title);
        System.out.print("--> ");
       try{
           value = cin.nextInt();
           if(value >= min && value <= max){
               validInput = true;
           }
           else{
               System.out.println("Значення має бути в межах від " + min + " до " + max);
               waitAnyKey();
           }
       }
       catch (Exception x){
           System.out.println("Не вірно введене значення");
           cin.next();
           waitAnyKey();
       }
        }
       return value;
    }

    public static String getString(String title){
        String value = "";
        Scanner cin = new Scanner(System.in);
        System.out.print(title);
        System.out.print("--> ");
        value = cin.next();
        return value;
    }
}