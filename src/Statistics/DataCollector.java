package Statistics;

import SimulationObjects.Regiment;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DataCollector {

    public HashMap<Regiment, LinkedList<Integer>> regimentMap = new HashMap<>();

    public void add(Regiment regiment){regimentMap.putIfAbsent(regiment,new LinkedList<>());}

    public void registerState(Regiment regiment){
        regimentMap.get(regiment).add(regiment.armyUnitList.size());
    }

    public void exportDataToCSV()
    {
        StringBuffer stringBuffer = new StringBuffer();
        List<Regiment> keysList = new LinkedList<>(regimentMap.keySet());

        // CSV Header
        for (Regiment regiment: keysList)
            stringBuffer.append(regiment).append(",");
        stringBuffer.replace(stringBuffer.length()-1,stringBuffer.length(),"\n");

        List<List<Integer>> valuesList = new LinkedList<>();
        int maxSize = 0;
        for (Regiment regiment: keysList) {
            valuesList.add(regimentMap.get(regiment));
            maxSize = Math.max(regimentMap.get(regiment).size(), maxSize);
        }

        // CSV Rows
        for(int i=0; i<maxSize; i++)
        {
            for(List<Integer> list:valuesList)
            {
                try{
                    stringBuffer.append(list.get(i)).append(',');
                }catch (IndexOutOfBoundsException e)
                {
                    stringBuffer.append("0,");
                }
            }
            stringBuffer.replace(stringBuffer.length()-1,stringBuffer.length(), "\n");
        }

        // Saving CSV
        try {
            String fileName = new Date().toString().replaceAll(" ","_").replaceAll(":","-")+".csv";
            String filePath = Paths.get("").toAbsolutePath().toString()+"\\"+fileName;
            PrintWriter printWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
            printWriter.write(stringBuffer.toString());
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
