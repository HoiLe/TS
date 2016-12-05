
public class misc {

	//I_x_y = f_x_D2;
	//1----
	Object[] keys =  f_x_D2.keySet().toArray();
	Object[] values =   f_x_D2.values().toArray();
		
	for (int i=0; i<values.length; i++)
		for (int j=0; j<values.length; j++)
		{
			if(Double.parseDouble(values[i].toString())>Double.parseDouble(values[j].toString()))
			{
				Double temp = Double.parseDouble(values[i].toString());
				values[i]= values[j];
				values[j]=temp;
				
				String s = (String)keys[i];
				keys[i]=keys[j];
				keys[j]=s;					
			}
		}
	
	//2----
	Object[] keys =  I_x_y.keySet().toArray();
	Object[] values =   I_x_y.values().toArray();
		
	for (int i=0; i<values.length; i++)
		for (int j=0; j<values.length; j++)
		{
			if((Double)values[i]>(Double)values[j])
			{
				Double temp = (Double)values[i];
				values[i]= values[j];
				values[j]=temp;
				
				String s = (String)keys[i];
				keys[i]=keys[j];
				keys[j]=s;					
			}
		}
}
