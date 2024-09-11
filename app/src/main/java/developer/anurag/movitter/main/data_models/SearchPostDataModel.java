package developer.anurag.movitter.main.data_models;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import developer.anurag.movitter.datatypes.Post;
import developer.anurag.movitter.movitter_api.PostRepository;

public class SearchPostDataModel extends ViewModel implements PostRepository.PostRepositoryListener {
    private MutableLiveData<List<String>> allPostsTitleList;
    private MutableLiveData<List<Post>> searchResults=new MutableLiveData<>();
    private PostRepository postRepository;

    public SearchPostDataModel(){
        this.postRepository=new PostRepository(this);
    }

    public void searchPost(String query){
        this.postRepository.searchPosts(query);
    }

    public MutableLiveData<List<String>> getAllPostsTitleList(){
        if(this.allPostsTitleList==null){
            this.allPostsTitleList=new MutableLiveData<>();
            this.postRepository.getAllPostsTitle();
        }
        return this.allPostsTitleList;
    }

    public MutableLiveData<List<Post>> getSearchResults() {
        return this.searchResults;
    }


    @Override
    public void onGetPosts(@Nullable List<Post> posts) {

    }

    @Override
    public void onGetSearchedPosts(List<Post> posts) {
        this.searchResults.setValue(posts);
    }

    @Override
    public void onGetAllPostsTitle(List<String> titles) {
        this.allPostsTitleList.setValue(titles);
    }
}
