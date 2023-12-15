package jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdbc.vo.AccountVO;

// DAO : 쿼리를 직접 작성하는게 아니라 DAO 한테 하게 하는 거
public class AccountDAO {

    // JDBC 관련 변수
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final String url = "jdbc:postgresql://localhost:5432/messenger";
    private static final String username = "sohee";
    private static final String password = "pass";
    // SQL 쿼리
    private final String ACCOUNT_INSERT = "INSERT INTO ACCOUNT (id, username, password) "
        + "VALUES ((SELECT coalesce(MAX(ID), 0) + 1 FROM ACCOUNT A), ?, ?)";

    private final String ACCOUNT_SELECT = "SELECT * FROM account WHERE ID = ?";

    // CRUD 기능 메소드
    public Integer insertAccount(AccountVO vo) {
        var id = -1;

        try {
            String[] resultId = {"id"};
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_INSERT, resultId);
            stmt.setString(1, vo.getUsername()); // 첫번째 ? 인자
            stmt.setString(2, vo.getPassword()); // 두번째 ? 인자
            stmt.executeUpdate(); // 실행

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1); // 첫번째 key값?
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public AccountVO selectAccount(Integer id) {
        AccountVO vo = null;

        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.prepareStatement(ACCOUNT_SELECT);
            stmt.setInt(1, id); // 첫번째 ? 인자
            var rs = stmt.executeQuery(); // 응답값 resultset

            if(rs.next()){
                vo = new AccountVO();
                vo.setId(rs.getInt("ID"));
                vo.setUsername(rs.getString("USERNAME"));
                vo.setPassword(rs.getString("PASSWORD"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vo;
    }
}
