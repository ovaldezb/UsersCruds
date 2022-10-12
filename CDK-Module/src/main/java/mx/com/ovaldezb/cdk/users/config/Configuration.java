package mx.com.ovaldezb.cdk.users.config;

public class Configuration {

    public final static String CREATE_USER_FUNCTION_NAME = "createUser";
    public final static String GET_USER_FUNCTION_NAME = "getAllUsers";
    public final static String CREATE_USER_FUNCTION_HANDLER = "mx.com.ovaldez.lambda.user.handlers.UserCreateHandler::handleRequest";
    public final static String GET_USER_FUNCTION_HANDLER = "mx.com.ovaldez.lambda.user.handlers.UserGetHandler::handleRequest";
    public final static String GET_USER_BYID_FUNCTION_NAME="getUserById";
    public final static String GET_USER_BYID_FUNCTION_HANDLER="mx.com.ovaldez.lambda.user.handlers.UserGetByIdHandler::handleRequest";
    public final static String UPDATE_USER_FUNCTION_NAME="updateUser";
    public final static String UPDATE_USER_FUNCTION_HANDLER="mx.com.ovaldez.lambda.user.handlers.UserUpdateHandler::handleRequest";
    public final static String DELETE_USER_FUNCTION_NAME="deleteUser";
    public final static String DELETE_USER_FUNCTION_HANDLER="mx.com.ovaldez.lambda.user.handlers.UserDeleteHandler::handleRequest";
    public final static String TABLE_USER = "USUARIOS";
    public final static String PRIMARY_KEY = "userId";

    public final static String INSERT_USER_CONDO_FUNCTION_NAME= "insertUserCondo";
    public final static String INSERT_USER_CONDO_HANDLER_NAME= "mx.com.ovaldezb.user.dynamo.handler.CreateUserHandler";

    public final static String GETALL_USER_CONDO_FUNCTION_NAME= "getAllUserCondo";
    public final static String GETALL_USER_CONDO_HANDLER_NAME= "mx.com.ovaldezb.user.dynamo.handler.GetAllUserHandler";
    public final static String GET_USERBYID_CONDO_FUNCTION_NAME= "getUserByIdCondo";
    public final static String GET_USERBYID_CONDO_HANDLER_NAME= "mx.com.ovaldezb.user.dynamo.handler.GetUserByIdHandler";
    public final static String UPDATE_USER_CONDO_FUNCTION_NAME= "updateUserCondo";
    public final static String UPDATE_USER_CONDO_HANDLER_NAME= "mx.com.ovaldezb.user.dynamo.handler.UpdateUserHandler";
    public final static String DELETE_USER_CONDO_FUNCTION_NAME= "deleteUserCondo";
    public final static String DELETE_USER_CONDO_HANDLER_NAME= "mx.com.ovaldezb.user.dynamo.handler.DeleteUserHandler";
}
