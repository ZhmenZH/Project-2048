package danyl.Game;


import java.io.*;
import java.util.ArrayList;

public class Leaders {

    private static Leaders leadersBoard;
    private String filepath;
    private String highScores;

    //All time leaders;
    private ArrayList<Integer> bestScores;
    private ArrayList<Long> bestTimes;

    private Leaders()
    {
        try {
            filepath = new File("").getAbsolutePath();
            System.out.println(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        highScores = "Scores";

        bestScores = new ArrayList<Integer>();
        bestTimes = new ArrayList<Long>();
    }

    public static Leaders getInstance()
    {
        if(leadersBoard == null)
        {
            leadersBoard = new Leaders();
        }
        return leadersBoard;
    }

    public void addScore(int score)
    {
        for(int i = 0; i<bestScores.size(); i++)
        {
            if(score >= bestScores.get(i))
            {
                bestScores.add(i,score);
                bestScores.remove(bestScores.size() - 1);
                return;
            }
        }
    }

    public void addTime(long millis)
    {
        boolean fileNotInitialized = true;

        for (int i = 0; i < bestTimes.size(); i++)
        {
            if (bestTimes.get(i) != 0)
            {
                fileNotInitialized = false;
            }
        }

        if (fileNotInitialized)
        {
            bestTimes.add(0, millis);
            bestTimes.remove(bestTimes.size() - 1);
            return;
        }

        for(int i = 0; i < bestTimes.size(); i++)
        {
            if(millis <= bestTimes.get(i))
            {
                bestTimes.add(i,millis);
                bestTimes.remove(bestTimes.size() - 1);
            }
        }
    }

    public void loadScores()
    {
        try {
        File file = new File(filepath,highScores);
        if(!file.isFile())
        {
            createSaveData();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        bestScores.clear();
        bestTimes.clear();

        String[] scores = reader.readLine().split(",");
        String[] times = reader.readLine().split(",");

        for(int i = 0; i < scores.length; i++)
        {
            bestScores.add(Integer.parseInt(scores[i]));
        }
        for(int i = 0; i < times.length; i++)
        {
            bestTimes.add(Long.parseLong(times[i]));
        }

        reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveScores()
    {
        FileWriter output = null;
        try {
        File file = new File(filepath,highScores);
        output = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(output);

        writer.write(bestScores.get(0) + "," + bestScores.get(1) + "," + bestScores.get(2) + "," + bestScores.get(3) + "," + bestScores.get(4));
        writer.newLine();
        writer.write(bestTimes.get(0) + "," + bestTimes.get(1) + "," + bestTimes.get(2) + "," + bestTimes.get(3) + "," + bestTimes.get(4));
        writer.newLine();
        writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSaveData()
    {
        FileWriter output = null;
        try {
            File file = new File(filepath,highScores);
            output = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);

            writer.write("0,0,0,0,0");
            writer.newLine();
            writer.write(Integer.MAX_VALUE + "," + Integer.MAX_VALUE + "," + Integer.MAX_VALUE + "," + Integer.MAX_VALUE + "," + Integer.MAX_VALUE);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighScore()
    {
        return bestScores.get(0);
    }

    public long getTheFastestTime()
    {
        return bestTimes.get(0);
    }

    public ArrayList<Integer> getBestScores() {
        return bestScores;
    }

    public ArrayList<Long> getBestTimes() {
        return bestTimes;
    }
}