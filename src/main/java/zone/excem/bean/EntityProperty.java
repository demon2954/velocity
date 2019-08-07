package zone.excem.bean;

import lombok.Data;

@Data
public class EntityProperty {
	private String javaName;
	private String javaShortType;
	private String javaLongType;
	private String dbName;
	private String dbType;
	private String comment;
	private boolean primaryKey = false;
}
