package com.itmo.app;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import java.io.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class XmlStaff {
    // мне кажется парсеры должны писаться не так...

    /**
     *
     * @param xmlFile файл который надо парсить
     * @return сет драконов
     * @throws FileNotFoundException если файл не нашелся
     */
    public static HashSet<Dragon> fromXmlToDragonList(File xmlFile) throws FileNotFoundException, JDOMParseException, JDOMException  {
        SAXBuilder builder = new SAXBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
        HashSet<Dragon> res = new HashSet<Dragon>();
        System.out.println("Parsing FILE: "+ xmlFile.getAbsolutePath());
        try {
            Document document = (Document) builder.build(reader);
            // rootNode == collection
            Element rootNode = document.getRootElement();
            List<Element> list = rootNode.getChildren("dragon");
            for (Element currDragon : list) {
                String name = currDragon.getChildText("name");
                Long id = Long.parseLong(currDragon.getAttributeValue("id"));
                Element coordinates = currDragon.getChild("coordinates");
                int x = Integer.parseInt(coordinates
                        .getChildText("x"));
                int y = Integer.parseInt(coordinates
                        .getChildText("y"));
                Coordinates coord = new Coordinates(x, y);

                Date dated;
                try{
                    Element date = currDragon.getChild("creationDate");
                    int dragonYear = Integer.parseInt(date.getChildText("year"));
                    int dragonMonth = Integer.parseInt(date.getChildText("month"));
                    int dragonDay = Integer.parseInt(date.getChildText("day"));
                    Calendar cal = Calendar.getInstance();
                    cal.set(dragonYear, dragonMonth, dragonDay);
                    dated = cal.getTime();
                }catch(NullPointerException | NumberFormatException e){
                    dated = new Date();
                }

                int age = Integer.parseInt(currDragon.getChildText("age"));

                float wingspan = Float.parseFloat(currDragon.getChildText("wingspan"));
                String drtype = currDragon.getChildText("type");
                DragonType type =
                        (drtype.equals("null")) ? null : DragonType.valueOf(drtype);

                DragonCharacter character = DragonCharacter.valueOf(currDragon.getChildText("character"));

                Element killer = currDragon.getChild("killer");
                String killerName = killer.getChildText("name");
                Color killerColor = Color.valueOf(killer.getChildText("hairColor"));
                Country killerNation = Country.valueOf(killer.getChildText("nationality"));
                Element loc = killer.getChild("location");
                int xx = Integer.parseInt(loc.getChildText("x"));
                long yy = Long.parseLong(loc.getChildText("y"));
                float zz = Float.parseFloat(loc.getChildText("z"));
                String locName = loc.getChildText("name");
                Location location = new Location(xx, yy, zz, locName);
                Element birthday = killer.getChild("birthday");
                int year = Integer.parseInt(birthday.getChildText("year"));
                Month month = Month.valueOf(birthday.getChildText("month"));
                int day = Integer.parseInt(birthday.getChildText("day"));
                LocalDateTime birthday1 = LocalDateTime.of(year, month, day, 0, 0);
                Person killa = new Person(killerName, birthday1, killerColor, killerNation, location);

                res.add(new Dragon(id, name, coord, age, wingspan, type, character, killa));
                reader.close();
            }
        }catch (IOException e){
            System.out.println("ошибка при парсинге файла");
        }catch (NumberFormatException | NullPointerException e){
            System.out.println("ошибка при парсинге.");
        }
        return res;
    }

    /**
     * метод для записи в xml файл коллекции
     * использую библиотеку JDOM2
     * @param collection
     * @param resFileName имя файла в который записать коллекцию
     * @throws FileNotFoundException
     */
    public static void writeCollectionToFile(HashSet<Dragon> collection, String resFileName) throws FileNotFoundException {
        try{
            Document doc = new Document();
            Element root = new Element("collection");
            doc.setRootElement(root);
            for(Dragon d : collection){
                Element currDragon = new Element("dragon");
                //id
                currDragon.setAttribute("id", String.valueOf(d.getId()));
                //name
                currDragon.addContent(new Element("name").addContent(d.getName()));
                //coord
                Element coords = new Element("coordinates")
                        .addContent(new Element("x").addContent(String.valueOf(d.getCoordinates().getX())))
                        .addContent(new Element("y").addContent(String.valueOf(d.getCoordinates().getY())));
                currDragon.addContent(coords);
                //creationDate
                currDragon.addContent(new Element("creationDate").addContent(d.getCreationDate().toString()));
                //age
                currDragon.addContent(new Element("age").addContent(String.valueOf(d.getAge())));
                //wingspan
                currDragon.addContent(new Element("wingspan").addContent(String.valueOf(d.getWingspan())));
                //character
                currDragon.addContent(new Element("character").addContent(String.valueOf(d.getCharacter())));
                //type
                currDragon.addContent(new Element("type").addContent(String.valueOf(d.getType())));

                //killer
                Person k = d.getKiller();
                if(k==null){
                    k = new Person("", LocalDateTime.now(), Color.BROWN, Country.RUSSIA, new Location(0, 0L, 0f, ""));
                }
                Element birthday = new Element("birthday");
                birthday.addContent(new Element("year").addContent(String.valueOf(k.getBirthday().getYear())))
                        .addContent(new Element("month").addContent(k.getBirthday().getMonth().toString()))
                        .addContent(new Element("day").addContent(String.valueOf(k.getBirthday().getDayOfMonth())));
                Element location = new Element("location")
                        .addContent(new Element("x").addContent(String.valueOf(k.getLocation().getX())))
                        .addContent(new Element("y").addContent(String.valueOf(k.getLocation().getY())))
                        .addContent(new Element("z").addContent(String.valueOf(k.getLocation().getZ())))
                        .addContent(new Element("name").addContent(String.valueOf(k.getLocation().getName())));
                Element killer = new Element("killer")
                        .addContent(new Element("name").addContent(k.getName()))
                        .addContent(birthday)
                        .addContent(new Element("hairColor").addContent(k.getHairColor().toString()))
                        .addContent(new Element("nationality").addContent(k.getNationality().toString()))
                        .addContent(location);

                currDragon.addContent(killer);
                root.addContent(currDragon);
            }
            // красивый формат с переводами на новую строку и отступами
            Format fmt = Format.getPrettyFormat();
            XMLOutputter outputter = new XMLOutputter(fmt);
            outputter.output(doc, new PrintWriter(new File(resFileName)));
        }catch (IOException e) {
            System.out.println("капут в xmlStaff");
        }
    }
}