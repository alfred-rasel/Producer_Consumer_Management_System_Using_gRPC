import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.logging.Logger;

public class GrpcClient {

    private static final Logger logger = Logger.getLogger(GrpcClient.class.getName());

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);

        // Registration example
        User.RegisterReq registerRequest = User.RegisterReq.newBuilder()
                .setUsername("new_user")
                .setPassword("new_password")
                .build();
        User.APIRes registerResponse = userStub.register(registerRequest);

        // Setting profile example
        User.ProfileReq setProfileRequest = User.ProfileReq.newBuilder()
                .setUsername("user")
                .setName("John Doe")
                .setAge(30)
                .build();
        User.APIRes setProfileResponse = userStub.setProfile(setProfileRequest);

        // Continue with login, update profile, and delete profile examples
        // ...

        logger.info(registerResponse.getResCode() + "\n" + registerResponse.getMessage());
    }
}
