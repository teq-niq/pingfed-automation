package admin.beans;


import com.example.pingfedadmin.model.*;
import com.example.pingfedadmin.model.SourceTypeIdKey.TypeEnum;

public class AccessTokenMappingAttribute {
	private final String name;
	private final String id;
	public String getId() {
		return id;
	}
	private final String value;
	private final SourceTypeIdKey.TypeEnum type;
	public AccessTokenMappingAttribute(String id, String name, TypeEnum type, String value) {
		super();
		this.id=id;
		this.name = name;
		this.value = value;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public SourceTypeIdKey.TypeEnum getType() {
		return type;
	}

}
