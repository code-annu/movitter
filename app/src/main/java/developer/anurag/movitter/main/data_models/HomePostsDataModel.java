package developer.anurag.movitter.main.data_models;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.movitter_api.PostRepository;
import developer.anurag.movitter.ui_components.adapters.PostAdapter;

public class HomePostsDataModel extends ViewModel implements PostRepository.PostRepositoryListener{
    private final PostRepository postRepository;
    private List<Post> homePosts=new ArrayList<>();
    private MutableLiveData<List<Post>> fetchedPosts=new MutableLiveData<>();

    public HomePostsDataModel() {
        this.postRepository=new PostRepository(this);
    }

    public MutableLiveData<List<Post>> getFetchedPosts() {
        return this.fetchedPosts;
    }

    public void fetchPosts(int from){
        this.postRepository.getPosts(from);
    }

    public List<Post> getHomePosts() {
        return homePosts;
    }

    @Override
    public void onGetPosts(@Nullable List<Post> posts) {
        this.fetchedPosts.setValue(posts);
        if(posts!=null){
            this.homePosts.addAll(posts);
        }
    }

    @Override
    public void onGetSearchedPosts(List<Post> posts) {

    }

    @Override
    public void onGetAllPostsTitle(List<String> titles) {

    }
}
