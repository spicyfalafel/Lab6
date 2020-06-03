package com.itmo.collection;

import java.io.Serializable;
import java.util.Date;

/**
 * The type com.itmo.Dragon.
 */
public class Dragon implements Comparable<Dragon>, Serializable {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private float wingspan; //Значение поля должно быть больше 0
    private DragonType type; //Поле может быть null
    private DragonCharacter character; //Поле не может быть null
    private Person killer; //Поле может быть null

    private static long idInc = 0;


    /**
     * Конструктор со всеми не автогенерируемыми полями
     *
     * @param name        - имя дракона
     * @param coordinates - координаты
     * @param age         - возраст дракона. value = age * wingspan
     * @param wingspan    - размах крыла
     * @param type        - тип дракона
     * @param character   - характер дракона
     * @param killer      - убийца дракона
     */
    public Dragon(String name, Coordinates coordinates, int age, float wingspan,
                  DragonType type, DragonCharacter character, Person killer){
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.wingspan = wingspan;
        this.type = type;
        this.character = character;
        this.killer = killer;
        creationDate = new Date();
    }


    public Dragon(String name, Coordinates coordinates, Date creationDate, int age, float wingspan,
                  DragonType type, DragonCharacter character, Person killer){
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.wingspan = wingspan;
        this.type = type;
        this.character = character;
        this.killer = killer;
    }

    /**
     *
     * @param id          the id
     * @param name        the name
     * @param coordinates the coordinates
     * @param age         the age
     * @param wingspan    the wingspan
     * @param type        the type
     * @param character   the character
     * @param killer      the killer
     */
    public Dragon(Long id, String name, Coordinates coordinates, int age, float wingspan, DragonType type,
                  DragonCharacter character, Person killer){
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.wingspan = wingspan;
        this.type = type;
        this.character = character;
        this.killer = killer;
        creationDate = new Date();
        this.id = id;
    }

    /**
     * Get value float.
     *
     * @return the float
     */
    public float getValue(){
        return this.wingspan*this.age;
    }

    /**
     * Instantiates a new com.itmo.Dragon.
     *
     * @param name        the name
     * @param coordinates the coordinates
     * @param age         the age
     * @param wingspan    the wingspan
     * @param character   the character
     */
    public Dragon(String name, Coordinates coordinates, int age, float wingspan,
                  DragonCharacter character){
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.wingspan = wingspan;
        this.character = character;
        creationDate = new Date();
    }

    /**
     * Set killer.
     *
     * @param killer the killer
     */
    public void setKiller(Person killer){
        this.killer = killer;
    }

    /**
     * Set type.
     *
     * @param type the type
     */
    public void setType(DragonType type){
        this.type = type;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Gets creation date.
     *
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets wingspan.
     *
     * @return the wingspan
     */
    public float getWingspan() {
        return wingspan;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public DragonType getType() {
        return type;
    }

    /**
     * Gets character.
     *
     * @return the character
     */
    public DragonCharacter getCharacter() {
        return character;
    }

    /**
     * Gets killer.
     *
     * @return the killer
     */
    public Person getKiller() {
        return killer;
    }
    public void setCreationDate(Date creationDate){
        this.creationDate = creationDate;
    }

    /**
     * костыль для команды updateById
     *
     * @param id id дракона
     */
    public void setId(long id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "id: " + this.id + "\n" +
                "name: " + this.name + "\n" +
                "coordinates: " + coordinates.getX().toString() +
                ", " + coordinates.getY() + "\n" +
                "creationDate: " + this.creationDate.toString() + "\n" +
                "age: " + this.age + "\n" +
                "wingspan: " + this.wingspan + "\n" +
                "type: " + ((type == null) ? "null" : this.type.toString()) + "\n" +
                "character: " + this.character.toString() + "\n" +
                "killer: " + ((this.killer == null) ? "null" : this.killer.toString());
    }

    /**
     * драконы сравниваются по value = age * wingspan
     * @param dragon сравниваемый дракон
     * @return
     */


    @Override
    public int compareTo(Dragon dragon) {
        return Float.compare(getValue(), dragon.getValue());
    }
}