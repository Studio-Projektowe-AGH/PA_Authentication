package config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import controllers.SigninController;
import controllers.SignupController;
import models.LoginCredentials;
import org.bson.types.ObjectId;
import services.BasicAuthenticationService;
import services.BasicDataService;
import services.UserAuthenticationService;
import services.UserDataSourceProvider;


/**
 * Created by Wojtek on 21/04/15.
 */
public class GuiceConfig  extends AbstractModule {

    @Override
    public void configure() {
        bind(new TypeLiteral<BasicDataService<LoginCredentials, ObjectId>>(){}).toProvider(UserDataSourceProvider.class);
        bind(BasicAuthenticationService.class).to(UserAuthenticationService.class);

        requestStaticInjection(SigninController.class);
        requestStaticInjection(SignupController.class);
        requestStaticInjection(UserAuthenticationService.class);
    }
}
