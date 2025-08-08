package com.fishdan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;

public class HouseTableParser {

    public static void main(String[] args) throws IOException {
        File input = new File("/mnt/c/data/house.gov.html");
        Document doc = Jsoup.parse(input, "UTF-8");

        try (PrintWriter writer = new PrintWriter("house_members.csv")) {
            // Updated CSV Header
            writer.println("State,District,Name,URL,Party,Office Room,Phone,Committee Assignment");

            Elements tables = doc.select("table[id^=housegov_reps_by_state-block]");

            for (Element table : tables) {
                String state = table.selectFirst("caption").text().trim();

                for (Element row : table.select("tbody tr")) {
                    Elements cells = row.select("td");

                    if (cells.size() >= 6) {
                        String district = cells.get(0).text().trim();

                        // Get Name and URL from anchor
                        Element nameCell = cells.get(1);
                        String name = nameCell.text().trim();
                        String url = "";
                        Element link = nameCell.selectFirst("a[href]");
                        if (link != null) {
                            url = link.absUrl("href"); // or link.attr("href") if you want raw
                        }

                        String party = cells.get(2).text().trim();
                        String office = cells.get(3).text().trim();
                        String phone = cells.get(4).text().trim();
                        String committees = cells.get(5).text().trim().replace("|", ";");

                        writer.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                                state, district, name, url, party, office, phone, committees);
                    }
                }
            }
        }

        System.out.println("Parsing complete. Output saved to house_members.csv");
    }
}
