import com.google.inject.Guice;
import com.google.inject.Injector;
import config.GuiceConfig;
import play.Application;
import play.GlobalSettings;

/**
 * Created by Wojtek on 21/04/15.
 */
public class Global extends GlobalSettings {

    private Injector injector;

    @Override
    public void onStart(Application application) {
        injector = Guice.createInjector(new GuiceConfig());
    }
}
