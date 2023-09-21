package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
            
            /*
            TO DO *****
            pull data from CSV
            Use collumn headings as keys
            convert each row into a json object and store in a jsonarray
            */
        
            // INSERT YOUR CODE HERE
            Integer num = null;
            //System.out.println("CSV STRING \n" + csvString);
            
            //System.out.println("\n\n\n");
            
            CSVReader csvReader = new CSVReader(new StringReader(csvString));
            List<String[]> csv = csvReader.readAll();
            Iterator<String[]> iterator = csv.iterator();
            
            
            
            JsonArray prodNums = new JsonArray();
            JsonArray colHeadings = new JsonArray();
            JsonArray data = new JsonArray();
            
            if (iterator.hasNext()){
                String[] headings = iterator.next(); //get the headings
                colHeadings.addAll(Arrays.asList(headings));
                while (iterator.hasNext()) {
                    String[] csvRecord = iterator.next();  //Pull a line
                    //JsonObject jsonRecord = new JsonObject();
                    //LinkedHashMap<String, String> jsonRecord = new LinkedHashMap<>(); //Create a hashmap
                    JsonArray tempData = new JsonArray();
                    prodNums.add(csvRecord[0]);
                    
                    for (int i=1;i<headings.length;++i){ //CSVreader did not clean this, or rather it kept it as String data, didn't have enough time to learn how to properly convert
                        if (i == 3 || i == 2){
                            num = Integer.valueOf(csvRecord[i]);
                            tempData.add(num);
                        }else{
                            tempData.add(csvRecord[i]);
                        }
                        //jsonRecord.put(headings[i].toLowerCase(),csvRecord[i]);
                        
                    }
                    data.add(tempData); //add hashmap to array
                }
                
            }
            
            //System.err.println(prodNums);
            //System.err.println(colHeadings);
            //System.err.println(data);
            
            LinkedHashMap<String, JsonArray> jsonRecord = new LinkedHashMap<>();
            jsonRecord.put("ProdNums",prodNums);
            jsonRecord.put("ColHeadings", colHeadings);
            jsonRecord.put("Data", data);
            
            result = Jsoner.serialize(jsonRecord);
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
            // INSERT YOUR CODE HERE
            //System.out.println("\nOriginal JString\n" + jsonString);
            JsonObject jStringObj = Jsoner.deserialize(jsonString, new JsonObject()); //I have no idea how to decompose this further
            //System.out.println("\nDs'd Jstring\n" + jsonObject);
            //System.out.println(jsonObject.get("ProdNums"));
            
            
            JsonArray prodnums = (JsonArray) jStringObj.get("ProdNums");
            JsonArray colheadings = (JsonArray) jStringObj.get("ColHeadings");
            JsonArray data = (JsonArray) jStringObj.get("Data");
            //System.out.println(prodnums);
            
            List<String[]> csvData = new ArrayList<>();
            List<String> line = new ArrayList<>();
            String[] dataLine = null;
            String[] array = null;
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer,',','"','\\',"\n");
            
            
            for (int i=0;i<colheadings.size();i++){
                line.add((String)colheadings.get(i));
            }
            array = line.toArray(new String[0]);
            csvWriter.writeNext(array);
            
            //csvData.add(line.toArray(new String[0]));
            
            for (int i=0;i<prodnums.size();i++){
                line.clear();
                //dataLine.clear();
                line.add((String)prodnums.get(i));
                //System.out.println(line);
                dataLine = (data.get(i).toString().split(",",-1));
                dataLine[0] = dataLine[0].replaceAll("\\[", "");
                dataLine[5] = dataLine[5].replaceAll("\\]", "");
                System.out.println(dataLine[0]);
                System.out.println("Bing");
                for (int j=0;j<6;j++){
                    if (j == 2 && i < 9){
                        line.add("0" + dataLine[j].trim());
                    } else{
                        line.add(dataLine[j].trim());
                    }
                    System.out.println("oof");
                    //System.out.println(dataLine.get(0).getClass());
                }
                System.out.println("Bong");
                System.out.println(line);
                array = line.toArray(new String[0]);
                line.clear();
                
                
                csvWriter.writeNext(array);
                
            }
            
            //System.out.println(data.get(0).toString());
            //System.out.println(line);
            
            
            
            
            
            
            
            
            System.out.println(writer.toString());
            
            result = writer.toString();
           
            
            
            //JsonObject json = Jsoner.deserialize(jsonString, new JsonObject());
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
