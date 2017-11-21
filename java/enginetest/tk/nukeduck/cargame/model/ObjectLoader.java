package tk.nukeduck.cargame.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ObjectLoader {
	public static Model loadModel(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			
			ArrayList<String> lines = new ArrayList<String>();
			String line = "";
			
			while((line = reader.readLine()) != null) {
				if(!(line.startsWith("#") || line.isEmpty())) lines.add(line);
			}
			reader.close();
			
			int[][] faces = new int[lines.size()][15];
			int y = 0;
			for(String l : lines) {
				String[] params = l.replace(" ", "").split(",");
				for(int i = 0; i < 15; i++) faces[y][i] = Integer.parseInt(params[i]);
				y += 1;
			}
			
			return new Model(faces);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}