package developer.anurag.movitter.datatypes;

import java.util.List;

public class User {
    private String fullname,email,avatarUri;
    private List<String> myPostIds,mySavedPostIds;

    public User() {
    }

    public User(String fullname, String email, String avatarUri) {
        this.fullname = fullname;
        this.email = email;
        this.avatarUri = avatarUri;
    }

    public User(String fullname, String email, String avatarUri, List<String> myPostIds, List<String> mySavedPostIds) {
        this.fullname = fullname;
        this.email = email;
        this.avatarUri = avatarUri;
        this.myPostIds = myPostIds;
        this.mySavedPostIds = mySavedPostIds;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public List<String> getMyPostIds() {
        return myPostIds;
    }

    public void setMyPostIds(List<String> myPostIds) {
        this.myPostIds = myPostIds;
    }

    public List<String> getMySavedPostIds() {
        return mySavedPostIds;
    }

    public void setMySavedPostIds(List<String> mySavedPostIds) {
        this.mySavedPostIds = mySavedPostIds;
    }
}
