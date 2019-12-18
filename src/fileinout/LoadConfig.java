package fileinout;

import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;
import objects.Parameters;

public class LoadConfig {
	public static Parameters loadFromFile(String fileName){
		try{
			Gson gson = new Gson();
			Reader reader = new FileReader(fileName);
			Parameters parameters = gson.fromJson(reader, Parameters.class);
			return parameters;
		}catch(Exception ex){
			System.out.println("load Parameters error");
			ex.printStackTrace();
		}
		return null;
	}

}
