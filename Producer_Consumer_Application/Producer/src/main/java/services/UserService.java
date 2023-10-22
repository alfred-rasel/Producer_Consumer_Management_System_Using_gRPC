package services;

import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import io.grpc.stub.StreamObserver;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class UserService extends userGrpc.userImplBase {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    // Data structure to store user profiles
    private final Map<String, UserProfile> userProfiles = new ConcurrentHashMap<>();

    public void register(User.RegisterReq request, StreamObserver<User.APIRes> responseObserver) {
        String userName = request.getUsername();
        String password = request.getPassword();

        logger.info("Request generated for user registration: " + userName);

        User.APIRes.Builder response = User.APIRes.newBuilder();

        // Check if the user already exists
        if (userProfiles.containsKey(userName)) {
            response.setResCode(400).setMessage("User already exists");
            logger.info("User registration failed for user: " + userName);
        } else {
            // Create a new user profile
            UserProfile userProfile = new UserProfile(userName, password);
            userProfiles.put(userName, userProfile);

            response.setResCode(200).setMessage("Registration successful");
            logger.info("Registration successful for user: " + userName);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    public void setProfile(User.ProfileReq request, StreamObserver<User.APIRes> responseObserver) {
        String userName = request.getUsername();
        String name = request.getName();
        int age = request.getAge();

        logger.info("Request generated for setting user profile: " + userName);

        User.APIRes.Builder response = User.APIRes.newBuilder();

        // Check if the user exists
        if (userProfiles.containsKey(userName)) {
            UserProfile userProfile = userProfiles.get(userName);
            userProfile.setName(name);
            userProfile.setAge(age);

            response.setResCode(200).setMessage("Profile set successfully");
            logger.info("Profile set successfully for user: " + userName);
        } else {
            response.setResCode(400).setMessage("User not found");
            logger.info("Profile setting failed for user: " + userName);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    public void updateProfile(User.UpdateProfileReq request, StreamObserver<User.APIRes> responseObserver) {
        String userName = request.getUsername();
        int newAge = request.getNewAge();

        logger.info("Request generated for updating user profile: " + userName);

        User.APIRes.Builder response = User.APIRes.newBuilder();

        // Check if the user exists
        if (userProfiles.containsKey(userName)) {
            UserProfile userProfile = userProfiles.get(userName);
            userProfile.setAge(newAge);

            response.setResCode(200).setMessage("Profile updated successfully");
            logger.info("Profile updated successfully for user: " + userName);
        } else {
            response.setResCode(400).setMessage("User not found");
            logger.info("Profile update failed for user: " + userName);
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    public void deleteAgeProperty(User.Empty request, StreamObserver<User.APIRes> responseObserver) {
        // Implement logic to delete the age property for a user
        // Be sure to handle any required cleanup operations

        User.APIRes.Builder response = User.APIRes.newBuilder();

        // Check if the user exists
        if (userProfiles.containsKey(request.getUsername())) {
            UserProfile userProfile = userProfiles.get(request.getUsername());
            userProfile.setAge(0); // Set age to 0 to "delete" it

            response.setResCode(200).setMessage("Age property deleted successfully");
            logger.info("Age property deleted successfully for user: " + request.getUsername());
        } else {
            response.setResCode(400).setMessage("User not found");
            logger.info("Age property deletion failed for user: " + request.getUsername());
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private static class UserProfile {
        private String username;
        private String password;
        private String name;
        private int age;

        public UserProfile(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
