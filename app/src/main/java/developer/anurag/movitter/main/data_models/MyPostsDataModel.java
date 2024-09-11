package developer.anurag.movitter.main.data_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.movitter_api.UserPostService;

public class MyPostsDataModel extends ViewModel implements UserPostService.UserPostServiceListener {
    private MutableLiveData<List<Post>> myPosts=new MutableLiveData<>();
    private UserPostService userPostService;

    public MyPostsDataModel(){
        this.userPostService=new UserPostService(this);
    }

    public void getPostsOfIds(List<String> postIds){
        this.userPostService.getMyPosts(postIds);
    }

    public MutableLiveData<List<Post>> getMyPosts() {
        return myPosts;
    }

    @Override
    public void onGetMyPosts(List<Post> posts) {
        this.myPosts.setValue(posts);
    }

    @Override
    public void onError(String message) {

    }
}
