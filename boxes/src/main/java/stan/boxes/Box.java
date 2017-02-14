package stan.boxes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import stan.boxes.json.JSONParser;
import stan.boxes.json.JSONWriter;
import stan.boxes.json.ParseException;

public class Box<DATA>
{
    private final ORM<DATA> orm;
    private final String fullPath;
    private final JSONParser parser = new JSONParser();

    public Box(ORM<DATA> o, String fp)
    {
        orm = o;
        fullPath = fp;
        createNewFile();
    }
    private void createNewFile()
    {
        File boxFile = new File(fullPath);
        if(!boxFile.exists())
        {
            try
            {
                boxFile.createNewFile();
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException e)
            {
            }
            catch(Exception e)
            {
            }
        } 
    }

    public List<DATA> getAll()
    {
        try
        {
            String data = read(fullPath);
            Map map = (Map)parser.parse(data);
            List convert = (List)map.get("list");
            List<DATA> list = new ArrayList<DATA>(convert.size());
            for(int i=0; i<convert.size(); i++)
            {
                list.add(orm.read((Map)convert.get(i)));
            }
            return list;
        }
        catch(ParseException e)
        {
            try
            {
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException ex)
            {
            }
        }
        catch(IOException e)
        {
        }
        catch(Exception e)
        {
        }
        return new ArrayList<DATA>();
    }

    public List<DATA> get(Query<DATA> query)
    {
        try
        {
            String data = read(fullPath);
            Map map = (Map)parser.parse(data);
            List convert = (List)map.get("list");
            List<DATA> list = new ArrayList<DATA>(convert.size());
            for(int i=0; i<convert.size(); i++)
            {
                DATA tmp = orm.read((Map)convert.get(i));
                if(query.query(tmp))
                {
                    list.add(tmp); 
                }
            }
            return list;
        }
        catch(ParseException e)
        {
            try
            {
                Map map = new HashMap();
                map.put("list", new Object[]{});
                map.put("date", System.currentTimeMillis());
                write(fullPath, JSONWriter.write(map));
            }
            catch(IOException ex)
            {
            }
        }
        catch(IOException e)
        {
        }
        catch(Exception e)
        {
        }
        return new ArrayList<DATA>();
    }
    public List<DATA> get(Query<DATA> query, Comparator<DATA> comparator)
    {
        List<DATA> list = get(query);
        Collections.sort(list, comparator);
        return list;
    }
    public List<DATA> get(Query<DATA> query, Range range)
    {
        List<DATA> list = get(query);
        return list.subList(range.getStart(), range.getStart() + range.getCount());
    }
    public List<DATA> get(Query<DATA> query, Comparator<DATA> comparator, Range range)
    {
        List<DATA> list = get(query, comparator);
        return list.subList(range.getStart(), range.getStart() + range.getCount());
    }
    public void add(DATA... datas)
    {
        if(datas == null || datas.length == 0)
        {
            return;
        }
        List<DATA> list = getAll();
        for(DATA d : datas)
        {
            list.add(d);
        }
        save(list);
    }
    public void replace(Query<DATA> query, DATA data)
    {
        List<DATA> list = getAll();
        for(int i=0; i<list.size(); i++)
        {
            if(query.query(list.get(i)))
            {
                list.set(i, data);
                break;
            }
        }
        save(list);
    }
    public void removeFirst(Query<DATA> query)
    {
        List<DATA> list = getAll();
        for(int i=0; i<list.size(); i++)
        {
            if(query.query(list.get(i)))
            {
                list.remove(i);
                break;
            }
        }
        save(list);
    }
    public void removeAll(Query<DATA> query)
    {
        List<DATA> list = getAll();
        int i=0;
        while(i<list.size())
        {
            if(query.query(list.get(i)))
            {
                list.remove(i);
            }
            else
            {
                i++;
            }
        }
        save(list);
    }
    public void clear()
    {
        Map map = new HashMap();
        map.put("list", new Object[]{});
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException ex)
        {
        }
        catch(Exception e)
        {
        }
    }
    private void save(List<DATA> list)
    {
        List convert = new ArrayList(list.size());
        for(int i=0; i<list.size(); i++)
        {
            convert.add(orm.write(list.get(i)));
        }
        Map map = new HashMap();
        map.put("list", convert);
        map.put("date", System.currentTimeMillis());
        try
        {
            write(fullPath, JSONWriter.write(map));
        }
        catch(IOException ex)
        {
        }
        catch(Exception e)
        {
        }
    }

    private synchronized void write(String fp, String data) throws IOException
    {
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(fp);
            fw.write(data);
        }
        finally
        {
            fw.close();
        }
    }
    private synchronized String read(String fp) throws IOException
    {
        FileReader fr = null;
        try
        {
            fr = new FileReader(fp);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while(line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        }
        finally
        {
            fr.close();
        }
    }
}