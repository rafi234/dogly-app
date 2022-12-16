package pk.rafi234.dogly.meetings.scrapler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class WepScrapper {
    private final static String[] urls = new String[] {
            "https://psipark.pl/wybiegi-kategorie/malopolskie/",
            "https://psipark.pl/wybiegi-kategorie/kujawsko-pomorskie/",
            "https://psipark.pl/wybiegi-kategorie/lodzkie/",
            "https://psipark.pl/wybiegi-kategorie/mazowieckie/",
            "https://psipark.pl/wybiegi-kategorie/pomorskie/",
            "https://psipark.pl/wybiegi-kategorie/slaskie/",
            "https://psipark.pl/wybiegi-kategorie/wielkopolskie/",
            "https://psipark.pl/wybiegi-kategorie/zachodniopomorskie/",
            "https://psipark.pl/wybiegi-kategorie/dolnoslaskie/",
            "https://psipark.pl/wybiegi-kategorie/swietokrzyskie/",
            "https://psipark.pl/wybiegi-kategorie/warminsko-mazurskie/",
            "https://psipark.pl/wybiegi-kategorie/lubelskie/",
            "https://psipark.pl/wybiegi-kategorie/lubuskie/",
            "https://psipark.pl/wybiegi-kategorie/podlaskie/",
            "https://psipark.pl/wybiegi-kategorie/podkarpackie/",
            "https://psipark.pl/wybiegi-kategorie/opolskie/"

    };

    private final static String PLANNED = "[PLANOWANY]";


    public static List<DogPark> getAllDogParks() {
        try {
            List<DogPark> dogParks = new ArrayList<>();
            for (String url : urls) {
                final Document htmlWeb = Jsoup.connect(url).get();
                final String voivodeship = prepareVoivodeship(url.split("/")[4]);

                for (Element element : htmlWeb.select("div.content-padder div")) {
                    final Elements row = element.select(".title");
                    final String fullName = row.text();
                    final String link = row.attr("href");
                    final String imgUrl = element.select(".attachment-post-thumbnail.size-post-thumbnail.wp-post-image").attr("src");
                    if (!fullName.equals("")) {
                        final DogPark newRow = prepareString(fullName);
                        newRow.setUrl(link);
                        newRow.setId(UUID.randomUUID());
                        newRow.setVoivodeship(voivodeship);
                        newRow.setImgUrl(imgUrl);
                        if (!dogParks.contains(newRow) && !fullName.contains(PLANNED)) {
                            dogParks.add(newRow);
                        }
                    }
                }
            }
            return dogParks;

        } catch (IOException e) {
            throw new ScrapperException("Scrapping the data was not possible!");
        }
    }

    private static String prepareVoivodeship(String voivodeship) {
        return switch (voivodeship) {
            case "malopolskie" -> "małopolskie";
            case "slaskie" -> "śląskie";
            case "lodzkie" -> "łódzkie";
            case "dolnoslaskie" -> "dolnoślązkie";
            case "swietokrzyskie" -> "świętokrzyskie";
            case "warminsko-mazurskie" -> "warmińsko-mazurskie";
            default -> voivodeship;
        };
    }

    private static DogPark prepareString(String row) {
        String[] preparedString = row.split(" ");
        DogPark dogPark = new DogPark();
        if (preparedString[0].equals("Koło")) {
            dogPark.setType("Wybieg dla psów");
            dogPark.setCity(preparedString[0]);
            dogPark.setLocation(preparedString[1] + " " + preparedString[2] + " " + preparedString[3]);
        } else if(preparedString[1].equals("toaleta")) {
            dogPark.setType(preparedString[0] + " " + preparedString[1]);
            dogPark.setCity(preparedString[2]);
            dogPark.setLocation(preparedString[3] + "  " + preparedString[4]);
        } else if (preparedString[3].equals("toaleta")) {
            dogPark.setType(preparedString[0] + " " + preparedString[1] + " " + preparedString[2]);
            dogPark.setCity("Warszawa");
            dogPark.setLocation(prepareLocation(preparedString).replace("dla psów ", ""));
        }
        else {
             dogPark.setType(preparedString[0] + " " + preparedString[1] + " " + preparedString[2]);
            dogPark.setCity(prepareCity(preparedString));
            dogPark.setLocation(prepareLocation(preparedString));
        }
        return dogPark;
    }

    private static String prepareCity(String[] preparedString) {
        if (isTwoPartCity(preparedString))
            return preparedString[3] + " " + preparedString[4];
        return preparedString[3];
    }

    private static String prepareLocation(String[] preparedString) {
        StringBuilder location = new StringBuilder();
        int i;
        if (isTwoPartCity(preparedString)) i = 5; else i = 4;
        for (; i < preparedString.length; ++i) {
            location.append(preparedString[i]).append(" ");
        }
        return location.toString().trim();
    }

    private static boolean isTwoPartCity(String[] preparedString) {
        return preparedString[3].equals("Nowy")
                || preparedString[3].equals("Gorzów")
                || preparedString[3].equals("Zielona")
                || preparedString[3].equals("Brzeg")
                || preparedString[3].equals("Czeski")
                || preparedString[3].equals("Dąbrowa")
                || preparedString[3].equals("Siemianowice")
                || preparedString[3].equals("Tarnowskie")
                || preparedString[3].equals("Jelenia");
    }
}
