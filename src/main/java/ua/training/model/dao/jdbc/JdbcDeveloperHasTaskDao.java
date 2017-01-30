package ua.training.model.dao.jdbc;

import org.apache.log4j.Logger;
import ua.training.model.dao.DeveloperHasTaskDao;
import ua.training.model.dao.exception.DaoException;
import ua.training.model.entity.DeveloperHasTask;
import ua.training.model.entity.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by andrii on 20.01.17.
 */
public class JdbcDeveloperHasTaskDao extends AbstractJdbcDao<DeveloperHasTask>
        implements DeveloperHasTaskDao {

    private static final String INSERT_INTO_DEVELOPER_HAS_TASK = "INSERT INTO " +
            "developer_has_task (developer_id, task_id, project_id, elapsed_time)" +
            " VALUES ( ?, ?, ?, ? ) ";
    private static final String SELECT_FROM_DEVELOPER_HAS_TASK = "SELECT * FROM " +
            "developer_has_task JOIN task ON task.id = developer_has_task.task_id " +
            "JOIN project ON project.id = developer_has_task.project_id " +
            "JOIN user ON user.id = developer_has_task.developer_user_id " +
            "JOIN developer ON user.id = developer.user_id " +
            "JOIN statement_of_work ON statement_of_work.id = project.statement_of_work_id ";;
    private static final String WHERE_DEVELOPER_ID =
            "WHERE developer_has_task.developer_user_id = ? ";

    public static final String DEVELOPER_ID = "developer_has_task.developer_user_id";
    public static final String TASK_ID = "developer_has_task.task_id";
    public static final String PROJECT_ID = "developer_has_task.project_id";
    public static final String ELAPSED_TIME = "developer_has_task.elapsed_time";

    private static Logger logger = Logger.getLogger(JdbcDeveloperHasTaskDao.class);

    public JdbcDeveloperHasTaskDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectAllQuery() {
        return SELECT_FROM_DEVELOPER_HAS_TASK;
    }

    @Override
    protected String getCreateQuery() {
        return INSERT_INTO_DEVELOPER_HAS_TASK;
    }

    @Override
    protected String getUpdateQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getDeleteQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected DeveloperHasTask getEntityFromResultSet(ResultSet resultSet) throws SQLException {
        return new DeveloperHasTask.Builder()
                .setDeveloper(JdbcDeveloperDao.getDeveloperFromResultSet(resultSet))
                .setTask(JdbcTaskDao.getTaskFromResultSet(resultSet))
                .setProject(JdbcProjectDao.getProjectFromResultSet(resultSet))
                .setElapsedTime(resultSet.getInt(ELAPSED_TIME))
                .build();
    }

    @Override
    protected void setIdForEntity(DeveloperHasTask entity, int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement query, DeveloperHasTask entity)
            throws SQLException {
        query.setInt(1 , entity.getDeveloper().getUser().getId());
        query.setInt(2, entity.getTask().getId());
        query.setInt(3, entity.getProject().getId());
        query.setInt(4, entity.getElapsedTime());
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement query, DeveloperHasTask entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DeveloperHasTask> find(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getSelectByIdQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeveloperHasTask> findByDeveloperId(Integer id) { // todo
        List<DeveloperHasTask> result = new ArrayList<>();
        try(PreparedStatement query =
                    connection.prepareStatement(SELECT_FROM_DEVELOPER_HAS_TASK
                            + WHERE_DEVELOPER_ID)){
            query.setInt(1 , id);
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                result.add(getEntityFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException(e);
        }
        return result;
    }
}
