import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Map g = new Map();
//        String banner = Files.readString(Path.of("The_Haunting_Of_Amazon_Hill/resources", "introText"));
//        System.out.println(banner);
//        g.start();
        SplashScreen s = new SplashScreen();
        s.pressAnyKeyToContinue();
        s.splash();
    }
}

