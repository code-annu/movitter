package developer.anurag.movitter.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import developer.anurag.movitter.datatypes.Genre;

public class AuthenticationUtil {

    public static List<String> getImageUris(){
        List<String> gameOfThronesImageUris = Arrays.asList("https://image.tmdb.org/t/p/original/xM8zPWNqwbgCZQNgOOH2YeM7Cu.jpg",
                "https://image.tmdb.org/t/p/original/ogtPIH1quolQcutu4ycUxmPBwSI.jpg", "https://image.tmdb.org/t/p/original/8OOj2C19XCzcetFBajsJxI0v5sR.jpg");

        List<String> houseOfDragonImageUris=Arrays.asList("https://image.tmdb.org/t/p/original/pRBeweWU9w1Oq6sWoOyYXjPaZe4.jpg",
                "https://image.tmdb.org/t/p/original/woB9ODfxAwjkvepR099bkiv1WaO.jpg","https://image.tmdb.org/t/p/original/10mvZdPGxwZcPcmkJYtuyiIYvY.jpg");

        List<String> breakingBadImageUris=Arrays.asList("https://image.tmdb.org/t/p/original/rKUAAwWnUJSVO7Up72qjqo1myQ4.jpg",
                "https://image.tmdb.org/t/p/original/wSztOsbdspDi0E6dWU1cxNmYDV9.jpg","https://image.tmdb.org/t/p/original/vFxjuhENDjEKzWXUGKmRFct15bA.jpg");

        Random random=new Random();
        int index=random.nextInt(3);
        switch (index){
            case 0:
                return gameOfThronesImageUris;
            case 1:
                return houseOfDragonImageUris;
            default:
                return breakingBadImageUris;
        }
    }

    public static List<String> getAvatarUris(){

        return Arrays.asList("https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F10491830-removebg-preview.png?alt=media&token=1e3acad5-a427-4d46-bc20-02ca085229a1",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F7294793-removebg-preview.png?alt=media&token=6aadd693-225d-46e8-8b3c-e52cac760d92",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F7309681-removebg-preview.png?alt=media&token=95df0291-ea21-4e2d-b7ed-f994d57c79b1",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F7309687-removebg-preview.png?alt=media&token=afe861d1-82a1-4e9a-bf78-c4a67aeea5b2",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F9334176-removebg-preview.png?alt=media&token=ab143b65-b1e7-488b-8e92-519bde13119c",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F9439678-removebg-preview.png?alt=media&token=7abe53ed-fac8-4233-8a25-b6497b7bc64a",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F9440461-removebg-preview.png?alt=media&token=fcefcdc4-20fb-4b90-a8ba-5900cc8a099a",
                "https://firebasestorage.googleapis.com/v0/b/movitter-f803d.appspot.com/o/avatars%2F9723582-removebg-preview.png?alt=media&token=652850bd-2a03-4c91-aeb3-71e1d0281850");
    }

    public static List<String> getAllGenres(){
        List<String> genres = new ArrayList<>();
        genres.add("Action");
        genres.add("Adventure");
        genres.add("Drama");
        genres.add("History Drama");
        genres.add("Comedy");
        genres.add("Fantasy");
        genres.add("Horror");
        genres.add("Mystery");
        genres.add("Romance");
        genres.add("Science Fiction");
        genres.add("Thriller");
        genres.add("War");
        genres.add("Western");
        genres.add("Animation");
        genres.add("Crime");
        genres.add("Family");
        genres.add("Documentary");
        genres.add("Adult");
        return genres;
    }
}
