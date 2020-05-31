package com.itmo.commands;

import com.itmo.collection.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;

/**
 * класс для ввода полей элемента.
 */
public class FieldsScanner {
    private Scanner sc;

    /**
     * Instantiates a new Input helper.
     *
     * @param sc the sc
     */
    public FieldsScanner(Scanner sc){
        this.sc = sc;
    }

    /**
     * @param cheVvodit че вводить?
     * @return введённая пользователем строка (может быть пустой)
     */
    private String scanLine(String cheVvodit){
        System.out.println("введите " + cheVvodit);
        return sc.nextLine().trim();
    }
    private String scanLine(){
        return sc.nextLine().trim();
    }

    /**
     * ПРИВАТНЫЙ МЕТОД ДЛЯ НЕПУСТОЙ СТРОКИ
     * @param cheVvodit че вводить?
     * @return введенная пользователем строка (не пустая)
     */
    private String scanNotEmptyLine(String cheVvodit){
        String res = scanLine(cheVvodit);
        while(res.trim().isEmpty()) {
            System.out.println("Строка не должна быть пустой или состоять только из пробелов.");
            System.out.println("Введите " + cheVvodit + " заново.");
            res = sc.nextLine();
        }
        return res.trim();
    }

    /**
     * метод для ввода аргументов-строк. Все Стринговые аргументы в лабе не могут быть пустыми.
     * Имена то есть.
     *
     * @param cheVvodit че вводить?
     * @return введенное пользователем, скорее всего, имя
     */
    public String scanStringArg(String cheVvodit){
        String str = scanLine(cheVvodit);
        while(str==null || str.equals("")){
            System.out.println("Не может быть пустой. Введите " + cheVvodit + " еще раз.");
            str = sc.nextLine();
        }
        return str;
    }

    /**
     * метод для сканирования любого Enum. проверяет, является ли введенная
     * пользователем строка элементом enum'а, который передается во втором аргументе.
     *
     * @param canBeNull может ли быть Enum пустым?
     * @param enumType  тип перечисления
     * @return enum
     */
    public Enum<?> scanEnum(boolean canBeNull, Class<? extends Enum> enumType){
        /*
        не могу вывести доступные значение Enum'а прям тут, потому что...
        Обратите внимание, что ни метод valueOf(), ни метод values() не определен в классе java.lang.Enum.
        Вместо этого они автоматически добавляются компилятором на этапе компиляции enum-класса.
         */
        while(true) {
            String str = scanLine();
            try {
                if (str.equals("") && canBeNull) return null;
                else if (str.equals("")){
                    throw new NullPointerException();
                }
                return Enum.valueOf(enumType, str);
            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Пожалуйста, введите одно из значений enum'а.");
            }
        }
    }

    /**
     * метод для сканирования аргумента который должен быть integer
     *
     * @param cheVvodit    че вводить?
     * @param positiveOnly должно ли число быть только положительным?
     * @return число int
     */
    public int scanInteger(String cheVvodit, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(cheVvodit);
            int res;
            try{
                res = Integer.parseInt(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("необходимо ввести число большее нуля");
                }else{
                    return res;
                }
            }catch (Exception e){
                System.out.println("введите целое число");
            }
        }
    }

    /**
     * метод для сканирования аргумента который должен быть float
     *
     * @param cheVvodit    че вводить?
     * @param positiveOnly должно ли число быть только положительным?
     * @return число float
     */
    public float scanFloat(String cheVvodit, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(cheVvodit);
            float res;
            try{
                res = Float.parseFloat(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("необходимо ввести число большее нуля");
                }else{
                    return res;
                }
            }catch (Exception e){
                System.out.println("введите число");
            }
        }
    }

    /**
     * метод для сканирования аргумента который должен быть long
     *
     * @param cheVvodit    че вводить?
     * @param positiveOnly должно ли число быть только положительным?
     * @return число long
     */
    public long scanLong(String cheVvodit, boolean positiveOnly){
        while(true) {
            String input = scanNotEmptyLine(cheVvodit);
            long res;
            try{
                res = Long.parseLong(input);
                if(positiveOnly && (res<=0)){
                    System.out.println("необходимо ввести число большее нуля");
                }else{
                    return res;
                }
            }catch (Exception e){
                System.out.println("введите число");
            }
        }
    }


    /**
     * Scan local date time no null local date time.
     *
     * @param cheVvodit the che vvodit
     * @return the local date time
     */
    public LocalDateTime scanLocalDateTimeNoNull(String cheVvodit){
        System.out.println(cheVvodit);
        int god = scanInteger("год", true);
        System.out.println("Доступные значения Month: ");
        Month mon;
        for(Month t : Month.values()){
            System.out.println(t + " ");
        }
        while(true){
            String str = scanNotEmptyLine(cheVvodit);
            try{
                mon = Month.valueOf(str);
                int day = scanInteger("день", true);
                return LocalDateTime.of(god, mon, day, 0, 0);
            }catch(IllegalArgumentException|NullPointerException e){
                System.out.println("введите название месяца правильно");
            }catch (DateTimeException e){
                System.out.println("Число не то для месяца. еще раз");
            }
        }
    }

    /**
     * Scan location location.
     *
     * @param cheVvodit the che vvodit
     * @return the location
     */
    public Location scanLocation(String cheVvodit){
        System.out.println("введите " + cheVvodit);
        int x = scanInteger("координата Х места", false);
        long y = scanLong("координата Y места", false);
        float z = scanFloat("координата Z места", false);
        String locName = scanStringArg("название места");
        return new Location(x, y, z, locName);
    }

    /**
     * сканирует всего дракона. проверяет правильность ввода полей. учитывает, какие поля
     * могут быть null, а какие поля-числа больше нуля
     *
     * @return дракон dragon
     */
    public Dragon scanDragon(){
        String name = scanStringArg("имя дракона");
        System.out.println("Координаты.");
        int x = scanInteger("Х", false);
        int y = scanInteger("Y", false);
        Coordinates coordinates = new Coordinates(x, y);
        int age = scanInteger("возраст дракона", true);
        float wingspan = scanFloat("размах крыльев (это число)", true);
        System.out.println("Введите тип дракона. Доступные типы: ");
        for(DragonType t : DragonType.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        DragonType type = (DragonType) scanEnum( true, DragonType.class);
        System.out.println("Введите характер дракона. Доступные типы: ");
        for(DragonCharacter t : DragonCharacter.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        DragonCharacter character = (DragonCharacter) scanEnum(false, DragonCharacter.class);
        Person killer = scanPerson("убийца дракона");
        return new Dragon(name, coordinates, age,
                wingspan, type, character, killer);
    }

    /**
     * сканирует всего Person. проверяет правильность ввода полей. учитывает, какие поля
     * могут быть null, а какие поля-числа больше нуля
     *
     * @param cheVvodit че вводить?
     * @return человiк person
     */
    public Person scanPerson(String cheVvodit){
        System.out.println("введите " + cheVvodit);
        String name = scanLine("имя");
        if(name.equals("")) return null; // Person может быть пустым
        LocalDateTime birthday = scanLocalDateTimeNoNull("дата рождения");
        System.out.println("Введите цвет волос. Доступные типы: ");
        for(Color t: Color.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        Color hairColor = (Color) scanEnum( true, Color.class);
        System.out.println("Введите национальность. Доступные типы: ");
        for(Country t : Country.values()){
            System.out.print(t + " ");
        }
        System.out.println();
        Country country = (Country) scanEnum( true, Country.class);
        Location loc = scanLocation("локацию");
        return new Person(name, birthday, hairColor, country, loc);
    }
}