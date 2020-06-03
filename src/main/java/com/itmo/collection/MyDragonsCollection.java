package com.itmo.collection;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyDragonsCollection implements Serializable {

    private HashSet<Dragon> dragons;
    private final Date creationDate;

    /**
     * Конструктор для пустой коллекции драконов
     */
    public MyDragonsCollection(){
        creationDate = new Date();
        dragons = new HashSet<>();
    }

    /**
     * конструктор, который использую в чтении из файла
     * @param dragons драконы, которые будут в коллекции
     */
    public MyDragonsCollection(HashSet<Dragon> dragons){
        creationDate = new Date();
        this.dragons = dragons;
    }

    public String show(){
        StringBuilder builder = new StringBuilder();
        if(dragons.size() == 0) return "Коллекция пуста. Добавьте дракончиков.";
        TreeSet<Dragon> treeSet = new TreeSet<>(dragons);
        treeSet.forEach(d ->{
                builder.append("----------\n").append(d.toString()).append("\n");
        });
        return builder.toString();
    }
    public String clear(){
        dragons.clear();
        return "Коллекция очищена";
    }
    public String add(Dragon dragon){
        Set<Long> setIds = dragons.stream().map(Dragon::getId).collect(Collectors.toSet());
        //генерация id

        for(long i = 0; i<Long.MAX_VALUE;i++){
            if(!setIds.contains(i)){
                dragon.setId(i);
                this.dragons.add(dragon);
                return "Дракон добавлен";
            }
        }

        //Stream.iterate(0, i -> i++).limit(Long.MAX_VALUE).

        return "Дракон не добавлен потому что не удалось сгенерировать для него id.";
    }
    public String addIfMax(Dragon dragon){
        if(findMaxValue()<dragon.getValue()){
            return add(dragon);
        }
        return "Не добавлен, т.к. не больший";
    }
    public String addIfMin(Dragon dragon){
        if(dragons.stream().anyMatch(d -> (d.getValue()<dragon.getValue()))){
            return "Не добавлен, т.к. не меньший";
        }else{
            return add(dragon);
        }
    }


    /**
     * удалить из коллекции все элементы, меньшие, чем заданный
     * @param dragon дракон с которым будут сравниваться все элементы коллекции
     */
    public String removeLower(Dragon dragon) {
        StringBuilder builder = new StringBuilder();
        dragons.stream().filter(d -> d.getValue() < dragon.getValue())
                .forEach(dr -> {
                    builder.append("Удален дракон с id ").append(dr.getId()).append("\n");
                    dragons.remove(dr);
                });
        if(builder.length()==0) return "Нет драконов меньше чем заданный";
        return builder.toString();
    }
    /**
     * фильтрует коллекцию, оставляет только тех, чьи имена начинаются с name
     * @param name является началом имени драконов которых нужно получить
     * @return сет драконов в отфильтрованном порядке
     */
    public HashSet<Dragon> filterStartsWithName(String name){
        Set<Dragon> dr = dragons.stream()
                .filter(d -> d.getName().trim().startsWith(name)).collect(Collectors.toSet());
        return (HashSet<Dragon>) dr;
    }

    /**
     * простой метод для вывода коллекции в обратном порядке
     */
    public String printDescending(){
        StringBuilder builder = new StringBuilder();
        dragons.stream().sorted((o1, o2) -> (int) (o2.getValue()-o1.getValue()))
                .forEach(d -> builder.append(d.getName())
                        .append(" with value ").append(d.getValue()).append("\n"));
        return builder.toString();
    }
    public boolean removeById(long id){
        Dragon dragon = findById(id);
        if(dragon != null){
            this.dragons.remove(dragon);
            return true;
        }
        return false;
    }

    public float findMaxValue(){
        return (dragons.size()==0 ? 0 :
                dragons.stream()
                .max(Comparator.comparing(Dragon::getValue))
                        .get().getValue());
    }

    public Dragon findById(long id) {
        return dragons.stream().filter(d -> d.getId() == id).findAny().orElse(null);
    }

    public String printFieldAscendingWingspan(){
        StringBuilder builder = new StringBuilder();
        dragons.stream().map(Dragon::getValue)
                .sorted()
                .forEach(v -> builder.append(v).append(" "));
        return builder.toString();
    }

    public String getCollectionInfo(){
        return "Тип коллекции: com.itmo.Dragon\nДата инициализации: " + creationDate +
        "\nКоличество элементов: " + dragons.size();
    }

    public HashSet<Dragon> getDragons() {
        return dragons;
    }
}