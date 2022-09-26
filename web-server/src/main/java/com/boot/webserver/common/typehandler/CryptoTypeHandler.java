package com.boot.webserver.common.typehandler;

import com.boot.webserver.common.RSAUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

public class CryptoTypeHandler implements TypeHandler<String> {
  @Override
  public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter != null) {
      String encryptedText = RSAUtils.encrypt(parameter);
      ps.setString(i, encryptedText);
    } else {
      ps.setNull(i, Types.VARCHAR);
    }
  }

  @Override
  public String getResult(ResultSet rs, String columnName) throws SQLException {
    String result = rs.getString(columnName);
    if (result == null) {
      return null;
    }

    String decryptedText = RSAUtils.decrypt(result);
    return decryptedText;
  }

  @Override
  public String getResult(ResultSet rs, int columnIndex) throws SQLException {
    String result = rs.getString(columnIndex);
    if (result == null) {
      return null;
    }

    String decryptedText = RSAUtils.decrypt(result);
    return decryptedText;
  }

  @Override
  public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
    String result = cs.getString(columnIndex);
    if (result == null) {
      return null;
    }

    String decryptedText = RSAUtils.decrypt(result);
    return decryptedText;
  }
}
