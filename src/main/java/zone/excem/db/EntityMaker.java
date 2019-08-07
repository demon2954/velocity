package zone.excem.db;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import zone.excem.bean.EntityProperty;
import zone.excem.util.StringUtil;

public class EntityMaker {
	public static List<EntityProperty> makeEntityProperty(String tableName) {
		List<EntityProperty> list = new ArrayList<EntityProperty>();
		List<String> columnNames = DatabaseUtil.getColumnNames(tableName);
		List<String> columnTypes = DatabaseUtil.getColumnTypes(tableName);
		List<String> columnComments = DatabaseUtil.getColumnComments(tableName);
		List<String> idNames = DatabaseUtil.getIdName(tableName);

		int size = columnNames.size();
		for (int i = 0; i < size; i++) {
			String columnName = columnNames.get(i);
			String columnType = columnTypes.get(i);
			String columnComment = columnComments.get(i);
			EntityProperty one = new EntityProperty();
			one.setJavaName(StringUtil.underlineToCamel(columnName, false));
			one.setDbName(columnName);
			one.setJavaLongType(getJavaLongType(columnType));
			one.setJavaShortType(getJavaShortType(columnType));
			one.setDbType(getDbType(columnType));
			one.setComment(StringUtil.isBlank(columnComment) ? "" : columnComment);
			if (CollectionUtils.isNotEmpty(idNames)) {
				for (String id : idNames) {
					if (columnName.equals(id)) {
						one.setPrimaryKey(true);
						break;
					}
				}
			}
			list.add(one);
		}
		return list;
	}

	private static String getDbType(String columnType) {
		switch (columnType) {
		case "INT":
			return "INTEGER";
		case "DATETIME":
			return "TIMESTAMP";
		default:
			return columnType;
		}
	}

	private static String getJavaShortType(String columnType) {
		switch (columnType) {
		case "INT":
			return "Integer";
		case "BIGINT":
			return "Long";
		case "VARCHAR":
			return "String";
		case "TIMESTAMP":
		case "DATETIME":
			return "Date";
		case "TINYINT":
			return "Byte";
		case "DOUBLE":
			return "Double";
		default:
			return columnType;
		}
	}
	
	private static String getJavaLongType(String columnType) {
		switch (columnType) {
		case "INT":
			return "java.lang.Integer";
		case "BIGINT":
			return "java.lang.Long";
		case "VARCHAR":
			return "java.lang.String";
		case "TIMESTAMP":
		case "DATETIME":
			return "java.util.Date";
		case "TINYINT":
			return "java.lang.Byte";
		case "DOUBLE":
			return "java.lang.Double";
		default:
			return columnType;
		}
	}
}
