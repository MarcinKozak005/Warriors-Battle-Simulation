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

    public HashMap<Regiment, LinkedList<Double>> regimentSizeMap = new HashMap<>();
    public HashMap<Regiment, LinkedList<Double>> regimentHpMap = new HashMap<>();

    public void add(Regiment regiment){
        regimentSizeMap.putIfAbsent(regiment,new LinkedList<>());
        regimentHpMap.putIfAbsent(regiment,new LinkedList<>());
    }


    public void registerState(Regiment regiment){
        regimentSizeMap.get(regiment).add((double) regiment.armyUnitList.size());
        regimentHpMap.get(regiment).add(
                regiment.armyUnitList.stream()
                        .mapToDouble(armyUnit -> armyUnit.hp)
                        .average()
                        .orElse(0.0)
        );
    }


    private String processData(HashMap<Regiment,LinkedList<Double>> map)
    {
        StringBuffer stringBuffer = new StringBuffer();
        List<Regiment> keysList = new LinkedList<>(map.keySet());

        // CSV Header
        for (Regiment regiment: keysList)
            stringBuffer.append(regiment).append(",");
        stringBuffer.replace(stringBuffer.length()-1,stringBuffer.length(),"\n");

        List<List<Double>> valuesList = new LinkedList<>();
        int maxSize = 0;
        for (Regiment regiment: keysList) {
            valuesList.add(map.get(regiment));
            maxSize = Math.max(map.get(regiment).size(), maxSize);
        }

        // CSV Rows
        for(int i=0; i<maxSize; i++)
        {
            for(List<Double> list:valuesList)
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

        return stringBuffer.toString();
    }

    private String createCSVFilePath(String nameExtension)
    {
        String properNameExtension = "("+nameExtension.replaceAll("\\W", "")+")";
        String fileName = new Date().toString().replaceAll(" ","_").replaceAll(":","-")+properNameExtension+".csv";
        return Paths.get("").toAbsolutePath().toString()+"\\"+fileName;
    }


    private void saveDataToCSVFile(String data, String filePath){
        try {
            PrintWriter printWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
            printWriter.write(data);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportDataToCSV()
    {

        String dataRegimentNumber = processData(regimentSizeMap);
        String numberNameExtension = "Number";
        String numberFilePath = createCSVFilePath(numberNameExtension);
        saveDataToCSVFile(dataRegimentNumber,numberFilePath);

        String dataRegimentAverageHp = processData(regimentHpMap);
        String hpNameExtension = "HP";
        String hpFilePath = createCSVFilePath(hpNameExtension);
        saveDataToCSVFile(dataRegimentAverageHp,hpFilePath);


        ScatterPlot.generateScatterPlot(numberFilePath,numberNameExtension);
        ScatterPlot.generateScatterPlot(hpFilePath,hpNameExtension);
    }
}
