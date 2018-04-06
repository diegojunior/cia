
package br.com.totvs.cia.infra.enums;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PersistentEnumType<T extends Enum & PersistentEnum> implements UserType, ParameterizedType {
	
	private Class<T> enumClass;
	
	private String defaultValue;

	@Override
	public int[] sqlTypes() {
		return new int[] {Types.VARCHAR};
	}

	@Override
	public Class<T> returnedClass() {
		return enumClass;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x == y;
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}
	
	@Override
	public void setParameterValues(Properties parameters) {
		
		String enumClassName = parameters.getProperty("enumClass");
		
		try {
		
			enumClass = (Class<T>)Class.forName(enumClassName).asSubclass(Enum.class).asSubclass(PersistentEnum.class);
		
		} catch (ClassNotFoundException e) {
			throw new HibernateException("Enum class não encontrada", e);
		}
		
		setDefaultValue(parameters.getProperty("defaultValue"));
	}
	
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		String codigo = rs.getString(names[0]);
        if(rs.wasNull()) {
            return null;
        }
        for(PersistentEnum value : returnedClass().getEnumConstants()) {
            if(codigo.equals(value.getCodigo())) {
                return value;
            }
        }
        
        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " codigo");
	}
	
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, ((PersistentEnum)value).getCodigo());
        }
		
	}

	/*@Override
	public Object nullSafeGet(ResultSet rs, String[] names,
			SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		
		String codigo = rs.getString(names[0]);
        if(rs.wasNull()) {
            return null;
        }
        for(PersistentEnum value : returnedClass().getEnumConstants()) {
            if(codigo.equals(value.getCodigo())) {
                return value;
            }
        }
        
        throw new IllegalStateException("Unknown " + returnedClass().getSimpleName() + " codigo");
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index,
			SessionImplementor session) throws HibernateException, SQLException {
		if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            st.setString(index, ((PersistentEnum)value).getCodigo());
        }
	}*/

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable)value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


}

