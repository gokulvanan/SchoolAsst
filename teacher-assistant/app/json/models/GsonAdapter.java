package json.models;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import models.TimeTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GsonAdapter implements JsonSerializer<CustomJSONModel> {

	private String label= null;
	public GsonAdapter(String label)
	{
		this.label=label;
	}
	@Override
	public JsonElement serialize(CustomJSONModel obj, Type type,	JsonSerializationContext jsc)  {

		Class<?> clazz = obj.getClass();
		String[] flds =obj.getFields(label);
		System.out.println(Arrays.toString(flds));
		JsonObject jsonObject = new JsonObject();
		for(String f : flds)
		{
			try
			{
				Field fld=clazz.getDeclaredField(f);
				fld.setAccessible(true);
				Object o = fld.get(obj);
				System.out.println(o.getClass());
				if(! (o instanceof String || o instanceof Integer || o instanceof Double
						|| o instanceof Float ||o instanceof Boolean) )
				{
					GsonBuilder gsonBuilder = new GsonBuilder();
			    	Gson gson = gsonBuilder.registerTypeAdapter(o.getClass(), new GsonAdapter(label)).create();
			    	jsonObject.addProperty(f, gson.toJson(o));
				}
				else {
					jsonObject.addProperty(f, o+"");
				}
			}catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return jsonObject;
	}

	/*@Override
    public JsonElement serialize(TimeTable obj, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message_id", obj.school);
        jsonObject.addProperty("message", message.getMessage());
        jsonObject.addProperty("user", message.getUsers().getUsername());
        jsonObject.addProperty("date", message.getDate().toString());
        return jsonObject;      
    }*/
}
