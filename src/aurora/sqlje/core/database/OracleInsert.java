package aurora.sqlje.core.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import aurora.sqlje.core.ISqlCallStack;

/**
 * primary key generated by sequence
 * 
 * @author jessen
 * 
 */
public class OracleInsert extends AbstractInsertOperation {
	private int pkIndex;

	public OracleInsert(ISqlCallStack context, Map map, String tableName,
			String pkName) {
		super(context, map, tableName, pkName);
		// TODO Auto-generated constructor stub
	}

	public OracleInsert(ISqlCallStack context, Object bean) {
		super(context, bean);
	}

	public OracleInsert(ISqlCallStack context, Object bean, String tableName,
			String pkName) {
		super(context, bean, tableName, pkName);
	}

	@Override
	protected Object execute(PreparedStatement ps) throws SQLException {
		CallableStatement cs = (CallableStatement) ps;
		cs.execute();
		long pk = cs.getLong(pkIndex);
		return pk;
	}

	@Override
	protected int performParameterBinding(PreparedStatement ps)
			throws Exception {
		pkIndex = super.performParameterBinding(ps);
		CallableStatement cs = (CallableStatement) ps;
		cs.registerOutParameter(pkIndex, Types.NUMERIC);
		return pkIndex + 1;
	}

	@Override
	protected String getPrefix() {
		return "BEGIN ";
	}

	@Override
	protected String getSuffix(String pkName) {
		return " RETURNING " + getPkField() + " INTO ${@" + pkName + "}; END;";
	}

//	@Override
//	protected String getDateExpression() {
//		return "trunc(sysdate)";
//	}
//
//	@Override
//	protected String getTimeExpression() {
//		return "sysdate";
//	}

	@Override
	protected String getInsertExpressionForPk() {
		return getTableName() + "_s.nextval";
	}

	@Override
	protected PreparedStatement createStatement(Connection conn, String sql)
			throws SQLException {
		return conn.prepareCall(sql);
	}

}
